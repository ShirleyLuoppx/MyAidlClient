package com.ppx.dailystudy.myaidlclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ppx.dailystudy.myaidlservice.IStudent;

/**
 * @author LuoXia
 */
public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private IStudent iStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.tv_name);

        Intent intent = new Intent();
        intent.setAction("com.test.server");
        intent.setPackage("com.ppx.dailystudy.myaidlservice");
        bindService(intent, connection, BIND_AUTO_CREATE);

        textView.setOnClickListener(v -> {
            if (iStudent != null) {
                try {
                    iStudent.setName("可以啦！！！！！！");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, "onCreate: istudent is null --------------");
            }
        });
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iStudent = IStudent.Stub.asInterface(service);
            Toast.makeText(getApplicationContext(), "连接成功", Toast.LENGTH_LONG).show();
            Log.d(TAG, "onServiceConnected: 连接成功   ComponentName:"+name+"--，pck:"+name.getPackageName());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            unbindService(connection);
            Log.d(TAG, "onServiceDisconnected: 连接断开");
        }
    };
}