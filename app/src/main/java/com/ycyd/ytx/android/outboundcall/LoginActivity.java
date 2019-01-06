package com.ycyd.ytx.android.outboundcall;

import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ycyd.ytx.android.util.LogUtil;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private Button btRand;
    private Button btLogin;
    private EditText etOperid;
    private EditText etRandid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btRand = (Button) findViewById(R.id.id_buttonGetRandID);
        btLogin = (Button) findViewById(R.id.id_buttonLogin);
        etOperid = (EditText) findViewById(R.id.id_editTextOperatorID);
        etRandid = (EditText) findViewById(R.id.id_editTextRandID);
        btRand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String operid = etOperid.getText().toString();
                if (null == operid || "".equals(operid)) {
                    AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this)
                            .setIcon(R.mipmap.ic_launcher)//设置标题的图片
                            .setTitle("提示")//设置对话框的标题
                            .setMessage("工号不能为空！")//设置对话框的内容
                            //设置对话框的按钮
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.setCancelable(false);
                    dialog.show();
                } else {
                    btRand.setEnabled(false);//禁止button点击
                    setTimer();
                }
            }
        });
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String operid = etOperid.getText().toString();
                String randid = etRandid.getText().toString();
                if (null == operid || "".equals(operid) ||
                        null == randid || "".equals(randid)) {
                    AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this)
                            .setIcon(R.mipmap.ic_launcher)//设置标题的图片
                            .setTitle("提示")//设置对话框的标题
                            .setMessage("工号或动态码不能为空！")//设置对话框的内容
                            //设置对话框的按钮
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.setCancelable(false);
                    dialog.show();
                } else {
                    btLogin.setEnabled(false);

                }
            }
        });
    }

    private static final int LEFT_TIME = 999;
    private static final int TIME_OVER = 998;

    private void setTimer() {
        LogUtil.d(TAG + "----------->>timer.start()", "计时开始！");

        long millisInFuture = 60 * 1000;
        long countDownInterval = 1000;
        timer = new CountDownTimer(millisInFuture, countDownInterval) {

            @Override
            public void onTick(long millisUntilFinished) {
                //millisUntilFinished  剩余时间回调，这个是实时的（以countDownInterval为单位）
                if (!isFinishing()) {
                    Message msg = Message.obtain();
                    msg.what = LEFT_TIME;
                    msg.obj = millisUntilFinished;
                    mHandler.sendMessage(msg);
                }
            }

            @Override
            public void onFinish() {
                //结束时的回调
                mHandler.sendEmptyMessage(TIME_OVER);
            }
        };
        timer.start();
    }

    private CountDownTimer timer;

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onDestroy() {
        stopTimer();
        super.onDestroy();
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LEFT_TIME:
                    long left_time = (long) msg.obj / 1000;
                    btRand.setText(String.valueOf(left_time) + "秒");
                    break;
                case TIME_OVER:
                    stopTimer();
                    btRand.setEnabled(true);
                    btRand.setText("取动态码");
                    break;
                default:
                    break;
            }
        }
    };

}
