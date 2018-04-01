package org.noneorone.client;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.noneorone.server.IAdditionService;

public class MainActivity extends AppCompatActivity {

    TextView tvContent;
    IAdditionService mSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initService();
    }

    private void initView() {
        tvContent = findViewById(R.id.tv_content);
    }

    private void initService() {
        Intent intent = new Intent();
        // 由于是隐式启动Service 所以要添加对应的action，A和之前服务端的一样。
        // android 5.0以后直设置action不能启动相应的服务，需要设置packageName或者Component。
        intent.setAction("org.noneorone.server.aidl.ADDITION_SERVICE");
        intent.setPackage("org.noneorone.server");
        startService(intent);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 调用asInterface()方法获得IMyAidlInterface实例
            mSub = IAdditionService.Stub.asInterface(service);
            if(mSub != null){
                try {
                    int resultValue = mSub.add(1, 2);
                    tvContent.setText("Client>SERVER_RESULT: " +String.valueOf(resultValue));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 解绑服务
        unbindService(serviceConnection);
    }
}
