package com.yangpf.pingtest;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.process;

public class MainActivity extends AppCompatActivity {
    private TextView mTextView;
    private TextView mTextView1;
    private Timer timer = new Timer();
    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    break;
                case 2:
                    break;

                default:
                    break;
            }
        }
    };
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            KLog.d("aaa", Thread.currentThread());
            ping(2, "www.baidu.com");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.textview);
        mTextView1 = findViewById(R.id.textView1);

        EventBus.getDefault().register(this);

        timer.schedule(timerTask,
                5 * 1000,//延迟5秒执行
                3*1000);//周期为1秒

    }

    /**
     * ping
     *
     * @param pingNum ping的次数
     * @param url     ping的目标URL
     * @return
     */
    public static void ping(int pingNum, final String url) {
        KLog.d("aaa", Thread.currentThread());
        new Thread() {
            @Override
            public void run() {
                KLog.d("aaa", Thread.currentThread());
                Process process = null;
                String result = "";
                String res = "1";
                try {
                    process = Runtime.getRuntime().exec("ping -c 3 " + url);
                    int status = 0;
                    try {
                        status = process.waitFor();
                        if (status == 0) {
                            result = "success";
                            KLog.d("aaa", result);
                        } else {
                            result = Integer.toString(status);
                            KLog.d("aaa", result);

                        }
                        EventBus.getDefault().post(new MessageEvent(1, result));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    InputStream input = process.getInputStream();
                    BufferedReader in = new BufferedReader(new InputStreamReader(input));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";
                    while ((line = in.readLine()) != null) {
                        buffer.append(line);
                        res += (line + "\r\n");
                    }

                    EventBus.getDefault().post(new MessageEvent(2, res));

                    if (res.indexOf("100% loss") != -1) {
                        System.out.println("与 " + url + " nononononon连接不畅通.");
                        KLog.d("aaa", "与 " + url + " 连接不畅通.");
                    } else {
                        System.out.println("与 " + url + " 连接畅通.");
                        KLog.d("aaa", "与 " + url + " 连接畅通.");

                    }

                    KLog.d("aaa", res);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }.start();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        if (messageEvent.getId() == 1) {
            KLog.d("aaa", "1");
            mTextView.setText("结果" + messageEvent.getMessage() + "结果");

        } else if (messageEvent.getId() == 2) {
            KLog.d("aaa", "2");

            mTextView1.setText(messageEvent.getMessage());

        }
    }

}

