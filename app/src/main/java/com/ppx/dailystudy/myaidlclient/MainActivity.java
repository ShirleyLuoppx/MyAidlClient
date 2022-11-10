package com.ppx.dailystudy.myaidlclient;

import static android.content.Context.BIND_AUTO_CREATE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.ppx.dailystudy.ipctest.BookActivity;
import com.ppx.dailystudy.myaidlservice.IStudent;

/**
 * @author LuoXia
 */
public class MainActivity extends AppCompatActivity {

    private String TAG = "ppx_MainActivity";
    private IStudent iStudent;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.tv_name).setOnClickListener(v -> {
            Log.d(TAG, "onCreate: 11111");
            testAidl();
        });

        findViewById(R.id.tv_book).setOnClickListener(v -> {
            startActivity(new Intent(this, BookActivity.class));
        });
    }


    /**
     * aidl测试
     */
    private void testAidl() {
        context = App.getContext();

        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "onServiceConnected: -----------");
                iStudent = IStudent.Stub.asInterface(service);

                if (iStudent != null) {
                    try {
                        iStudent.setName("皮皮虾");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "onCreate: istudent is null --------------");
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "onServiceDisconnected: ---------");
            }
        };

        Intent TCS = new Intent();
        TCS.setAction("com.test.server");
        TCS.setPackage("com.ppx.dailystudy.myaidlservice");
        context.bindService(TCS, connection, BIND_AUTO_CREATE);
    }
}