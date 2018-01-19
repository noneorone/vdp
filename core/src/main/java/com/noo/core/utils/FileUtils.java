package com.noo.core.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.StatFs;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.noo.core.R;
import com.noo.core.app.VdpApplicationWrapper;
import com.noo.core.log.Logger;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * IO流处理<br/>
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/3/30 17:52<br/>
 * @since 1.0
 */
public class FileUtils {
    private FileUtils() {
    }

    /**
     * 检测Intent是否可用
     *
     * @param context
     * @param action
     * @return
     */
    public static boolean isIntentAvaliable(Context context, String action) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(action);
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfos.size() > 0;
    }

    /**
     * 获取Uri，兼容7.0及以上
     *
     * @param intent
     * @param file
     * @return
     */
    public static Uri getUri(Intent intent, File file) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Context context = VdpApplicationWrapper.get().getApplicationContext();
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    /**
     * 获取一个用于打开HTML文件的intent
     *
     * @param file
     * @return
     */
    public static Intent getHtmlFileIntent(File file) {
        Uri uri = Uri.parse(file.toString()).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(file.toString()).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    /**
     * 获取一个用于打开图片文件的intent
     *
     * @param file
     * @return
     */
    public static Intent getImageFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        Uri uri = getUri(intent, file);
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    /**
     * 获取一个用于打开PDF文件的intent
     *
     * @param file
     * @return
     */
    public static Intent getPdfFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        Uri uri = getUri(intent, file);
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    /**
     * 获取一个用于打开文本文件的inten
     *
     * @param file
     * @return
     */
    public static Intent getTextFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        Uri uri = getUri(intent, file);
        intent.setDataAndType(uri, "text/plain");
        return intent;
    }

    /**
     * 获取一个用于打开音频文件的intent
     *
     * @param file
     * @return
     */
    public static Intent getAudioFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = getUri(intent, file);
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    /**
     * 获取一个用于打开视频文件的intent
     *
     * @param file
     * @return
     */
    public static Intent getVideoFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = getUri(intent, file);
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    /**
     * 获取一个用于打开CHM文件的intent
     *
     * @param file
     * @return
     */
    public static Intent getChmFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        Uri uri = getUri(intent, file);
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    /**
     * 获取一个用于打开Word文件的intent
     *
     * @param file
     * @return
     */
    public static Intent getWordFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        Uri uri = getUri(intent, file);
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    /**
     * 获取一个用于打开Excel文件的intent
     *
     * @param file
     * @return
     */
    public static Intent getExcelFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        Uri uri = getUri(intent, file);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    /**
     * 获取一个用于打开PPT文件的intent
     *
     * @param file
     * @return
     */
    public static Intent getPPTFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        Uri uri = getUri(intent, file);
        String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        if (extension.equalsIgnoreCase(".ppt")) {
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (extension.equalsIgnoreCase(".pptx")) {
            intent.setDataAndType(uri, "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        }
        return intent;
    }

    /**
     * 获取一个用于打开apk文件的intent
     *
     * @param file
     * @return
     */
    public static Intent getApkFileIntent(File file) {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(getUri(intent, file), "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * 获取文件名称
     *
     * @param filePath
     * @return
     */
    public static String getFileNameAndExt(String filePath) {
        try {
            int p1 = filePath.lastIndexOf("/") + 1;
            int p2 = filePath.length() - 1;
            return filePath.substring(p1, p2);
        } catch (Exception e) {
            Logger.e(e);
        }
        return null;
    }

    /**
     * 获取文件名称
     *
     * @param filePath
     * @return
     */
    public static String getName(String filePath) {
        try {
            int p1 = filePath.lastIndexOf("/");
            int p2 = filePath.lastIndexOf(".");
            return filePath.substring(p1 + 1, p2);
        } catch (Exception e) {
            Logger.e(e);
        }
        return null;
    }

    /**
     * 获取文件后缀
     *
     * @param filePath
     * @return
     */
    public static String getExtension(String filePath) {
        if (filePath == null) return null;
        try {
            int p2 = filePath.lastIndexOf(".");
            if (p2 != -1) {
                return filePath.substring(p2, filePath.length());
            }
        } catch (Exception e) {
            Logger.e(e);
        }
        return null;
    }

    /**
     * 获取文件后缀
     *
     * @param file
     * @return
     */
    public static String getExtension(File file) {
        if (null != file && file.isFile()) {
            String name = file.getName();
            if (null != name && name.contains(".")) {
                return name.substring(name.lastIndexOf("."), name.length());
            }
        }

        return null;
    }

    /**
     * 获取文件后缀
     *
     * @param fileName
     * @return
     */
    public static String getExtensionByFileName(String fileName) {
        if (null != fileName && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."), fileName.length());
        }
        return "";
    }

    /**
     * 检测该文件是否为图片文件
     *
     * @param filePath
     * @return
     */
    public static boolean isImageFile(String filePath) {
        String extension = getExtension(filePath);
        if (null != extension) {
            Resources resources = VdpApplicationWrapper.get().getResources();
            if (Arrays.toString(resources.getStringArray(R.array.fileEndingImage)).contains(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据指定的文件在其所在的目录中创建一个临时同类型的文件
     *
     * @param filePath
     * @return 创建成功则返回该临时文件，若创建失败则返回null
     */
    public static File createTempFileInnerDir(String filePath) {
        try {

            String name = getName(filePath);
            String extension = getExtension(filePath);
            if (name != null && extension != null) {
                int length = name.trim().length();
                int minLength = 2;
                if (length < minLength) {
                    for (int i = 0; i < minLength - length; i++) {
                        name = name.concat("_");
                    }
                }
                return File.createTempFile(name.concat("_"), extension);

            }
        } catch (Exception e) {
            Logger.e(e);
        }
        return null;
    }

    /**
     * 根据不同文件类型设置指定ImageView对象的图标
     *
     * @param iv
     * @param extension
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static ImageView setImageIcon(ImageView iv, String extension) {
        Resources resources = VdpApplicationWrapper.get().getResources();
        extension = extension.toLowerCase(Locale.getDefault());
        if (Arrays.toString(resources.getStringArray(R.array.fileEndingImage)).contains(extension)) {
            iv.setImageResource(R.drawable.icon_file_image);
        } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingPhotoShop)).contains(extension)) {
            iv.setImageResource(R.drawable.icon_file_ps);
        } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingWebText)).contains(extension)) {
            iv.setImageResource(R.drawable.icon_file_html);
        } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingPackage)).contains(extension)) {
            iv.setImageResource(R.drawable.icon_file_compress);
        } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingApk)).contains(extension)) {
            iv.setImageResource(R.drawable.icon_file_apk);
        } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingAudio)).contains(extension)) {
            iv.setImageResource(R.drawable.icon_file_music);
        } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingVideo)).contains(extension)) {
            iv.setImageResource(R.drawable.icon_file_video);
        } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingText)).contains(extension)) {
            iv.setImageResource(R.drawable.icon_file_txt);
        } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingPdf)).contains(extension)) {
            iv.setImageResource(R.drawable.icon_file_pdf);
        } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingWord)).contains(extension)) {
            iv.setImageResource(R.drawable.icon_file_word);
        } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingExcel)).contains(extension)) {
            iv.setImageResource(R.drawable.icon_file_excel);
        } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingPPT)).contains(extension)) {
            iv.setImageResource(R.drawable.icon_file_ppt);
        } else {
            iv.setImageResource(R.drawable.icon_file_others);
        }

        return iv;
    }

    /**
     * 根据文件名获取对应文件类型的图标资源ID
     *
     * @param fileName
     * @return
     */
    public static int getIconResId(String fileName) {
        if (fileName == null) {
            return R.drawable.icon_file_others;
        }
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            String extension = fileName.substring(index).toLowerCase(Locale.getDefault());
            Resources resources = VdpApplicationWrapper.get().getResources();
            if (Arrays.toString(resources.getStringArray(R.array.fileEndingImage)).contains(extension)) {
                return R.drawable.icon_file_image;
            } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingPhotoShop)).contains(extension)) {
                return R.drawable.icon_file_ps;
            } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingWebText)).contains(extension)) {
                return R.drawable.icon_file_html;
            } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingPackage)).contains(extension)) {
                return R.drawable.icon_file_compress;
            } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingApk)).contains(extension)) {
                return R.drawable.icon_file_apk;
            } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingAudio)).contains(extension)) {
                return R.drawable.icon_file_music;
            } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingVideo)).contains(extension)) {
                return R.drawable.icon_file_video;
            } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingText)).contains(extension)) {
                return R.drawable.icon_file_txt;
            } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingPdf)).contains(extension)) {
                return R.drawable.icon_file_pdf;
            } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingWord)).contains(extension)) {
                return R.drawable.icon_file_word;
            } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingExcel)).contains(extension)) {
                return R.drawable.icon_file_excel;
            } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingPPT)).contains(extension)) {
                return R.drawable.icon_file_ppt;
            }
        }
        return R.drawable.icon_file_others;
    }

    /**
     * 打开文件
     *
     * @param path
     * @param activity
     */
    public static void openFile(String path, Activity activity) {
        if (!TextUtils.isEmpty(path)) {
            openFile(new File(path), activity);
        }
    }

    /**
     * 打开文件
     *
     * @param file
     * @param activity
     */
    public static void openFile(File file, Activity activity) {
        Resources resources = activity.getResources();
        try {
            if (null != file && file.isFile()) {
                // 获取文件名及其扩展
                String fileName = file.getName();
                int index = fileName.lastIndexOf(".");
                if (index == -1) {
                    // 如果没有文件名后缀，则提示不能打开
                    ToastUtils.showToast(activity, R.string.file_cannot_be_open);
                    return;
                }
                String extension = fileName.substring(index).toLowerCase(Locale.getDefault());
                Intent intent = null;
                if (Arrays.toString(resources.getStringArray(R.array.fileEndingImage)).contains(extension)) {
                    intent = FileUtils.getImageFileIntent(file);
                    activity.startActivity(intent);
                } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingWebText)).contains(extension)) {
                    intent = FileUtils.getHtmlFileIntent(file);
                    activity.startActivity(intent);
                } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingPackage)).contains(extension)) {
                    intent = FileUtils.getApkFileIntent(file);
                    activity.startActivity(intent);
                } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingAudio)).contains(extension)) {
                    intent = FileUtils.getAudioFileIntent(file);
                    activity.startActivity(intent);
                } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingVideo)).contains(extension)) {
                    intent = FileUtils.getVideoFileIntent(file);
                    activity.startActivity(intent);
                } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingText)).contains(extension)) {
                    intent = FileUtils.getTextFileIntent(file);
                    activity.startActivity(intent);
                } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingPdf)).contains(extension)) {
                    intent = FileUtils.getPdfFileIntent(file);
                    activity.startActivity(intent);
                } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingWord)).contains(extension)) {
                    intent = FileUtils.getWordFileIntent(file);
                    activity.startActivity(intent);
                } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingExcel)).contains(extension)) {
                    intent = FileUtils.getExcelFileIntent(file);
                    activity.startActivity(intent);
                } else if (Arrays.toString(resources.getStringArray(R.array.fileEndingPPT)).contains(extension)) {
                    intent = FileUtils.getPPTFileIntent(file);
                    activity.startActivity(intent);
                } else {
                    ToastUtils.showToast(activity, R.string.file_cannot_be_open);
                }
            } else {
                ToastUtils.showToast(activity, R.string.file_invalid);
            }
        } catch (ActivityNotFoundException e) {
            Logger.e(e);
            ToastUtils.showToast(activity, R.string.file_cannot_be_open);
        }
    }

    /**
     * 获取外设存储目录，包括内部SD卡、扩展卡等
     */
    public static File[] getExternalStorageDirectory() {
        List<File> files = new ArrayList<File>();
        Process process = null;
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            Runtime runtime = Runtime.getRuntime();
            process = runtime.exec("mount");
            inputStream = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(inputStream);
            String line = null;
            @SuppressWarnings("unused")
            StringBuffer lines = new StringBuffer();
            reader = new BufferedReader(isr);
            while ((line = reader.readLine()) != null) {
                if (line.contains("secure"))
                    continue;
                if (line.contains("asec"))
                    continue;

                // vfat
                if (line.contains("fat") || line.contains("fuse")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        files.add(new File(columns[1]));
                    }
                }
            }
        } catch (IOException e) {
            Logger.e(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    Logger.e(e);
                }
            }
            if (process != null) {
                try {
                    process.destroy();
                } catch (Exception e) {
                    Logger.e(e);
                }
            }
        }

        return files.size() > 0 ? files.toArray(new File[0]) : null;
    }

    /**
     * 获取可装载的外设
     *
     * @return
     */
    public static HashSet<String> getExternalMounts() {
        final HashSet<String> out = new HashSet<String>();
        String reg = "(?i).*vold.*(vfat|ntfs|exfat|fat32|ext3|ext4).*rw.*";
        String s = "";
        Process process = null;
        InputStream inputStream = null;
        try {
            process = new ProcessBuilder().command("mount").redirectErrorStream(true).start();
            process.waitFor();
            inputStream = process.getInputStream();
            final byte[] buffer = new byte[1024];
            while (inputStream.read(buffer) != -1) {
                s = s + new String(buffer);
            }
        } catch (final Exception e) {
            Logger.e(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    Logger.e(e);
                }
            }
            if (process != null) {
                try {
                    process.destroy();
                } catch (Exception e) {
                    Logger.e(e);
                }
            }
        }

        // parse output
        final String[] lines = s.split("\n");
        for (String line : lines) {
            if (!line.toLowerCase(Locale.US).contains("asec")) {
                if (line.matches(reg)) {
                    String[] parts = line.split(" ");
                    for (String part : parts) {
                        if (part.startsWith("/"))
                            if (!part.toLowerCase(Locale.US).contains("vold"))
                                out.add(part);
                    }
                }
            }
        }

        return out;
    }

    /**
     * 获取存储设备列表（包括内部SD卡和外部扩展卡）
     *
     * @return
     */
    public static File[] getStorageDirectory() {
        File externalStorageDir = Environment.getExternalStorageDirectory();
        File storageDir = new File(externalStorageDir.getParent());
        File[] dirs = storageDir.listFiles();

        if (null != dirs && dirs.length > 0) {
            List<File> files = new ArrayList<File>();
            for (File dir : dirs) {
                if ("secure,asec,obb".contains(dir.getName()))
                    continue;
                if (dir.canRead() && getDirAvailableSize(dir) > 0) {
                    files.add(dir);
                }
            }
            if (files.size() > 0) {
                return files.toArray(new File[0]);
            }
        }
        return null;
    }

    /**
     * 获取指定目录可用空间大小
     *
     * @param dir
     * @return
     */
    @SuppressWarnings("deprecation")
    public static long getDirAvailableSize(File dir) {
        // 获取文件系统空间
        StatFs statFs = new StatFs(dir.getPath());
        // 获取可供程序使用的块
        long availableBlocks = statFs.getAvailableBlocks();
        // 获取块大小
        long blockSize = statFs.getBlockSize();
        // 获取剩余可用空间，若是扩展卡则返回空间值大小为负数，所以在此取绝对值
        return Math.abs(availableBlocks * blockSize);
    }

    /**
     * 根据附件名称获取存储卡对应以项目名称命名的文件夹中的附件
     *
     * @return
     */
    public static File getFileInAppDir(String fileName) {
        // 根据应用名来创建目录
        Context context = VdpApplicationWrapper.get();
        String appNameEn = context.getString(R.string.app_name_en);
        // 获取存储卡目录
        File[] storageDirs = getStorageDirectory();
        if (null != storageDirs && storageDirs.length > 0) {
            for (File storageDir : storageDirs) {
                File saveDir = new File(storageDir, appNameEn);
                if (saveDir.exists()) {
                    // 文件保存目录，过滤文件名中的特殊字符，否则会异常
                    File file = new File(saveDir, StringUtils.sanitizeFilename(fileName));
                    if (file.exists()) {
                        return file;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 采用了新的办法获取APK图标，之前的失败是因为android中存在的一个BUG,通过 appInfo.publicSourceDir =
     * apkPath;来修正这个问题，详情参见:
     * http://code.google.com/p/android/issues/detail?id=9151
     */
    public static Drawable getApkIcon(String apkPath) {
        PackageManager pm = VdpApplicationWrapper.get().getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            try {
                return appInfo.loadIcon(pm);
            } catch (OutOfMemoryError e) {
                Log.e("ApkIconLoader", e.toString());
            }
        }
        return null;
    }

    /**
     * 安装 apk
     *
     * @param file 需要安装的apk文件
     */
    public static void install(File file) {
        Intent intent = new Intent();
        //
        intent.setAction(Intent.ACTION_VIEW);
        // 设置文件路径，设置文件类型，apk文件类型为application/vnd.android.package-archive（可以从tomcat的web.xml文件中查找apk对应的类型）
        intent.setDataAndType(getUri(intent, file), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        VdpApplicationWrapper.get().startActivity(intent);
    }

    /**
     * 把一个文件转化为字节
     *
     * @param file
     * @return byte[]
     * @throws Exception
     */
    public static byte[] getByte(File file) {
        int len = 0;
        byte[] bytes = null;
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;

        try {
            fis = new FileInputStream(file);
            baos = new ByteArrayOutputStream();
            bytes = new byte[1024];
            while ((len = fis.read(bytes)) != -1) {
                baos.write(bytes, 0, len);
            }
            bytes = baos.toByteArray();
        } catch (OutOfMemoryError e) {
            Logger.e(e);
        } catch (Exception e) {
            Logger.e(e);
        } finally {
            if (null != baos) {
                try {
                    baos.close();
                } catch (Exception e) {
                }
            }
            if (null != fis) {
                try {
                    fis.close();
                } catch (Exception e) {
                }
            }
        }

        return bytes;
    }


    /**
     * 检测文件的类型
     *
     * @param path
     * @param isShowError
     * @return
     */
    public static File isValidFile(String path, boolean... isShowError) {
        File file = null;

        if (null != path) {
            file = new File(path);
            // 文件存在、文件类型、非空文件
            if (file.exists() && file.isFile() && file.length() > 0) {
                return file;
            } else {
                file = null;
            }
        }

        // 为空文件时，是否需要提醒
        if (null == file) {
            if (null != isShowError && isShowError.length > 0) {
                boolean showError = isShowError[0];
                if (showError) {
                    new Thread() {
                        public void run() {
                            Looper.prepare();
                            Toast.makeText(VdpApplicationWrapper.get(), R.string.file_is_empty, Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }

                        ;
                    }.start();
                }
            }
        }

        return file;

    }


    /**
     * 获取文件的 MimeType
     *
     * @param file
     * @return
     */
    public static String getMimeType(File file) {
        if (file != null) {
            return getMimeType(file.getPath());
        }
        return null;
    }

    /**
     * 获取文件的 MimeType
     *
     * @param filePath
     * @return
     */
    public static String getMimeType(String filePath) {
        try {
            FileNameMap fileNameMap = URLConnection.getFileNameMap();
            return fileNameMap.getContentTypeFor(filePath);
        } catch (Exception e) {
            Logger.e(e);
        }
        return null;
    }


    /**
     * 检测指定路径的文件是否为图片
     */
    @SuppressLint("DefaultLocale")
    public static boolean isImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        boolean result = false;
        String lower = path.toLowerCase();
        if (lower.endsWith(".bmp") || lower.endsWith(".dib") || lower.endsWith(".gif") ||
                lower.endsWith(".jfif") || lower.endsWith(".jpe") || lower.endsWith(".jpeg") ||
                lower.endsWith(".jpg") || lower.endsWith(".png") || lower.endsWith(".tif") ||
                lower.endsWith(".tiff") || lower.endsWith(".ico")) {
            result = true;
        }
        return result;
    }

    /**
     * 判断是否图片
     *
     * @param fileExt 文件后缀名
     * @return
     */
    public static boolean isPictrue(String fileExt) {
        if (TextUtils.isEmpty(fileExt)) {
            String[] pictrueExt = VdpApplicationWrapper.get().getResources().getStringArray(R.array.fileEndingImage);
            if (pictrueExt != null) {
                for (String ext : pictrueExt) {
                    if (ext.contains(fileExt.toLowerCase(Locale.getDefault()))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 根据文件网络资源链接和指定后缀名来检测是否已经存在本地
     *
     * @param url       文件网络资源全路径URL链接 eg.http://file.ynet.com/2/1507/22/10248322-500.jpg
     * @param extension 预保存的文件名后缀 eg. .png|.jpg|.txt|.py
     * @return File 若找到本地已经存在的文件，则直接返回本地文件全路径；若不存在，则返回新的文件对象，否则返回空对象
     */
    public static File getFileWithLink(String url, String extension) {
        if (!TextUtils.isEmpty(url)) return null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            String apn = isPictrue(extension) ? "images" : "files";
            File dir = new File(Environment.getExternalStorageDirectory(), apn);
            if (!dir.exists() || !dir.isDirectory()) {
                dir.mkdirs();
            }
            String preName = PasswordEncoder.encode(url);
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (null != files && files.length > 0) {
                    for (File file : files) {
                        if (file.isFile()) {
                            String fileName = file.getName();
                            int index = fileName.indexOf(".");
                            if (index != -1) {
                                String name = fileName.substring(0, index);
                                String fileExtension = fileName.substring(index, fileName.length());
                                if (fileExtension.equals(extension) && name.equals(preName)) {
                                    return file;
                                }
                            } else {
                                if (fileName.equals(preName)) {
                                    return file;
                                }
                            }
                        }
                    }
                }
            }

            if (TextUtils.isEmpty(extension)) {
                preName = preName.concat(extension);
            }
            return new File(dir, preName);
        }
        return null;
    }


}
