package com.noo.core.ui.web;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.widget.Toast;

import com.noo.core.log.Logger;
import com.noo.core.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * {@link DownloadListener}扩展，支持多种下载模式
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2018/1/18 10:34<br/>
 * @since 1.0
 */
public class VdpDownloadManager implements DownloadListener {

    /**
     * 支持的下载模式
     */
    public enum Mode {
        /**
         * 通过系统所支持的文件下载方式来下载
         */
        ACTION_VIEW,
        /**
         * 通过系统默认的下载管理器中下载
         */
        DOWNLOAD_MGR,
        /**
         * 通过用户自定义下载方式
         */
        USER_DEFINED
    }

    private Mode mode;
    private Context context;

    private String url;
    private String mimetype;
    private String userAgent;
    private long contentLength;
    private String contentDisposition;

    public VdpDownloadManager(Context context, Mode mode) {
        this.mode = mode;
        this.context = context;
    }

    private void setDownloadInfo(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        this.url = url;
        this.mimetype = mimetype;
        this.userAgent = userAgent;
        this.contentLength = contentLength;
        this.contentDisposition = contentDisposition;

        Logger.d(String.format(
                "url:%s, userAgent:%s, contentDisposition:%s, mimetype:%s, contentLength:%s ",
                url,
                userAgent,
                contentDisposition,
                mimetype,
                contentLength
        ));
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        setDownloadInfo(url, userAgent, contentDisposition, mimetype, contentLength);

        switch (mode) {
            case ACTION_VIEW:
                byActionViewMode();
                break;
            case DOWNLOAD_MGR:
                byDownloadMgrMode();
                break;
            case USER_DEFINED:
                byUserDefinedMode();
                break;
            default:
                ;
        }
    }

    /**
     * 以{@link Mode#ACTION_VIEW}的模式下载文件
     */
    private void byActionViewMode() {
        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    /**
     * 以{@link Mode#DOWNLOAD_MGR}的模式下载文件
     */
    private void byDownloadMgrMode() {
        if (context != null && context instanceof Activity) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            request.setVisibleInDownloadsUi(true);
            request.addRequestHeader("Cookie", CookieManager.getInstance().getCookie(url));
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, getFileName());
            request.setShowRunningNotification(true);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            Activity activity = (Activity) context;
            DownloadManager downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
            if (downloadManager != null) {
                downloadManager.enqueue(request);
            }
        }
    }

    /**
     * 以{@link Mode#USER_DEFINED}的模式下载文件
     */
    private void byUserDefinedMode() {
        Request request = new Request.Builder().url(url).build();
        OkHttpClient client = new OkHttpClient();
        final Call call = client.newCall(request);
        final boolean[] isCallFinished = {false};
        final String[] localFilePath = new String[1];

        final ProgressDialog dialog = getProgressDialog();
        dialog.setMessage(getFileName() + " is DOWNLOADING...");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isCallFinished[0]) {
                    dialog.dismiss();
                    if (localFilePath.length > 0 && context instanceof Activity) {
                        FileUtils.openFile(localFilePath[0], (Activity) context);
                    }
                }
            }
        });
        dialog.show();

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                setProgressMessage(dialog, "download FAILED!!!");
                Logger.e(e);
                isCallFinished[0] = true;
            }

            @Override
            public void onResponse(Call call, Response response) {
                File file = null;
                FileOutputStream os = null;
                try {
                    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                        file = new File(Environment.getExternalStorageDirectory(), getFileName());
                        if (file.exists()) {
                            file.delete();
                        }
                        os = new FileOutputStream(file);
                        ResponseBody body = response.body();
                        if (body != null) {
                            InputStream is = body.byteStream();
                            byte[] b = new byte[10240];
                            int length = 0;
                            int sumLength = 0;
                            while ((length = is.read(b)) != -1) {
                                Logger.d("length: " + length);
                                os.write(b, 0, length);
                                sumLength += length;
                                updateProgress(dialog, Double.valueOf((1.0 * sumLength / contentLength) * 100).intValue());
                            }
                            os.close();

                            localFilePath[0] = file.getAbsolutePath();
                            setProgressMessage(dialog, "file stored in " + file.getAbsolutePath());
                        }
                    }
                } catch (Exception e) {
                    setProgressMessage(dialog, "exception OCCURED!!!");
                    Logger.e(e);
                } finally {
                    isCallFinished[0] = true;
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            // nothing needs to be care
                        } finally {
                            os = null;
                        }
                    }
                }
            }
        });
    }

    /**
     * 通过{@link #contentDisposition}来截取文件名
     *
     * @return 文件名
     */
    private String getFileName() {
        String[] disp = contentDisposition.split(";");
        for (String s : disp) {
            if (s.contains("filename")) {
                if (s.contains("*=")) {
                    return s.substring(s.lastIndexOf("'") + 1);
                } else {
                    return s.substring(s.indexOf("\"") + 1, s.length() - 1);
                }
            }
        }
        return contentDisposition;
    }

    /**
     * 构造进度条对话框
     *
     * @return {@link ProgressDialog}
     */
    private ProgressDialog getProgressDialog() {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setMax(100);
        dialog.setProgress(0);
        return dialog;
    }

    /**
     * 设置进度条对话框显示消息
     *
     * @param dialog  {@link ProgressDialog}
     * @param message 消息串
     */
    private void setProgressMessage(final ProgressDialog dialog, final String message) {
        if (context instanceof Activity && dialog != null) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog.setMessage(message);
                }
            });
        } else {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 设置进度条对话框显示消息
     *
     * @param dialog   {@link ProgressDialog}
     * @param progress 进度
     */
    private void updateProgress(final ProgressDialog dialog, final int progress) {
        if (context instanceof Activity && dialog != null) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog.setProgress(progress);
                }
            });
        }
    }

}
