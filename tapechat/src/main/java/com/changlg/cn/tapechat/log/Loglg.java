package com.changlg.cn.tapechat.log;

import android.text.TextUtils;

import java.io.File;

/**
 * core Log
 * Created by chang on 2016/4/7.
 */
public class Loglg {

    public static final String DEFAULT_MESSAGE = "execute";
    // system line separator(windows  "\n",UNIX  "/n")
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String NULL_TIPS = "Log with null object";// null hint
    public static final String PARAM = "Param";
    public static final String NULL = "null";
    public static final String TAG_DEFAULT = "Loglg";// default Tag

    public static final int JSON_INDENT = 4;// Json format indent

    // log level
    public static final int V = 0x1;// Verbose
    public static final int D = 0x2;// Debug
    public static final int I = 0x3;// Info
    public static final int W = 0x4;// Warn
    public static final int E = 0x5;// Error
    public static final int A = 0x6;// Assert
    public static final int JSON = 0x7;
    public static final int XML = 0x8;

    private static boolean IS_SHOW_LOG = true;
    private static final int STACK_TRACE_INDEX = 5;

    public static void init(boolean isShowLog) {
        IS_SHOW_LOG = isShowLog;
    }

    public static void v() {
        printLog(V, null, DEFAULT_MESSAGE);
    }

    public static void v(Object msg) {
        printLog(V, null, msg);
    }

    public static void v(String tag, Object... objects) {
        printLog(V, tag, objects);
    }


    public static void d() {
        printLog(D, null, DEFAULT_MESSAGE);
    }

    public static void d(Object msg) {
        printLog(D, null, msg);
    }

    public static void d(String tag, Object... objects) {
        printLog(D, tag, objects);
    }

    public static void i() {
        printLog(I, null, DEFAULT_MESSAGE);
    }

    public static void i(Object msg) {
        printLog(I, null, msg);
    }

    public static void i(String tag, Object... objects) {
        printLog(I, tag, objects);
    }

    public static void w() {
        printLog(W, null, DEFAULT_MESSAGE);
    }

    public static void w(Object msg) {
        printLog(W, null, msg);
    }

    public static void w(String tag, Object... objects) {
        printLog(W, tag, objects);
    }

    public static void e() {
        printLog(E, null, DEFAULT_MESSAGE);
    }

    public static void e(Object msg) {
        printLog(E, null, msg);
    }

    public static void e(String tag, Object... objects) {
        printLog(E, tag, objects);
    }

    public static void a() {
        printLog(A, null, DEFAULT_MESSAGE);
    }

    public static void a(Object msg) {
        printLog(A, null, msg);
    }

    public static void a(String tag, Object... objects) {
        printLog(A, tag, objects);
    }

    public static void json() {
        printLog(JSON, null, DEFAULT_MESSAGE);
    }

    public static void json(Object msg) {
        printLog(JSON, null, msg);
    }

    public static void json(String tag, Object... objects) {
        printLog(JSON, tag, objects);
    }

    public static void xml() {
        printLog(XML, null, DEFAULT_MESSAGE);
    }

    public static void xml(Object msg) {
        printLog(XML, null, msg);
    }

    public static void xml(String tag, Object... objects) {
        printLog(XML, tag, objects);
    }

    public static void file(File targetDirectory, Object msg) {
        printFile(null, targetDirectory, null, msg);
    }

    public static void file(String tag, File targetDirectory, Object msg) {
        printFile(tag, targetDirectory, null, msg);
    }

    public static void file(String tag, File targetDirectory, String fileName, Object msg) {
        printFile(tag, targetDirectory, fileName, msg);
    }

    private static void printLog(int type, String tagString, Object... objects) {
        if (!IS_SHOW_LOG)
            return;
        String[] contents = wrapContent(tagString, objects);
        String tag = contents[0];
        String msg = contents[1];
        String headString = contents[2];

        switch (type) {
            case V:
            case D:
            case I:
            case W:
            case E:
            case A:
                BaseLog.printDefault(type, tag, headString + msg);
                break;
            case JSON:
                JsonLog.printJson(tag, msg, headString);
                break;
            case XML:
                XmlLog.printXml(tag, headString, msg);
                break;
        }

    }

    private static void printFile(String tagString, File targetDirectory, String fileName, Object objectMsg) {
        if (!IS_SHOW_LOG)
            return;
        String[] contents = wrapContent(tagString, objectMsg);
        String tag = contents[0];
        String msg = contents[1];
        String headString = contents[2];
        FileLog.printFile(tag, targetDirectory, fileName, headString, msg);
    }

    /**
     * package log content
     *
     * @param tagString log tag
     * @param objects   log content
     * @return String array containing log full content
     */
    private static String[] wrapContent(String tagString, Object... objects) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement stackTraceElement = stackTrace[STACK_TRACE_INDEX];

        String className = stackTraceElement.getFileName();
        int lineNumber = stackTraceElement.getLineNumber();
        if (lineNumber < 0)
            lineNumber = 0;
        String methodName = stackTraceElement.getMethodName();
        // Capitalize the first letter of the function name and the subsequent content to be a full function name.
        String methodNameNovel = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
        // Tag does not specify a function name as tag
        String tag = tagString == null ? methodName : tagString;
        if (TextUtils.isEmpty(tag))
            tag = TAG_DEFAULT;

        String msg = objects == null ? NULL_TIPS : getObjectsString(objects);
        //In the window can be clicked to jump to the specified code line, is "()" played a role
        String headString = "[(" + className + ":" + lineNumber + ")#" + methodNameNovel + "]";

        return new String[]{tag, msg, headString};
    }

    /**
     * get log content String
     *
     * @param objects log content object array
     * @return log content String
     */
    private static String getObjectsString(Object... objects) {
        if (objects.length > 1) {
            StringBuilder sb = new StringBuilder();
            sb.append(LINE_SEPARATOR);
            for (int i = 0; i < objects.length; i++) {
                Object object = objects[i];
                if (object == null)
                    sb.append(PARAM).append("[").append(i).append("]").append(NULL)
                            .append(LINE_SEPARATOR);
                else
                    sb.append(PARAM).append("[").append(i).append("]").append(object.toString())
                            .append(LINE_SEPARATOR);
            }
            return sb.toString();
        } else {
            Object object = objects[0];
            return object == null ? NULL : object.toString();
        }
    }

}
