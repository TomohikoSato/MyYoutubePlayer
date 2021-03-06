package com.example.tomohiko_sato.myyoutubeplayer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.WindowManager;

public class ExternalPlayerService extends Service {
    private final WindowManager.LayoutParams playerViewParams = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,    // アプリケーションのTOPに配置
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |  // フォーカスを当てない(下の画面の操作がd系なくなるため)
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, // モーダル以外のタッチを背後のウィンドウへ送信
            PixelFormat.TRANSLUCENT);  // viewを透明にする
    private final ExternalPlayerServiceBinder binder = new ExternalPlayerServiceBinder();
    private WindowManager windowManager;
    private PlayerView playerView;

    public static void bind(Context context, ServiceConnection conn) {
        Intent intent = new Intent(context, ExternalPlayerService.class);
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    public static void unbind(Context context, ServiceConnection conn) {
        context.unbindService(conn);
    }


    public static void startService(Context context) {
        Intent intent = new Intent(context, ExternalPlayerService.class);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.i("startId:%d, intent:%s ", startId, intent);
        return START_STICKY;
    }

    public class ExternalPlayerServiceBinder extends Binder {
        ExternalPlayerService getService() {
            return ExternalPlayerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Logger.d(intent.toString());
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        this.setTheme(R.style.AppTheme);
        playerView = (PlayerView) LayoutInflater.from(this).inflate(R.layout.view_player, null);
        windowManager.addView(playerView, playerViewParams);
        new Handler().postDelayed(() -> {
            Logger.d("delay time has come. removeView");
            //windowManager.removeView(playerView);
            stopSelf();
        }, 30 * 1000);

        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.d();
        playerView.release();
        windowManager.removeView(playerView);
        playerView = null;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Logger.i();
    }
}
