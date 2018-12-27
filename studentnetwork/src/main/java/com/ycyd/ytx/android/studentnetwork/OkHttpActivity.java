package com.ycyd.ytx.android.studentnetwork;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpActivity extends AppCompatActivity {
    TextView responseTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_urlconnection);
        Button sendRequest=(Button) findViewById(R.id.id_send);
        responseTextView=(TextView) findViewById(R.id.id_response);
        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responseTextView.setText("");
                new Thread(new Runnable() {
                    class Ver {
                        protected int versionCode;
                        public int getVersionCode(){
                            return this.versionCode;
                        }
                        public void setVersionCode(int versionCode){
                            this.versionCode=versionCode;
                        }
                    }

                    @Override
                    public void run() {
                        try{
                            OkHttpClient client=new OkHttpClient();
                            Request request=new Request.Builder()
                                    //.url("http://www.baidu.com")
                                    .url("http://111.47.64.131:8091/app/QueryVersionJSON.jsp?name=Android.outboundcall")
                                    .build();
                            Response response=client.newCall(request).execute();
                            String reader=response.body().string();
                            //showResponse(reader);
                            parseJSONWithJSONObject(reader);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    private void parseJSONWithJSONObject(String reader) {
                        try{
                            //JSONObject jsonObject=new JSONObject(reader);
                            //final String ver=String.valueOf(jsonObject.getInt("versionCode"));
                             Gson gson=new Gson();
                            final Ver ver=gson.fromJson(reader,Ver.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    responseTextView.setText(String.valueOf(ver.getVersionCode()));
                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    private void showResponse(final String s) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                responseTextView.setText(s);
                            }
                        });
                    }
                }).start();
            }
        });
    }
}
