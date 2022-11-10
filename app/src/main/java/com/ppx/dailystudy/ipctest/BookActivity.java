package com.ppx.dailystudy.ipctest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ppx.dailystudy.myaidlclient.R;

import java.util.List;

/**
 * @Author: LuoXia
 * @Date: 2022/11/6 11:51
 * @Description:
 */
public class BookActivity extends AppCompatActivity {

    private String TAG = "BookActivity";
    private boolean isBound = false;
    private IBookManager iBookManager;
    private TextView tvBooks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        tvBooks = findViewById(R.id.tv_books);
        Log.d(TAG, "onCreate: ");
        attemptBindService();

        findViewById(R.id.btn_add_book).setOnClickListener(v -> {
            try {
                addBook(new Book(4, "《第一行代码》"));
                try {
                    showBooks();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });


//        try {
//            showBooks();
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
    }

    private void showBooks() throws RemoteException {
        if (isBound) {
            List<Book> list = iBookManager.getBooks();
            if (list != null) {
                StringBuilder stringBuilder = new StringBuilder();
                for (Book book : list) {
                    stringBuilder.append(book.getBookId())
                            .append("：")
                            .append(book.getBookName())
                            .append("\n");
                }
                tvBooks.setText(stringBuilder);
            }
        }
    }

    private void attemptBindService() {
        Intent intent = new Intent();
        intent.setAction("com.ppx.ipctest");
        intent.setPackage("com.ppx.dailystudy");
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: ");
            iBookManager = IBookManager.Stub.asInterface(service);
            try {
                List<Book> list = iBookManager.getBooks();
                Log.d(TAG, "onServiceConnected: size = " + list.size());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: ");
            isBound = false;
        }
    };

    private void addBook(Book book) throws RemoteException {
        if (!isBound) {
            attemptBindService();
            Log.d(TAG, "addBook: 服务未连接，尝试连接中，请稍后重试");
            return;
        }
        if (iBookManager != null && book != null) {
            iBookManager.addBook(book);
        }
        Log.d(TAG, "addBook: size = " + (iBookManager.getBooks().size()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        unbindService(connection);
        isBound = false;
    }
}
