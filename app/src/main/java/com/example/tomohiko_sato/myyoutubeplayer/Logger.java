package com.example.tomohiko_sato.myyoutubeplayer;


import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.tomohiko_sato.myyoutubeplayer.Logger.TagGenerater.getCallerClassName;
import static com.example.tomohiko_sato.myyoutubeplayer.Logger.TagGenerater.getCallerMethodName;
import static com.example.tomohiko_sato.myyoutubeplayer.Logger.TagGenerater.trim;

/**
 * TODO: releaseのときProguardがかかるので、ログ情報をサーバーに送るような使い方ができなくなる気がする。調査し改善案を考える。
 * TODO: Tagはクラス名のみにして、Logの最初にメソッド名をつける
 */
public class Logger {
    /**
     * "" だとロギングされないので半角スペースを一つ入れる
     */
    private final static String EMPTY = " ";

    public static void d() {
        d("");
    }

    public static void d(String str, Object... args) {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        Log.d(trim(getCallerClassName(stackTrace)), String.format("%s: %s", getCallerMethodName(stackTrace), String.format(str, args)));
    }

    public static void i() {
        i("");
    }

    public static void i(String str, Object... args) {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        Log.i(trim(getCallerClassName(stackTrace)), String.format("%s: %s", getCallerMethodName(stackTrace), String.format(str, args)));
    }

    public static void e() {
        e("");
    }

    public static void e(String str, Object... args) {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        Log.e(trim(getCallerClassName(stackTrace)), String.format("%s: %s", getCallerMethodName(stackTrace), String.format(str, args)));
    }


    static class TagGenerater {
        private static final int CALL_STACK_INDEX = 2;
        private static final Pattern ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$");
        private static final int MAX_TAG_LENGTH = 23;

        /**
         * {@link #MAX_TAG_LENGTH} 以下にトリミングされる。
         */
        static String trim(String tag) {
            return tag.length() > MAX_TAG_LENGTH ? tag.substring(0, MAX_TAG_LENGTH) : tag;
        }

        /**
         * 匿名クラスの場合、$1といったsuffixは除去される。
         */
        static String getCallerClassName(StackTraceElement[] stackTrace) {
            if (stackTrace.length <= CALL_STACK_INDEX) {
                throw new IllegalStateException(
                        "Synthetic stacktrace didn't have enough elements: are you using proguard?");
            }

            String callerClassName = stackTrace[CALL_STACK_INDEX].getClassName();
            Matcher m = ANONYMOUS_CLASS.matcher(callerClassName);
            if (m.find()) {
                callerClassName = m.replaceAll("");
            }
            return callerClassName.substring(callerClassName.lastIndexOf('.') + 1);
        }

        static String getCallerMethodName(StackTraceElement[] stackTrace) {
            if (stackTrace.length <= CALL_STACK_INDEX) {
                throw new IllegalStateException(
                        "Synthetic stacktrace didn't have enough elements: are you using proguard?");
            }

            return stackTrace[CALL_STACK_INDEX].getMethodName();
        }
    }
}
