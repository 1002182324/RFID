package com.it.RFID.tools;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.pgyersdk.update.DownloadFileListener;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.pgyersdk.update.javabean.AppBean;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

public class UpdataUtil {
    //检查更新
    public static void checkUpdate(Context context) {
        final Context mcontext = context;

        new PgyUpdateManager.Builder()
                .setForced(true)                //设置是否强制更新,非自定义回调更新接口此方法有用
                .setUserCanRetry(false)         //失败后是否提示重新下载，非自定义下载 apk 回调此方法有用
                .setDeleteHistroyApk(false)     // 检查更新前是否删除本地历史 Apk
                .setUpdateManagerListener(new UpdateManagerListener() {
                    @Override
                    public void onNoUpdateAvailable() {
                        //没有更新是回调此方法
                        Log.d("pgyer", "there is no new version");
                    }

                    @Override
                    public void onUpdateAvailable(AppBean appBean) {
                        final String DOWNLOD_URI = appBean.getDownloadURL();
                        //有更新是回调此方法
                        Log.d("pgyer", "there is new version can update"
                                + "new versionCode is " + appBean.getVersionCode());
                        new QMUIDialog.MessageDialogBuilder(mcontext)
                                .setTitle("检测到新版本")
                                .setMessage(appBean.getReleaseNote())
                                .addAction("更新", new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        PgyUpdateManager.downLoadApk(DOWNLOD_URI);
                                        dialog.dismiss();
                                    }
                                })
                                .addAction("取消", new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                    }
                                })
                                .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show();
                        //调用以下方法，DownloadFileListener 才有效；如果完全使用自己的下载方法，不需要设置DownloadFileListener


                    }

                    @Override
                    public void checkUpdateFailed(Exception e) {
                        //更新检测失败回调
                        Log.e("pgyer", "check update failed ", e);
                        ToastUtil.makeLToast("更新失败");

                    }
                })
                //注意 ：下载方法调用 PgyUpdateManager.downLoadApk(appBean.getDownloadURL()); 此回调才有效
                .setDownloadFileListener(new DownloadFileListener() {
                    private Integer integer;

                    @Override
                    public void downloadFailed() {
                        //下载失败
                        Log.e("pgyer", "download apk failed");
                    }

                    @Override
                    public void downloadSuccessful(Uri uri) {
                        Log.e("pgyer", uri + "-----download apk Successful");
                        PgyUpdateManager.installApk(uri);  // 使用蒲公英提供的安装方法提示用户 安装apk

                    }

                    @Override
                    public void onProgressUpdate(Integer... integers) {
                        integer = integers[0];
                        Log.e("pgyer", "update download apk progress : " + integer);
                    }
                })
                .register();
    }


}
