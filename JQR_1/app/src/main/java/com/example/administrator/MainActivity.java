package com.example.administrator;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "huhxRobot";
    private TextView textView;
    private EditText editText;
    private final String apiUrl = "http://www.tuling123.com/openapi/api";
    private final String apiKey = "a213df7d97684742bcf8855e396bafd4";
    String urlStr = apiUrl+"?key="+apiKey;   //添加网址及接入图灵机器人apiKey             拼接http://www.tuling123.com/openapi/api?key=a213df7d97684742bcf8855e396bafd4&info=
    final static int ROBOT_MESSAGE = 0;      //初始化机器人信息ROBOT_MESSAGE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editView);
        textView = (TextView) findViewById(R.id.textView);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());

    }

    public void sendMessage(View view) {
        String sendmessage = editText.getText().toString();   // 把流中的数据转换成字符串,采用的编码是utf-8(模拟器默认编码)
        final String params = "info=" + sendmessage;   //将编辑框输入的聊天信息添加入info后
        new Thread(new Runnable() {           // new一个线程
            @Override
            public void run() {
                HttpURLConnection connection = null;
                OutputStream outputStream = null;   //输入
                BufferedReader reader = null;
                StringBuilder result = new StringBuilder();
                String line = "";
                try {
                    URL url = new URL(urlStr);    //创建一个URL对象
                    connection = (HttpURLConnection) url.openConnection(); //调用URL的openConnection()方法,获取HttpURLConnection对象，利用HttpURLConnection对象从网络中获取网页数据
                    connection.setRequestMethod("POST");  // 设定请求的方法为"POST"，默认是GET：GET：请求获取Request-URI所标识的资源； POST：在Request-URI所标识的资源后附加新的数据
                    connection.setReadTimeout(5000); //设置从主机读取数据超时（单位：毫秒）
                    connection.setConnectTimeout(5000); //设置连接主机超时（单位：毫秒）

                    outputStream = connection.getOutputStream();  // 获得一个输出流,向服务器写数据
                    outputStream.write(params.getBytes());   //写入参数获取字节

                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); // 调用HttpURLConnection连接对象的getInputStream()函数,获取输入流
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    Message message = new Message();           //new一个信息对象
                    message.obj = result.toString();
                    message.what = ROBOT_MESSAGE;
                    handler.sendMessage(message);
                } catch (Exception e) {           //捕获异常
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    connection.disconnect();          //断开连接
                }
            }
        }).start();     //开启线程
    }


    private Handler handler = new Handler() {            //handler是Android给我们提供用来更新UI的一套机制，也是一套消息处理的机制，我们可以用它发送消息，也可以通过它处理消息。联系framework可以详细看到。生命周期的改变都是通过handler消息改变的。
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ROBOT_MESSAGE:
                    String Jsonmessage = (String) msg.obj;   //msg.ob使用来放对象的
                    Log.i(TAG, Jsonmessage);

                    String text = "";
                    try {
                            JSONObject jsonObject = new JSONObject(Jsonmessage);
                        text = (String) jsonObject.get("text");
                    } catch (JSONException e) {
                        e.printStackTrace();  //抛出异常，直接用e.printStackTrace便可以直接输出错误发生函数的调用堆栈信息。
                    }

                    String [] temp = null;
                    temp = Jsonmessage .split("\"");  ////这里正确的是使用json解析，觉得麻烦就使用了字符串拆分

                    textView.setText(temp[5]);
                    Log.i(TAG, text);
                    Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
            }
        }
    };
}
