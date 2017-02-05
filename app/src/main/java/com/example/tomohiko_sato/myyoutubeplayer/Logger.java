package com.example.tomohiko_sato.myyoutubeplayer;


import android.util.Log;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.tomohiko_sato.myyoutubeplayer.Logger.TagGenerater.getTag;

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
        Log.d(getTag(), EMPTY);
    }

    public static void d(String str, Object... args) {
        Log.d(getTag(), String.format(str, args));
    }

    public static void i() {
        Log.i(getTag(), EMPTY);
    }

    public static void i(String str, Object... args) {
        Log.i(getTag(), String.format(str, args));
    }

    public static void w() {
        Log.w(getTag(), EMPTY);
    }

    public static void w(String str, Object... args) {
        Log.w(getTag(), String.format(str, args));
    }

    public static void e() {
        Log.e(getTag(), EMPTY);
    }

    public static void e(String str, Object... args) {
        Log.e(getTag(), String.format(str, args));
    }

    static class TagGenerater {
        private static final int CALL_STACK_INDEX = 2;
        private static final Pattern ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$");
        private static final int MAX_TAG_LENGTH = 23;

        /**
         * @return {CallerClassName}#{CallerMethodName}。ただし {@link #MAX_TAG_LENGTH}文字にトリミングされる。
         */
        static String getTag() {
            StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            if (stackTrace.length <= CALL_STACK_INDEX) {
                throw new IllegalStateException(
                        "Synthetic stacktrace didn't have enough elements: are you using proguard?");
            }
            StackTraceElement caller = stackTrace[CALL_STACK_INDEX];
            String tag = String.format(Locale.US, "%s#%s", getCallerClassName(caller), getCallerMethodName(caller));
            return tag.length() > MAX_TAG_LENGTH ? tag.substring(0, MAX_TAG_LENGTH) : tag;
        }

        private static String getCallerMethodName(StackTraceElement element) {
            return element.getMethodName();
        }

        /**
         * 匿名クラスの場合、$1といったsuffixは除去される。
         */
        private static String getCallerClassName(StackTraceElement element) {
            String callerClassName = element.getClassName();
            Matcher m = ANONYMOUS_CLASS.matcher(callerClassName);
            if (m.find()) {
                callerClassName = m.replaceAll("");
            }
            return callerClassName.substring(callerClassName.lastIndexOf('.') + 1);
        }
    }
}
