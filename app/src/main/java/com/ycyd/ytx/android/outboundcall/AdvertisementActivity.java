package com.ycyd.ytx.android.outboundcall;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.ycyd.ytx.android.util.LogUtils;
import com.ycyd.ytx.android.util.NetworkUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

import static java.lang.Thread.sleep;

public class AdvertisementActivity extends AppCompatActivity {
    private static final String TAG = "AdvertisementActivity";
    LogUtils.sign=LogUtils.VERBOSE;
    private TextView contentview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);
        contentview =(TextView)findViewById(R.id.id_fullscreen_content);
        //开始版本检查
        try{
            //获取packagemanager的实例
            PackageManager packageManager = getPackageManager();
            //getPackageName()是你当前类的包名，0代表是获取版本信息
            String packName=getPackageName();
            PackageInfo packInfo = packageManager.getPackageInfo(packName,PackageManager.GET_ACTIVITIES);
            final int curVersionCode=packInfo.versionCode;
            final String curAppName=packInfo.packageName;
            ApplicationInfo appInfo = packageManager.getApplicationInfo(packName,PackageManager.GET_META_DATA);
            String address = appInfo.metaData.getString("QueryVersionURL");
            //String address = "http://111.47.64.131:8091/app/QueryVersionJSON.jsp";
            Map<String, String> map = new HashMap<>();
            //map.put("name", curAppName);
            map.put("name", "Android.OutboundCall");
            NetworkUtil.sendOkHttpRequestPost(address, map,
                    new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                String resultJson = response.body().string();
                                final String strLastVersionCode=NetworkUtil.parseJSON(resultJson);
                                sleep(1000);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        contentview.setText(strLastVersionCode);
                                    }
                                });
                                if(Integer.parseInt(strLastVersionCode)>curVersionCode) {
                                    UpdateApp();
                                }
                                else{
                                    StartLogin();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                LogUtils.d(TAG+"----------->>NetworkUtil.parseJSON","解析版本号失败！");
                                finish();
                            }
                        }
                    });


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void UpdateApp() {
    }

    private void StartLogin() {
        //
        Intent intent = new Intent(this,AdvertisementActivity.class);
        startActivity(intent);
        //结束掉当前的activity
        this.finish();
    }

}
