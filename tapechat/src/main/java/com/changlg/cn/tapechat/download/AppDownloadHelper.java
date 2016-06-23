package com.changlg.cn.tapechat.download;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.NumberFormat;



/**
 * APP下载帮助类
 *
 * @author 2015年9月7日 / changliugang
 */
public class AppDownloadHelper {

    /**
     * 下载成功标志位
     */
    public static final int SUCCESSFUL = 0;
    /**
     * 下载失败标志位
     */
    public static final int FAIL = 1;
    /**
     * 下载中标志位
     */
    public static final int RUNNING = 2;
    /**
     * 下载开始标志位
     */
    public static final int START = 3;
    /**
     * 下载进度标志位， 此标志负责传递下载进度值
     */
    public static final int PROGRESS = 4;
    // 默认apk文件名
    private static final String DEFAULT_NAME = "default";
    private Context mContext;
    private DownloadManager mDownloadManager = null;
    // 下载信息变更观察器
    private DownloadChangeObserver mDownloadChangeObserver;
    // DownloadManager下载时在队列中赋予的ID，用于区别队列中多个下载任务的相关信息，如大小，时间，下载进度等
    private long lastDownloadId = 0;

    private boolean isRunning;
    //  外部赋值参数,目前就这俩。下载到SD卡的位置是系统的Download文件夹，就不要让人乱改了。
    private String mApkName;// apk文件名，无格式后缀
    private String mDownloadUrl;// apk下载地址

    private DownloadHandler mDownloadHandler;// 传递下载进度
    private static OnDownloadListener mListener;// 下载相关信息的监听器
    private int downloadStute;// 下载状态

    /**
     * 获取下载状态
     *
     * @return 当前下载状态
     */
    public int getDownloadStute() {
        return downloadStute;
    }

    /**
     * 获取DownloadManager,如果你还要配置DownloadManager的话，调用之
     *
     * @return {@link DownloadManager} 系统下载管理
     */
    public DownloadManager getDownloadManager() {
        return mDownloadManager;
    }

    /**
     * 获取下载是否正在进行，若正在下载中，当不允许再次点击下载
     *
     * @return 正在下载中 true；空闲状态 false
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * 开始下载
     *
     * @param context  当前上下文
     * @param listener 下载信息监听器 {@link OnDownloadListener}
     */
    public void start(Context context, OnDownloadListener listener) {
        if (listener == null)
            new RuntimeException("The OnDownloadListener is necessary");
        else
            mListener = listener;
        start(context, mDownloadUrl, null);
    }

    /**
     * static class DownloadHandler和外部class的联系，让内部类和外部类不产生任何联系就是static class的
     * 目的，防止GC时因为handle有引用导致，AppDownloadHelper，循环泄漏。不需要在AppDownloadHelper中通过
     * 申明大量的static变量来提供给handler使用（这样的代码不仅难看而且设计很不合理）。因为在DownloadHandler
     * 有一个mHelper的弱引用所以可以调用mHelper的任何public函数和变量。
     */
    private static class DownloadHandler extends Handler {
        // This Handler class should be static or leaks might occur 警告的处理
        private final WeakReference<AppDownloadHelper> mHelper;

        public DownloadHandler(AppDownloadHelper helper) {
            mHelper = new WeakReference<>(helper);
        }

        @Override
        public void handleMessage(Message msg) {
            AppDownloadHelper helper = mHelper.get();
            if (helper != null) {
                // 下载业务处理
                switch (msg.what) {
                    case AppDownloadHelper.START:
                        mListener.onStartDownload();
                        break;
                    case AppDownloadHelper.SUCCESSFUL:
                        mListener.onDownloadSuccessed();
                        break;
                    case AppDownloadHelper.FAIL:
                        mListener.onDownloadFaid();
                        break;
                    case AppDownloadHelper.RUNNING:
                        mListener.onDownloadRunning();
                        break;
                    case AppDownloadHelper.PROGRESS:
                        int a1 = msg.arg1;
                        int a2 = msg.arg2;
                        String o = (String) msg.obj;
                        mListener.onUpdateProgress(a2, a1, o);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 开始下载
     *
     * @param context     上下文对象
     * @param downloadUrl 文件下载地址
     * @param apkName     文件名
     */
    private void start(Context context, String downloadUrl, String apkName) {
        mContext = context;

        if (TextUtils.isEmpty(downloadUrl)) {
            new RuntimeException("The URI to be downloaded is null");
        }

        if (!TextUtils.isEmpty(apkName)) {
            mApkName = apkName;
        }

        mDownloadHandler = new DownloadHandler(this);

        // 实例化系统下载服务
        mDownloadManager = (DownloadManager) context
                .getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(downloadUrl);
        Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS).mkdir();
        Request request = new Request(uri);
        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(
                Request.NETWORK_MOBILE | Request.NETWORK_WIFI)
                // 不允许漫游
                .setAllowedOverRoaming(false)
                        // 设置下载后文件存放的位置sdcard的目录下的download文件夹
                .setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS, mApkName + ".apk");
        mDownloadHandler.sendEmptyMessage(START);
        // 加入下载队列，开始下载
        lastDownloadId = mDownloadManager.enqueue(request);
        // 注册下载完成广播监听
        context.registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        mDownloadChangeObserver = new DownloadChangeObserver(null);
        // 注册下载观察者监听， DownloadManager下载文件的通用标识符，根据这个来启用观察器，监视下载变动，来获取下载进度
        context.getContentResolver().registerContentObserver(
                Uri.parse("content://downloads/my_downloads"), true,
                mDownloadChangeObserver);
    }

    private void queryDownloadInfo() {

        Query query = new Query();
        query.setFilterById(lastDownloadId);
        // 查询下载相关信息，子线程中
        Cursor cursor = mDownloadManager.query(query);
        if (cursor != null && cursor.moveToFirst()) {
            // 状态
            int status = cursor.getInt(cursor
                    .getColumnIndex(DownloadManager.COLUMN_STATUS));
            // 文件总长
            int fileSizeIndex = cursor
                    .getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
            // 目前为止下载了多少
            int curSizeIndex = cursor
                    .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
            int fileSize = cursor.getInt(fileSizeIndex);
            int curSize = cursor.getInt(curSizeIndex);

//             int reasonIndex =  cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
//            int reason = cursor.getInt(reasonIndex);

            Message message = mDownloadHandler.obtainMessage();
            downloadStute = status;
            switch (status) {
                case DownloadManager.STATUS_FAILED:
//                    Log.d("chang","STATUS_FAILED reason code:"+reason);
                    mDownloadHandler.sendEmptyMessage(FAIL);
                    break;
                case DownloadManager.STATUS_PAUSED:
//                    Log.d("chang","STATUS_PAUSED reason code:"+reason);
                    Log.d("AppDownloadHelper", "PAUSED");
                    break;
                case DownloadManager.STATUS_PENDING:
                    // 尝试重启
                    break;
                case DownloadManager.STATUS_RUNNING:
                    message.what = PROGRESS;
                    message.arg1 = curSize < 0 ? 0 : curSize;
                    message.arg2 = fileSize < 0 ? 0 : fileSize;
                    message.obj = percentFormat(curSize, fileSize);
                    mDownloadHandler.sendEmptyMessage(RUNNING);
                    mDownloadHandler.sendMessage(message);
                    isRunning = true;
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    message.what = PROGRESS;
                    message.arg1 = curSize;
                    message.arg2 = fileSize;
                    message.obj = percentFormat(curSize, fileSize);
                    mDownloadHandler.sendEmptyMessage(SUCCESSFUL);
                    mDownloadHandler.sendMessage(message);
                    break;
                default:
                    break;
            }
        }

    }

    // ContentObserver内容观察者，目的是观察(捕捉)特定Uri引起的数据库的变化，继而做一些相应的处理

    /**
     * 下载进度变化观察器
     *
     * @author 2015年9月7日 / changliugang
     */
    class DownloadChangeObserver extends ContentObserver {

        public DownloadChangeObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            // 观察到下载Uri数据库变化后查询
            queryDownloadInfo();
        }

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 这里可以取得下载的id，这样就可以知道哪个文件下载完成了。适用与多个下载任务的监听
            // Log.v("tag",
            // ""+intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0));
//            queryDownloadInfo();
            // 下载文件夹路径
            String path = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + File.separator + mApkName + ".apk";

            openFile(path);
        }
    };

    /**
     * 关闭监听,下载完毕关闭监听以释放资源
     */
    public void close() {
        if (mContext != null) {
            if (receiver != null) {
                mContext.unregisterReceiver(receiver);
            }
            if (mDownloadChangeObserver != null) {
                mContext.getContentResolver().unregisterContentObserver(
                        mDownloadChangeObserver);
            }
        }
    }

    /**
     * 将下载进度百分比化
     *
     * @param cursize  已下载字节数
     * @param filesize 总字节数
     * @return 百分比化的下载进度
     */
    public String percentFormat(int cursize, int filesize) {
        double temp = (double) cursize / (double) filesize;
        BigDecimal b = new BigDecimal(temp);
        NumberFormat percent = NumberFormat.getPercentInstance(); // 建立百分比格式化引用
        percent.setMaximumFractionDigits(2); // 百分比小数点最多3位
        String result = percent.format(b);
        return TextUtils.isEmpty(result) ? "" : result;
    }

    /**
     * 安装APK程序代码
     *
     * @param path 安装包路径
     */
    private void openFile(String path) {
        File file = new File(path);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        mContext.startActivity(intent);
        close();
    }

    /**
     * 添加一个静态的Builder类来构建参数，并提升可读性
     */
    public static class Builder {
        private String apkName;// 文件名

        private String downloadUrl;// 文件下载地址

        public Builder apkName(String apkName) {
            this.apkName = apkName;
            return this;
        }

        public Builder downloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
            return this;
        }

        /**
         * 创建AppDownloadHelper下载帮助类
         *
         * @return AppDownloadHelper下载帮助类 {@link AppDownloadHelper}
         */
        public AppDownloadHelper build() {
            return new AppDownloadHelper(this);
        }

    }

    /**
     * AppDownloadHelper构造函数
     *
     * @param b AppDownloadHelper的Builder类 {@link Builder}
     */
    private AppDownloadHelper(Builder b) {
        // 没有命名下载的apk名字，就默认为DEFAULT_NAME
        if (b.apkName != null)
            mApkName = b.apkName;
        else
            mApkName = DEFAULT_NAME;
        mDownloadUrl = b.downloadUrl;
    }

}
