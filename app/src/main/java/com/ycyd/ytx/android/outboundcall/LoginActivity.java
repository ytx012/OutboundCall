package com.ycyd.ytx.android.outboundcall;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btRand= (Button)findViewById(R.id.id_buttonGetRandID);
        Button btLogin= (Button)findViewById(R.id.id_buttonLogin);
        final EditText etOperid=(EditText)findViewById(R.id.id_editTextOperatorID);
        final EditText etRandid=(EditText)findViewById(R.id.id_editTextRandID);
        btRand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String operid=etOperid.getText().toString();
                if(null==operid||"".equals(operid))
                {
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
                }else{

                }

            }
        });
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String operid=etOperid.getText().toString();
                String randid=etRandid.getText().toString();
                if(null==operid||"".equals(operid)||
                        null==randid||"".equals(randid))
                {
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
                }else{

                }

            }
        });

    }
}
