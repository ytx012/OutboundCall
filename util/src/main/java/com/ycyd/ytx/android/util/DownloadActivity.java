package com.ycyd.ytx.android.util;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DownloadActivity extends AppCompatActivity
{
    private DownloadService.DownloadBinder downloadBinder;
    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder=(DownloadService.DownloadBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        Button st=(Button)findViewById(R.id.buttonstart);
        Button pa=(Button)findViewById(R.id.buttonpasue);
        Button ca=(Button)findViewById(R.id.buttoncancel);
        st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(downloadBinder==null) return;
                String url="https://raw.githubusercontent.com/guolindev/eclipse/master/eclipse-inst-win64.exe";
                downloadBinder.startDownload(url);
            }
        });

        pa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(downloadBinder==null) return;
                downloadBinder.pauseDownload();
            }
        });

        ca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(downloadBinder==null) return;
                downloadBinder.CancelDownload();
            }
        });

        Intent in=new Intent(this,DownloadService.class);
        //startService(in);
        bindService(in,connection,BIND_AUTO_CREATE);
        if(ContextCompat.checkSelfPermission(DownloadActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(DownloadActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]!=PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"无权限！",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }
}
