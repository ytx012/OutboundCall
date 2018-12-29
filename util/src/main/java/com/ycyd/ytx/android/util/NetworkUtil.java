package com.ycyd.ytx.android.util;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.xml.parsers.SAXParserFactory;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class NetworkUtil {
    ////////////////////////////////gson
    /*
    {"versionCode":2}
    */
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

    public static String parseJSON(String strJSON) {
        String result="";
        try {
            //JSONObject解析
            //JSONObject jsonObject=new JSONObject(strJSON);
            //String ver=String.valueOf(jsonObject.getInt("versionCode"));
            //若是对象列表等使用
            //JSONArray jsonArray=new JSONArray(strJSON);
            //JSONObject jsonObject=jsonArray.getJSONObject(i);

            //google GSON解析
            Gson gson = new Gson();
            Ver ver = gson.fromJson(strJSON, Ver.class);
            //若是对象列表等使用
            //List<Ver> list=gson.fromJson(reader，new TypeToken<List<Ver>>(){}.getType());
            result=String.valueOf(ver.getVersionCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  result;
    }
    ////////////////////////////////XML
    /*
    XML示例
    <books>
    <book>
    <name>book1</name>
    <price>99</price>
    </book>
    <book>
    <name>book2</name>
    <price>10</price>
    </book>
    </books>
     */
    public static void parseXMLSAXString(final String strXML,
                                  final HttpCallbackListener listener) {
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
                    //Log.d("httpreader", "parseXMLSAX: ");
                    xmlReader.parse(new InputSource(new StringReader(strXML)));

                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                }
            }

            //SAX解析接口
            class ContentHandlerpro extends DefaultHandler {
                //private String TAG = "ContentHandlerpro";
                private String nodeName;
                private StringBuilder sName;
                private StringBuilder iPrince;
                private StringBuilder result;

                @Override
                public void endDocument() throws SAXException {
                    if (listener != null) {
                        listener.onFinish(result.toString());
                    }
                }

                @Override
                public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
                    //Log.d(TAG, "startElement-localName: " + localName);
                    //Log.d(TAG, "startElement-qName: " + qName);
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
        }).start();
    }


    public static void parseXMLSAXURL(final String addresss,
                               final HttpCallbackListener listener) {
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
                    //Log.d("httpreader", "parseXMLSAX: ");
                    //第1种直接传url
                    xmlReader.parse(addresss);

                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                }
            }

            //SAX解析接口
            class ContentHandlerpro extends DefaultHandler {
                //private String TAG = "ContentHandlerpro";
                private String nodeName;
                private StringBuilder sName;
                private StringBuilder iPrince;
                private StringBuilder result;

                @Override
                public void endDocument() throws SAXException {
                    if (listener != null) {
                        listener.onFinish(result.toString());
                    }
                }

                @Override
                public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
                    //Log.d(TAG, "startElement-localName: " + localName);
                    //Log.d(TAG, "startElement-qName: " + qName);
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
        }).start();
    }


    public static String parseXMLPull(final String strXML) {
        StringBuilder stringResult = new StringBuilder();
        try {
            //*Pull解析
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            // 由工厂创建一个解析器对象
            XmlPullParser parser = factory.newPullParser();
            // 打开xml文档对应的输入流,填写xml文档的路径
            //parser.setInput(new FileInputStream("src/xmlwendang/student.xml"), "UTF-8");
            parser.setInput(new StringReader(strXML));
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

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringResult.toString();
    }

    //OkHttp*GET模式*
    public static void sendOkHttpRequestGet
    (final String address,
     Callback callback) {
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        httpClient.newCall(request).enqueue(callback);
    }

    //OkHttp*POST模式*
    public static void sendOkHttpRequestPost
    (final String address,
     final Map<String, String> map,
     Callback callback) {
        OkHttpClient httpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : map.keySet()) {
            builder.add(key, map.get(key));
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        httpClient.newCall(request).enqueue(callback);
    }

    //HttpURLConnection*GET模式*
    public static void sendHttpRequestGet(final String address,
                                          final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    // 1. 得到访问地址的URL
                    URL url = new URL(address);
                    // 2. 得到网络访问对象java.net.HttpURLConnection
                    connection = (HttpURLConnection) url.openConnection();
                    // 3. 设置请求参数（过期时间，输入、输出流、访问方式），以流的形式进行连接
                    // 设置请求方式
                    connection.setRequestMethod("GET");
                    // 设置超时时间
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    // 设置是否向HttpURLConnection输出
                    connection.setDoOutput(true);
                    // 设置是否从httpUrlConnection读入
                    connection.setDoInput(true);
                    // 设置是否使用缓存
                    connection.setUseCaches(true);
                    // 设置此 HttpURLConnection 实例是否应该自动执行 HTTP 重定向
                    connection.setInstanceFollowRedirects(true);
                    // 连接
                    connection.connect();
                    // 4. 得到响应状态码的返回值 responseCode
                    int code = connection.getResponseCode();
                    // 5. 如果返回值正常，数据在网络中是以流的形式得到服务端返回的数据
                    if (code == 200) {
                        // 正常响应
                        // 从流中读取响应信息
                        StringBuilder msg = new StringBuilder();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            // 循环从流中读取
                            msg.append(line);
                        }
                        listener.onFinish(msg.toString());
                    }

                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }

                } finally {
                    // 6. 断开连接，释放资源
                    if (connection != null) {
                        connection.disconnect();
                    }

                }
            }
        }).start();
    }

    //HttpURLConnection*POST模式*
    public static void sendHttpRequestpost
    (final String address,
     final String parameter,
     final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    // 1. 得到访问地址的URL
                    URL url = new URL(address);
                    // 2. 得到网络访问对象java.net.HttpURLConnection
                    connection = (HttpURLConnection) url.openConnection();
                    // 3. 设置请求参数（过期时间，输入、输出流、访问方式），以流的形式进行连接
                    // 设置请求方式
                    connection.setRequestMethod("POST");
                    // 设置超时时间
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    // 设置是否向HttpURLConnection输出
                    connection.setDoOutput(true);
                    // 设置是否从httpUrlConnection读入
                    connection.setDoInput(true);
                    // 设置是否使用缓存
                    connection.setUseCaches(true);
                    // 设置此 HttpURLConnection 实例是否应该自动执行 HTTP 重定向
                    connection.setInstanceFollowRedirects(true);
                    // 连接
                    connection.connect();

                    // 获取URLConnection对象对应的输出流
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    // 发送请求参数
                    out.writeBytes(parameter);//post的参数 xx=xx&yy=yy
                    // flush输出流的缓冲
                    out.flush();

                    // 4. 得到响应状态码的返回值 responseCode
                    int code = connection.getResponseCode();
                    // 5. 如果返回值正常，数据在网络中是以流的形式得到服务端返回的数据
                    if (code == 200) {
                        // 正常响应
                        // 从流中读取响应信息
                        StringBuilder msg = new StringBuilder();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            // 循环从流中读取
                            msg.append(line);
                        }
                        listener.onFinish(msg.toString());
                    }

                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }

                } finally {
                    // 6. 断开连接，释放资源
                    if (connection != null) {
                        connection.disconnect();
                    }

                }
            }
        }).start();
    }
}
