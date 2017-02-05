package com.example.tomohiko_sato.myyoutubeplayer;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ExternalPlayerService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.d();
        ExternalPlayerService.bindService(this, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                Logger.d();
                service = ((ExternalPlayerService.ExternalPlayerServiceBinder) binder).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        });
    }
}
