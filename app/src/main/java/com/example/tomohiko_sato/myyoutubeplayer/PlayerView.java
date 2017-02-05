package com.example.tomohiko_sato.myyoutubeplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
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
    private final DragDetector dragDetector;

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        dragDetector = new DragDetector(new DragDetector.OnMovedListener() {
            @Override
            public void onMoved(int dx, int dy) {
                Logger.d("dx:%d, dy:%d", dx, dy);
                updateLayout(dx, dy);
            }
        }, new DragDetector.OnClickedListener() {
            @Override
            public void onClicked() {
                Logger.d();
            }
        });
        setOnTouchListener(dragDetector);
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

    private static class DragDetector implements OnTouchListener {
        private final OnMovedListener moved;
        private final OnClickedListener clicked;

        private int downX = 0;
        private int downY = 0;
        private int oldX = 0;
        private int oldY = 0;

        DragDetector(OnMovedListener moved, OnClickedListener clicked) {
            this.moved = moved;
            this.clicked = clicked;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Logger.d(event.toString());
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    downX = (int) event.getRawX();
                    downY = (int) event.getRawY();
                    oldX = downX;
                    oldY = downY;
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    int distance = (int) Math.sqrt(((event.getRawX() - downX) * (event.getRawX() - downX) + (event.getRawY() - downY) * (event.getRawY() - downY)));
                    if (distance < 30) {
                        clicked.onClicked();
                    }
                    break;
                }
                default: {
                    int dx = (int) (event.getRawX() - oldX);
                    int dy = (int) (event.getRawY() - oldY);
                    oldX = (int) event.getRawX();
                    oldY = (int) event.getRawY();
                    moved.onMoved(dx, dy);
                }
            }

            return false;
        }

        interface OnMovedListener {
            void onMoved(int dx, int dy);
        }

        interface OnClickedListener {
            void onClicked();
        }
    }
}
