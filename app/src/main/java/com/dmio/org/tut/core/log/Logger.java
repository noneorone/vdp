package com.dmio.org.tut.core.log;

import android.text.TextUtils;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

/**
 * 功能说明：日志处理<br/>
 * 作者：wangmeng on 2017/3/29 21:17<br/>
 * 邮箱：noneorone@yeah.net
 */

public class Logger {

    /**
     * 优先级
     */
    public enum Priority {
        v("verbose", Log.VERBOSE),
        d("debug", Log.DEBUG),
        i("info", Log.INFO),
        w("warn", Log.WARN),
        e("error", Log.ERROR),
        a("assert", Log.ASSERT);

        private int value;
        private String flag;

        public String getFlag() {
            return flag;
        }

        public int getValue() {
            return value;
        }

        Priority(String flag, int value) {
            this.flag = flag;
            this.value = value;
        }
    }

    /**
     * 获取调用此方法的上面的方法名和类
     *
     * @return 返回标签
     */
    public static String getTag() {
        StackTraceElement[] elements = (new Throwable()).getStackTrace();
        if (elements != null && elements.length > 0) {
            for (StackTraceElement ste : elements) {
                String className = ste.getClassName();
                if (!Thread.class.getName().equals(className)
                        && !Log.class.getName().equals(className)
                        && !Logger.class.getName().equals(className)) {

                    String methodName = ste.getMethodName();
                    StringBuffer buffer = new StringBuffer();
                    if (!TextUtils.isEmpty(className)) {
                        buffer.append(className);
                    }
                    if (!TextUtils.isEmpty(methodName)) {
                        buffer.append(".");
                        buffer.append(methodName);
                    }

                    if (buffer.length() > 0) {
                        return buffer.toString();
                    }
                }
            }
        }
        return Logger.class.getName();
    }

    public static void v(String msg) {
        record(Priority.v, msg);
    }

    public static void v(Throwable tr) {
        record(Priority.v, tr);
    }

    public static void d(String msg) {
        record(Priority.d, msg);
    }

    public static void d(Throwable tr) {
        record(Priority.d, tr);
    }

    public static void i(String msg) {
        record(Priority.i, msg);
    }

    public static void i(Throwable tr) {
        record(Priority.i, tr);
    }

    public static void w(String msg) {
        record(Priority.w, msg);
    }

    public static void w(Throwable tr) {
        record(Priority.w, tr);
    }

    public static void e(String msg) {
        record(Priority.e, msg);
    }

    public static void e(Throwable tr) {
        record(Priority.e, tr);
    }

    /**
     * 记录日志，包括显示和记录到文件
     *
     * @param priority 优先级
     * @param object   消息对象
     */
    public static void record(Priority priority, Object object) {
        record(priority, object, true);
    }

    /**
     * 记录日志，包括显示和记录到文件
     *
     * @param priority 优先级
     * @param object   消息对象
     * @param asyn     是否异步写入
     */
    public static void record(Priority priority, Object object, boolean asyn) {
        String msg = null;
        if (object instanceof String) {
            msg = (String) object;
        } else if (object instanceof Throwable) {
            msg = getStackTraceString((Throwable) object);
        }

        if (!TextUtils.isEmpty(msg)) {
            Log.println(priority.getValue(), getTag(), msg);
            //eg.03-30 10:00:00.000 756-861/? D/AlarmManager: set alarm to RTC 13982856
            String content = String.format("%s %s/%s %s",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss").format(System.currentTimeMillis()),
                    priority.getFlag(),
                    getTag(),
                    msg
            );
            if (asyn) {
                LoggerRecorder.getInstance().asynWriteToFile(priority.getFlag(), content);
            } else {
                LoggerRecorder.getInstance().writeToFile(priority.getFlag(), content);
            }
        }
    }

    /**
     * Handy function to get a loggable stack trace from a Throwable
     *
     * @param tr An exception to log
     */
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

}
