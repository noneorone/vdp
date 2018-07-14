package com.noo.core.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * IO流处理<br/>
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2018/1/19 8:51<br/>
 * @since 1.0
 */
public class IOUtils {

    private IOUtils() {
    }

    /**
     * 将文本写入文件
     *
     * @param file    文件
     * @param content 文本内容
     * @param append  是否追加
     */
    public static void writeStringToFile(File file, String content, boolean append) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(file, append);
            bw = new BufferedWriter(fw);
            bw.write(content + "\n");
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                } finally {
                    bw = null;
                }
            }
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                } finally {
                    fw = null;
                }
            }
        }
    }

}
