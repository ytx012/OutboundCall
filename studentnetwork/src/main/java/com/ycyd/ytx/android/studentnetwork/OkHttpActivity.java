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
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.xml.sax.InputSource;

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
                String sURL = "http://111.47.64.131:8091/app/test.xml";
                //sendRequestWithOkHttp(sURL);
                parseXMLSAX(sURL);
            }

            private void sendRequestWithOkHttp(final String sURL) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient client = new OkHttpClient();
                            //*GET
                            Request request = new Request.Builder()
                                    //.url("http://www.baidu.com")
                                    //.url("http://111.47.64.131:8091/app/QueryVersionJSON.jsp?name=Android.outboundcall")
                                    .url(sURL)
                                    //.url("http://www.w3school.com.cn/example/xmle/cd_catalog.xml")
                                    .build();
                            /*/
                            /*POST
                            RequestBody requestBody=new FormBody.Builder()
                                    .add("name","Android.outboundcall")
                                    //.add(name,value)
                                    .build();
                            Request request=new Request.Builder()
                                    .url("http://111.47.64.131:8091/app/QueryVersionJSON.jsp")
                                    .post(requestBody)
                                    .build();
                            */
                            Response response = client.newCall(request).execute();
                            String reader = response.body().string();
                            //showResponse(reader);
                            //解析JSON
                            //parseJSON(reader);
                            parseXMLPull(reader);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    private void parseJSON(String reader) {
                        try {
                            //JSONObject解析
                            //JSONObject jsonObject=new JSONObject(reader);
                            //final String ver=String.valueOf(jsonObject.getInt("versionCode"));
                            //若是对象列表等使用
                            //JSONArray jsonArray=new JSONArray(reader);
                            //JSONObject jsonObject=jsonArray.getJSONObject(i);
                            //showResponse(ver);

                            //google GSON解析
                            Gson gson = new Gson();
                            Ver ver = gson.fromJson(reader, Ver.class);
                            //若是对象列表等使用
                            //List<Ver> list=gson.fromJson(reader，new TypeToken<List<Ver>>(){}.getType());
                            showResponse(String.valueOf(ver.getVersionCode()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    //ver对象
                    class Ver {
                        protected int versionCode;

                        public int getVersionCode() {
                            return this.versionCode;
                        }

                        public void setVersionCode(int versionCode) {
                            this.versionCode = versionCode;
                        }
                    }

                    private void parseXMLPull(String reader) {
                        StringBuilder stringResult = new StringBuilder();
                        try {
                            //*Pull解析
                            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                            // 由工厂创建一个解析器对象
                            XmlPullParser parser = factory.newPullParser();
                            // 打开xml文档对应的输入流,填写xml文档的路径
                            //parser.setInput(new FileInputStream("src/xmlwendang/student.xml"), "UTF-8");
                            parser.setInput(new StringReader(reader));
                            // 获得事件类型
                            int eventType = parser.getEventType();
                            //System.out.println(eventType == XmlPullParser.START_DOCUMENT);
                            String sName = "", iPrince = "";
                            while (eventType != XmlPullParser.END_DOCUMENT) {
                                switch (eventType) {
                                    case XmlPullParser.START_DOCUMENT:
                                        System.out.println("读取开始,开始文档!");
                                        break;
                                    case XmlPullParser.START_TAG:
                                        String tagName = parser.getName();
                                        if (tagName.equals("name")) {
                                            sName = parser.nextText();
                                        }
                                        if (tagName.equals("price")) {
                                            iPrince = parser.nextText();
                                        }
                                        break;
                                    case XmlPullParser.END_TAG:
                                        if (parser.getName().equals("book")) {
                                            //System.out.println("读取进度-结束标签:" + parser.getName());
                                            stringResult.append("书名：" + sName + ";价格:" + iPrince);
                                        } else if (parser.getName().equals("books")) {
                                            //System.out.println("读取完成-结束根目录:" + parser.getName());
                                        }
                                        break;
                                }
                                // 读取下一个节点
                                eventType = parser.next();
                            }
                            //System.out.println("读取完成，文档结束!");
                            showResponse(stringResult.toString());
                        } catch (Exception e) {
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

            private void parseXMLSAX(final String sURL) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //SAX解析
                            //1.创建解析工厂
                            SAXParserFactory factory = SAXParserFactory.newInstance();
                            //2.得到解析器
                            //3得到解读器
                            XMLReader xmlReader = factory.newSAXParser().getXMLReader();
                            //设置内容处理器
                            xmlReader.setContentHandler(new ContentHandlerpro());
                            //读取xml的文档内容
                            Log.d("httpreader", "parseXMLSAX: ");
                            //第1种直接传url
                            //xmlReader.parse(sURL);
                            //第2种传数据
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url(sURL)
                                    .build();
                            Response response = client.newCall(request).execute();
                            String reader = response.body().string();
                            xmlReader.parse(new InputSource(new StringReader(reader)));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    //SAX解析接口
                    class ContentHandlerpro extends DefaultHandler {
                        private String TAG = "ContentHandlerpro";
                        private String nodeName;
                        private StringBuilder sName;
                        private StringBuilder iPrince;
                        private StringBuilder result;

                        @Override
                        public void endDocument() throws SAXException {
                            showResponse(result.toString());
                        }

                        @Override
                        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
                            Log.d(TAG, "startElement-localName: " + localName);
                            Log.d(TAG, "startElement-qName: " + qName);
                            nodeName = localName;
                        }

                        @Override
                        public void startDocument() throws SAXException {
                            sName = new StringBuilder();
                            iPrince = new StringBuilder();
                            result = new StringBuilder();
                        }

                        @Override
                        public void endElement(String uri, String localName, String qName) throws SAXException {
                            if ("book".equals(localName)) {
                                result.append("书名:" + sName.toString().trim() + ";");
                                result.append("价格:" + iPrince.toString().trim() + "\r\n");
                                sName.setLength(0);
                                iPrince.setLength(0);
                            }
                        }

                        @Override
                        public void characters(char[] ch, int start, int length) throws SAXException {
                            if ("name".equals(nodeName)) {
                                sName.append(ch, start, length);
                            } else if ("price".equals(nodeName)) {
                                iPrince.append(ch, start, length);
                            }
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
