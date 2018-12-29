package com.ycyd.ytx.android.studentnetwork;

import android.media.session.MediaSession;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycyd.ytx.android.util.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.Attributes;

import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.spec.ECField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.xml.sax.InputSource;

import static java.lang.Thread.sleep;

public class OkHttpActivity extends AppCompatActivity {
    TextView responseTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_urlconnection);
        final Button sendRequest = (Button) findViewById(R.id.id_send);
        responseTextView = (TextView) findViewById(R.id.id_response);
        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responseTextView.setText("");
                String address = "http://111.47.64.131:8091/app/test.txt";
                NetworkUtil.sendOkHttpRequestGet(address, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String string = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                responseTextView.setText(string);
                            }
                        });
                    }
                });
                address = "http://10.28.4.176:8091/app/QueryVersionJSON.jsp";
                Map<String, String> map = new HashMap<>();
                map.put("name", "Android.OutboundCall");
                NetworkUtil.sendOkHttpRequestPost(address, map,
                        new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String string = response.body().string();
                                try {
                                    sleep(1000);
                                } catch (Exception e) {

                                }

                                final String in=NetworkUtil.parseJSON(string);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        responseTextView.setText(in);
                                    }
                                });
                            }
                        });
                //address = "http://10.28.4.176:8091/app/QueryVersionJSON.jsp";
                //NetworkUtil.parseXMLSAXURL(,);

            }
        });
    }
}
