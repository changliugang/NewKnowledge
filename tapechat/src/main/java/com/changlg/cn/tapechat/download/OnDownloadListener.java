package com.changlg.cn.tapechat.download;
/**
 * 
 * APK下载监听
 * @author 2015年9月2日 / changliugang
 */
public interface OnDownloadListener {
	/**
	 * 
	  * 开始下载
	  * @author  2015年9月2日 / changliugang
	 */
	public void onStartDownload();
	/**
	 * 
	  * 下载失败
	  * @author  2015年9月2日 / changliugang
	 */
	public void onDownloadFaid();
	/**
	 * 
	  * 下载成功
	  * @author  2015年9月2日 / changliugang
	 */
	public void onDownloadSuccessed();
	/**
	 * 
	  * 下载中
	  * @author  2015年9月2日 / changliugang
	 */
	public void onDownloadRunning();
	/**
	 * 
	  * 下载进度返回
	  * @param fileSize 下载文件总大小
	  * @param curSize 已下载文件大小
	  * @param percent 下载进度百分比，小数点后保留两位like 78.34%
	  * @author  2015年9月8日 / changliugang
	 */
	public void onUpdateProgress(int fileSize, int curSize, String percent);
}
