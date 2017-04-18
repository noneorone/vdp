package com.dmio.org.tut.core.log;

import android.os.Environment;

import com.dmio.org.tut.core.utils.AppUtils;
import com.dmio.org.tut.core.utils.DateUtils;
import com.dmio.org.tut.core.utils.IOUtils;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 功能说明：日志记录器<br/>
 * 作者：wangmeng on 2017/3/30 14:39<br/>
 * 邮箱：noneorone@yeah.net
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

    /**
     * 日志文件根目录路径
     */
    private static final String LOG_FILE_ROOT_DIR_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + AppUtils.getAppName() + "/log";

    private ExecutorService executor;
    private static LoggerRecorder instance;

    private LoggerRecorder() {
        executor = Executors.newFixedThreadPool(5);
    }

    public static final LoggerRecorder getInstance() {
        if (instance == null) {
            synchronized (LoggerRecorder.class) {
                if (instance == null) {
                    instance = new LoggerRecorder();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化
     */
    public void init() {
        mkLogRootDir();
        startCheckCrudLogFiles();
    }

    /**
     * 创建日志根目录
     */
    private boolean mkLogRootDir() {
        if (AppUtils.isStorageMounted()) {
            File file = new File(LOG_FILE_ROOT_DIR_PATH);
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
    private void startCheckCrudLogFiles() {
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
    private void removeLogFiles() {
        File rootDir = new File(LOG_FILE_ROOT_DIR_PATH);
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
    public void asynWriteToFile(final String type, final String content) {
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
    public boolean writeToFile(String type, String content) {
        try {
            File dir = new File(LOG_FILE_ROOT_DIR_PATH, type);
            if (!dir.getParentFile().exists()) {
                mkLogRootDir();
            }
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (dir.exists()) {
                String name = DateUtils.getDateNowEN();
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
