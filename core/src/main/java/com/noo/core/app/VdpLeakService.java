package com.noo.core.app;

//import com.squareup.leakcanary.AnalysisResult;
//import com.squareup.leakcanary.DisplayLeakService;
//import com.squareup.leakcanary.HeapDump;

/**
 * DisplayLeakService扩展，可用于处理dump文件上传到服务器的业务
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/7/6 16:48<br/>
 * @since 1.0
 */
public class VdpLeakService /*extends DisplayLeakService*/ {

//    @Override
//    protected void afterDefaultHandling(final HeapDump heapDump, AnalysisResult result, String leakInfo) {
//        if (!result.leakFound || result.excludedLeak) {
//            return;
//        }
//
//        // TODO: 2017/7/6 handle dump file uploading to server etc. sample code as follows:
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Looper.prepareMainLooper();
//                Toast.makeText(getApplicationContext(), "leak::: " + heapDump.heapDumpFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
//                Looper.loop();
//            }
//        }).start();
//
//    }
}
