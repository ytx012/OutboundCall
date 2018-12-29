package com.ycyd.ytx.android.studentnetwork;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.ycyd.ytx.android.util.*;

import static java.lang.Thread.sleep;

public class HttpURLConnectionActivity extends AppCompatActivity {
    TextView responseTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_urlconnection);
        final Button sendRequest=(Button) findViewById(R.id.id_send);
        responseTextView=(TextView) findViewById(R.id.id_response);
        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responseTextView.setText("");
                sendRequestWithHttpURLConnec();
            }

            private void sendRequestWithHttpURLConnec() {
                String address="http://10.28.4.176:8091/app/test.txt";
                NetworkUtil.sendHttpRequestGet(address,
                        new HttpCallbackListener(){
                            @Override
                            public void onFinish(String response) {
                                final String string=response;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        responseTextView.setText(string);
                                    }
                                });
                            }
                            @Override
                            public void onError(Exception e) {
                                e.printStackTrace();
                            }
                        });

                address="http://10.28.4.176:8091/app/QueryVersion.jsp";
                NetworkUtil.sendHttpRequestpost(address,"name=Android.OutboundCall",
                        new HttpCallbackListener(){
                            @Override
                            public void onFinish(String response) {
                                final String string=response;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            sleep(3000);
                                        }catch (Exception e){

                                        }
                                        responseTextView.setText(string);
                                    }
                                });
                            }
                            @Override
                            public void onError(Exception e) {
                                e.printStackTrace();
                            }
                        });



            }
        });
    }
}
