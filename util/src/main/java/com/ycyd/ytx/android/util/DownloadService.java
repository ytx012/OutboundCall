package com.ycyd.ytx.android.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.File;

public class DownloadService extends Service {
    private DownloadTask task;
    private String downloadUrl;
    private DownloadListener listener=new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1,getNotification("下载...",progress));
        }

        @Override
        public void onSuccess() {
            task=null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("下载成功！",-1));
            Toast.makeText(DownloadService.this,"下载成功！",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            task=null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("下载失败！",-1));
            Toast.makeText(DownloadService.this,"下载失败！",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onPaused() {
            task=null;
            Toast.makeText(DownloadService.this,"暂停！",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCanceled() {
            task=null;
            stopForeground(true);
            Toast.makeText(DownloadService.this,"放弃！",Toast.LENGTH_SHORT).show();

        }
    };

    private Notification getNotification(String title, int progress) {
        Intent intent=new Intent(this,DownloadActivity.class);
        PendingIntent pi=PendingIntent.getActivities(this,0,new Intent[]{intent},0);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        if(progress>0){
            builder.setContentText(progress+"%");
            builder.setProgress(100,progress,false);
        }
        return builder.build();

    }

    private NotificationManager getNotificationManager(){
        return  (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }

    private  DownloadBinder mBinder=new DownloadBinder();

    class DownloadBinder extends Binder{
        public void startDownload(String url){
            if(task==null){
                downloadUrl=url;
                task=new DownloadTask(listener);
                task.execute(downloadUrl);
                startForeground(1,getNotification("下载",0));
                Toast.makeText(DownloadService.this,"下载中",Toast.LENGTH_SHORT).show();
            }
        }

        public void pauseDownload(){
            if(task!=null){
                task.pauseDownload();
            }
        }

        public void CancelDownload(){
            if(task!=null) {
                task.CancelDownload();
            }else {
                if(downloadUrl!=null) {
                    String fileNmae=downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                    String directory= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    File file=new File(directory+fileNmae);
                    if(file.exists()){
                        file.delete();
                    }
                    getNotificationManager().cancel(1);
                    stopForeground(true);
                    Toast.makeText(DownloadService.this,"下载取消",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return mBinder;
    }
}
