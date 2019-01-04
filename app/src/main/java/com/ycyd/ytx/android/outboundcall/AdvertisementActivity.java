package com.ycyd.ytx.android.outboundcall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ycyd.ytx.android.util.LogUtil;
import com.ycyd.ytx.android.util.NetworkUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static java.lang.Thread.sleep;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;


public class AdvertisementActivity extends AppCompatActivity
        implements Callback {
    private static final String TAG = "AdvertisementActivity";
    private TextView contentview;
    private final int DOWN_ERROR = 4;
    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;
    private final int MY_PERMISSIONS_REQUEST_REQUEST_INSTALL_PACKAGES = 1;
    private int curVersionCode;
    private String curAppName;
    private String downloadUrl;
    private PackageManager packageManager;
    /*
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case DOWN_ERROR:
                    //下载apk失败
                    Toast.makeText(getApplicationContext(), "下载新版本失败", 1).show();
                    break;
            }
        }
    };
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);
        contentview = (TextView) findViewById(R.id.id_fullscreen_content);
        LogUtil.displayLevel = LogUtil.VERBOSE;
        //开始版本检查
        try {
            //获取packagemanager的实例
            packageManager = getPackageManager();
            //getPackageName()是你当前类的包名，0代表是获取版本信息
            String packName = getPackageName();
            PackageInfo packInfo = packageManager.getPackageInfo(packName, PackageManager.GET_ACTIVITIES);
            curVersionCode = packInfo.versionCode;
            curAppName = packInfo.packageName;
            ApplicationInfo appInfo = packageManager.getApplicationInfo(packName, PackageManager.GET_META_DATA);
            String address = appInfo.metaData.getString("QueryVersionURL");
            //String address = "http://111.47.64.131:8091/app/QueryVersionJSON.jsp";
            Map<String, String> map = new HashMap<>();
            map.put("name", curAppName);
            //map.put("name", "Android.OutboundCall");
            NetworkUtil.sendOkHttpRequestPost(address, map, this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(Call call, IOException e) {
        e.printStackTrace();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String resultJson = "";
        Map<String, String> map;
        map = new HashMap<>();
        String strLastVersionCode = "";
        try {
            resultJson = response.body().string();
            LogUtil.d(TAG + "----------->>NetworkUtil.sendOkHttpRequestPost->resultJson:", resultJson);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d(TAG + "----------->>NetworkUtil.sendOkHttpRequestPost", "获取版本号失败！");
            finish();
        }
        try {
            Gson gson = new Gson();

            map = gson.fromJson(resultJson, Map.class);
            strLastVersionCode = map.get("versionCode");
            downloadUrl = map.get("downloadUrl");
            LogUtil.d(TAG + "----------->>Gson->strLastVersionCode:", strLastVersionCode);
            LogUtil.d(TAG + "----------->>Gson->downloadUrl:", downloadUrl);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d(TAG + "----------->>Gson", "解析版本号失败！");
            finish();
        }

        if (Integer.parseInt(strLastVersionCode) > curVersionCode) {
            LogUtil.d(TAG + "----------->>老版本，需要更新", "老版本，需要更新！");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    UpdateApp();
                }
            } else {
                UpdateApp();
            }
        } else {
            LogUtil.d(TAG + "----------->>新版本，不需要更新", "新版本，不需要更新！");
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File file = new File(Environment.getExternalStorageDirectory(), "tmp.apk");
                if (file.exists()) {
                    file.delete();
                    LogUtil.d(TAG + "----------->>delete app file", "存在安装文件，删除APP文件！");
                }
            }
            //登录
            StartLogin();
        }
    }

    private void UpdateApp() {
        contentview.setText("更新APP！");
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            final File file = new File(Environment.getExternalStorageDirectory(), "tmp.apk");
            final ProgressDialog pd;    //进度条对话框
            final String address;
            address = downloadUrl;
            if (file.exists()) {
                PackageInfo filePkinfo = packageManager.getPackageArchiveInfo(file.getPath(), PackageManager.GET_ACTIVITIES);
                if (curAppName.equals(filePkinfo.packageName)
                        && curVersionCode < filePkinfo.versionCode) {
                    //存在且版本高于当前
                    LogUtil.d(TAG + "----------->>new app file exists", "新版APP文件已存在，直接安装！");
                    installApk(file);
                    return;
                } else {
                    file.delete();
                    //Looper.prepare();
                    pd = new ProgressDialog(this);
                    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    pd.setMessage("正在下载更新");
                    pd.show();
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                //String serviceString = DOWNLOAD_SERVICE;
                                //DownloadManager downloadManager;
                                //downloadManager = (DownloadManager)getSystemService(serviceString);
                                sleep(1000);
                                getFileFromServer(file, address, pd);
                                sleep(1000);
                                pd.dismiss(); //结束掉进度条对话框
                                installApk(file);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            } else {
                pd = new ProgressDialog(this);
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.setMessage("正在下载更新");
                pd.show();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            //String serviceString = DOWNLOAD_SERVICE;
                            //DownloadManager downloadManager;
                            //downloadManager = (DownloadManager)getSystemService(serviceString);
                            sleep(1000);
                            getFileFromServer(file, address, pd);
                            sleep(1000);
                            pd.dismiss(); //结束掉进度条对话框
                            installApk(file);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }

        } else {
            Toast.makeText(this, "无SD卡，退出APP!", Toast.LENGTH_LONG).show();
            this.finish();
        }
        //this.finish();
    }


    private void getFileFromServer(File file, String path, ProgressDialog pd) throws Exception {
        LogUtil.d(TAG + "----------->>download app start", "开始下载app！");
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.connect();
        int code = conn.getResponseCode();
        //获取到文件的大小
        pd.setMax(conn.getContentLength());
        InputStream is = conn.getInputStream();
        FileOutputStream fos = new FileOutputStream(file);
        BufferedInputStream bis = new BufferedInputStream(is);
        byte[] buffer = new byte[1024];
        int len;
        int total = 0;
        while ((len = bis.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
            total += len;
            //获取当前下载量
            pd.setProgress(total);
        }
        fos.close();
        bis.close();
        is.close();
        LogUtil.d(TAG + "----------->>download app end", "下载app结束！");
    }

    protected void installApk(File file) {
        LogUtil.d(TAG + "----------->>install app", "开始安装app！");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //此时已在主线程中，可以更新UI了
                contentview.setText("请更新APP！");
            }
        });

        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        Uri appUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            appUri = FileProvider.getUriForFile(this, "com.ycyd.ytx.android.outboundcall.fileProvider", file);
            LogUtil.d(TAG + "----------->>appUri:", appUri.toString());

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //来对目标应用临时授权该Uri所代表的文件
            //FLAG_GRANT_READ_URI_PERMISSION | FLAG_ACTIVITY_NEW_TASK
            //执行的数据类型
            intent.setDataAndType(appUri, "application/vnd.android.package-archive");
        } else {
            appUri = Uri.fromFile(file);
            LogUtil.d(TAG + "----------->>appUri:", appUri.toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //执行的数据类型
            intent.setDataAndType(appUri, "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                UpdateApp();
            } else {
                Toast.makeText(this, "权限已拒绝!", Toast.LENGTH_LONG).show();
                this.finish();
            }
        }
    }

    private void StartLogin() {
        LogUtil.d(TAG + "----------->>StartLogin", "转入登录！");
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        //结束掉当前的activity
        this.finish();
    }
}
