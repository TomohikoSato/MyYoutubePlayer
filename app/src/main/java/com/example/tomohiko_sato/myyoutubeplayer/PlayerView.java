package com.example.tomohiko_sato.myyoutubeplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * プレイヤー用のビュー。
 * ２つのモード持っている
 * 1. ドラッグできる。
 * 2. ドラッグせずに関連動画を表示などもできる (fragment使うべきか？)
 */
public class PlayerView extends FrameLayout {
    public PlayerView(Context context) {
        super(context);
    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

}
