package team25.conveniencestore.activitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;
import team25.conveniencestore.R;

public class LoadingActivity extends AppCompatActivity {

    private static final int SPASH_TIME_OUT = 4000;
    private RingProgressBar ringProgressBar;
    private Handler myHandler;
    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ringProgressBar = (RingProgressBar) findViewById(R.id.ringProgressBar);

        setRingProgress();
    }

    @SuppressLint("HandlerLeak")
    private void setRingProgress() {
        ringProgressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {
            @Override
            public void progressToComplete() {
                Intent i = new Intent(LoadingActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });

        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    if (progress < 100) {
                        progress++;
                        ringProgressBar.setProgress(progress);
                    }
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(SPASH_TIME_OUT/100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    myHandler.sendEmptyMessage(0);
                }
            }
        }).start();
    }
}
