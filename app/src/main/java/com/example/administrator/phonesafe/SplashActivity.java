package com.example.administrator.phonesafe;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.administrator.Utils.StreamUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    private static final String tag = "SplashActivity";
    private  TextView tv_version_name;
    private  int mLocalVersionCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initUI();
        initData();
    }


    /**
     * 初始化UI控件
     */
    private  void initUI(){

        tv_version_name = (TextView)findViewById(R.id.tv_version_name);
    }

    /**
     * 获取数据方法
     */
    private  void  initData(){
        tv_version_name.setText("版本名称："+getVersionName());
//        检测是否有更新，有就提示（本地与服务器的比较）
           mLocalVersionCode =  getVersionCode();
//        获取服务 器版本号（客户端发请求，服务器响应）
//        更新版本号，新版本的描述，服务器版本号，新版本apk下载地址

        checkVersion();





    }

    /**
     * 检测版本号
     */
    private void checkVersion() {

        new Thread(new Runnable() {
            @Override
            public void run() {
//                只能用模拟器访问tomcat
                try {
                  URL url = new URL("http://10.0.2.2:8080/update74.json");

                    try {
//                        开启一个链接
                        Log.i(tag ,"asd");
                        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                        connection.setConnectTimeout(2000);
                        connection.setReadTimeout(2000);
//                        connection.setRequestMethod("GET");默认get请求
//                        获取响应码
                        if(connection.getResponseCode()==200){
                            InputStream is =  connection.getInputStream();
                            String json = StreamUtil.streamToString(is);
                            Log.i(tag ,json);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }


    /**
     * 非0 则代表获取成功
     * @return
     */
    private int getVersionCode() {
        PackageManager pm = getPackageManager();
//        从包的管理者对象中，获取指定包名的基本信息（版本名称，版本号），0代表获取基本信息
        try{
            PackageInfo packageInfo =   pm.getPackageInfo(getPackageName() ,0);
            return packageInfo.versionCode;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  0;
    }

    private String getVersionName(){
    //        包管理者对象getPackageManager
       PackageManager pm = getPackageManager();
//        从包的管理者对象中，获取指定包名的基本信息（版本名称，版本号），0代表获取基本信息
        try{
            PackageInfo packageInfo =   pm.getPackageInfo(getPackageName() ,0);
            return packageInfo.versionName;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
