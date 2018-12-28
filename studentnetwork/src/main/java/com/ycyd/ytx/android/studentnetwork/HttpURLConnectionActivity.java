package com.ycyd.ytx.android.studentnetwork;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection connection=null;
                        BufferedReader reader=null;
                        try{
/*
                            URL url=new URL("http://www.baidu.com");
                            connection=(HttpURLConnection)url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(8000);
                            connection.setReadTimeout(8000);
                            connection.connect();
                            InputStream in=connection.getInputStream();
                            reader=new BufferedReader(new InputStreamReader(in));
                            StringBuilder response =new StringBuilder();
                            String line;
                            //line=reader.readLine();
                            while((line=reader.readLine())!=null){
                                response.append(line);
                            }
 */

/*GET模式
                            // 1. 得到访问地址的URL
                            //URL url = new URL("https://www.baidu.com");
                            URL url = new URL("http://111.47.64.131:8091/app/QueryVersionJSON.jsp?name=Android.OutboundCall");
                            // 2. 得到网络访问对象java.net.HttpURLConnection
                            connection = (HttpURLConnection) url.openConnection();
                            // 3. 设置请求参数（过期时间，输入、输出流、访问方式），以流的形式进行连接
                            // 设置请求方式
                            connection.setRequestMethod("GET");
                            // 设置是否向HttpURLConnection输出
                            connection.setDoOutput(false);
                            // 设置是否从httpUrlConnection读入
                            connection.setDoInput(true);
                            // 设置是否使用缓存
                            connection.setUseCaches(true);
                            // 设置此 HttpURLConnection 实例是否应该自动执行 HTTP 重定向
                            connection.setInstanceFollowRedirects(true);
                            // 设置超时时间
                            connection.setConnectTimeout(3000);
                            //name=Android.OutboundCall
                            // 连接
                            connection.connect();
                            // 4. 得到响应状态码的返回值 responseCode
                            int code = connection.getResponseCode();
                            // 5. 如果返回值正常，数据在网络中是以流的形式得到服务端返回的数据
                            StringBuilder msg = new StringBuilder();
                            if (code == 200) { // 正常响应
                                // 从流中读取响应信息
                                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                String line = null;
                                while ((line = reader.readLine()) != null) { // 循环从流中读取
                                    msg.append(line);
                                }
                            }
                            // 6. 断开连接，释放资源
                            connection.disconnect();
GET模式*/



//*Post模式
                            // 1. 得到访问地址的URL
                            //URL url = new URL("http://111.47.64.131:8091/app/QueryVersionJSON.jsp?name=Android.OutboundCall");
                            //URL url = new URL("http://10.28.4.176:8091/app/test.xml");
                            //http://www.w3school.com.cn/example/xmle/cd_catalog.xml
                            URL url = new URL("http://10.28.4.176:8091/app/test.txt");
                            // 2. 得到网络访问对象java.net.HttpURLConnection
                            connection = (HttpURLConnection) url.openConnection();
                            // 3. 设置请求参数（过期时间，输入、输出流、访问方式），以流的形式进行连接
                            // 设置请求方式
                            //connection.setRequestMethod("POST");
                            //connection.setRequestMethod("GET");
                            // 设置是否向HttpURLConnection输出
                            //connection.setDoOutput(true);
                            // 设置是否从httpUrlConnection读入
                            //connection.setDoInput(true);
                            // 设置是否使用缓存
                            //connection.setUseCaches(true);
                            // 设置此 HttpURLConnection 实例是否应该自动执行 HTTP 重定向
                            //connection.setInstanceFollowRedirects(true);
                            // 设置超时时间
                            //connection.setConnectTimeout(3000);
                            //connection.setRequestProperty("Accept", "text/xml");
                            //DataOutputStream out=new DataOutputStream(connection.getOutputStream());
                            //out.writeBytes("name=Android.OutboundCall");//&paramter=XXXXX

                            // 连接
                            //connection.connect();
                            // 4. 得到响应状态码的返回值 responseCode
                            int code = connection.getResponseCode();
                            // 5. 如果返回值正常，数据在网络中是以流的形式得到服务端返回的数据
                            StringBuilder msg = new StringBuilder();
                            if (code == 200) { // 正常响应
                                // 从流中读取响应信息
                                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                String line = null;
                                while ((line = reader.readLine()) != null) { // 循环从流中读取
                                    msg.append(line);
                                }
                            }
                            // 6. 断开连接，释放资源
                            connection.disconnect();
// Post模式*/


                            // 显示响应结果
                            showResponse(msg.toString());
                        } catch (MalformedURLException e) {
                            // URL格式错误
                            System.out.println("ytx1");
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            // 不支持你设置的编码
                            System.out.println("ytx2");
                            e.printStackTrace();
                        } catch (ProtocolException e) {
                            // 请求方式不支持
                            System.out.println("ytx3");
                            e.printStackTrace();
                        } catch (IOException e) {
                            // 输入输出通讯出错
                            System.out.println("ytx4");
                            e.printStackTrace();
                        }finally {
                            // 关闭流
                            if(reader!=null){
                                try{
                                    reader.close();
                                }catch (IOException e){
                                    e.printStackTrace();
                                }
                            }
                            if(connection!=null){
                                connection.disconnect();
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
