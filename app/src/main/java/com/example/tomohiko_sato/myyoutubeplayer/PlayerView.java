package com.example.tomohiko_sato.myyoutubeplayer;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
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
            setCurrentState(currentState == State.EXPAND ? State.FLOAT : State.EXPAND);
        }));
    }

    private void setCurrentState(State state) {
        currentState = state;
        switch (currentState) {
            case FLOAT:
                shrink();
                break;
            case EXPAND:
                fill();
                // 画面ぴったりにする
                break;
        }
    }

    private void shrink() {
        if (!isAttachedToWindow()) {
            return;
        }
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) getLayoutParams();
        lp.width = getResources().getDimensionPixelSize(R.dimen.player_float_width);
        lp.height = getResources().getDimensionPixelSize(R.dimen.player_float_height);
        windowManager.updateViewLayout(this, lp);
    }

    private void fill() {
        if (!isAttachedToWindow()) {
            return;
        }
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) getLayoutParams();
        lp.width = screenWidth;
        lp.height = screenHeight;
        windowManager.updateViewLayout(this, lp);
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
