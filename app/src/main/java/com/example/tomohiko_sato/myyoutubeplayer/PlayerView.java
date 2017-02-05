package com.example.tomohiko_sato.myyoutubeplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * プレイヤー用のビュー。
 * ２つのモード持っている
 * 1. ドラッグできる。
 * 2. ドラッグせずに関連動画を表示などもできる (fragment使うべきか？)
 */
public class PlayerView extends FrameLayout {
    private final WindowManager windowManager;
    private State currentState = State.FLOAT;

    enum State {
        FLOAT,
        EXPAND
    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        setOnTouchListener(new TouchEventTranslater((dx, dy) -> {
            Logger.d("dx:%d, dy:%d", dx, dy);
            if (currentState == State.FLOAT) {
                updateLayout(dx, dy);
            }
        }, () -> {
            Logger.d();
            currentState = currentState == State.EXPAND ? State.FLOAT : State.EXPAND;
        }));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    private void updateLayout(int dx, int dy) {
        if (!isAttachedToWindow()) {
            return;
        }
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) getLayoutParams();
        lp.x += dx;
        lp.y += dy;
        windowManager.updateViewLayout(this, lp);
    }

}
