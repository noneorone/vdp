package com.noo.core.log;

import android.content.Context;
import android.os.Environment;

import com.noo.core.utils.AppUtils;
import com.noo.core.utils.DateUtils;
import com.noo.core.utils.IOUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 日志记录器<br/>
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/3/30 14:39<br/>
 * @since 1.0
 */
public class LoggerRecorder {

    /**
     * 默认单个日志最多保留天数7天
     */
    private static final long MAX_RENCENT_DAY = 7;

    /**
     * 默认单个日志文件最大为5M
     */
    private static final long MAX_LENGTH_SINGLE_FILE = 5 * 1024 * 1024;

    private static ExecutorService executor;

    private static String logPath;
    private static SimpleDateFormat dateFormat;

    private LoggerRecorder() {
    }

    /**
     * 初始化
     */
    public static void init(Context context) {
        executor = Executors.newFixedThreadPool(5);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        logPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + AppUtils.getAppName(context) + "/log";

        mkLogRootDir();
        startCheckCrudLogFiles();
    }

    /**
     * 清除资源
     */
    public static void releaseResource() {
        if (executor != null && !executor.isShutdown() && !executor.isTerminated()) {
            executor.shutdown();
        }
    }

    /**
     * 创建日志根目录
     */
    private static boolean mkLogRootDir() {
        if (AppUtils.isStorageMounted()) {
            File file = new File(logPath);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.exists();
        }
        return false;
    }

    /**
     * 开始检查脏日志以便腾出空间
     */
    private static void startCheckCrudLogFiles() {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                removeLogFiles();
            }
        });
    }

    /**
     * 删除日志文件
     */
    private static void removeLogFiles() {
        File rootDir = new File(logPath);
        if (rootDir.exists()) {
            File[] dirs = rootDir.listFiles();
            if (dirs != null) {
                for (File dir : dirs) {
                    File[] files = dir.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            int daysGap = DateUtils.getTwoDaysGap(file.lastModified(), System.currentTimeMillis());
                            if (daysGap >= MAX_RENCENT_DAY) {
                                file.delete();
                            }
                        }
                    }
                }
            }

        }
    }


    /**
     * 异步记录到文件
     *
     * @param type    类型串
     * @param content 内容
     */
    public static void asynWriteToFile(final String type, final String content) {
        executor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                return writeToFile(type, content);
            }
        });
    }

    /**
     * 记录到文件
     *
     * @param type    类型串
     * @param content 内容
     */
    public static boolean writeToFile(String type, String content) {
        try {
            File dir = new File(logPath, type);
            if (!dir.getParentFile().exists()) {
                mkLogRootDir();
            }
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (dir.exists()) {
                String name = dateFormat.format(System.currentTimeMillis());
                File file = new File(dir, name + ".log");
                synchronized (file) {
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    if (file.exists()) {
                        if (file.length() > MAX_LENGTH_SINGLE_FILE) {
                            String newName = name + "_" + System.currentTimeMillis() + ".log";
                            file.renameTo(new File(file.getParentFile(), newName));
                            writeToFile(type, content);
                        } else {
                            IOUtils.writeStringToFile(file, content, true);
                            file.setLastModified(System.currentTimeMillis());
                        }
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


}
