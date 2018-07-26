package cdt.spectre.timer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class stoppless_timer extends AppCompatActivity {

    public static TextView timenow;
    public static TextView timer;
    public static TextView applog;
    public static TextView applogs;
    public Button startb;
    public Button pauseb;
    public Button resumeb;
    public Button stopb;
    public static boolean running = false;

    public static long setendtime = 0;
    public static boolean endtime = true;
    public static long timeleft = 0;
    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor;

    public static CountDownTimer cdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        sp = getSharedPreferences("timer", stoppless_timer.MODE_PRIVATE);

        timenow = (TextView) findViewById(R.id.txt1);
        timer = (TextView) findViewById(R.id.txt2);
        applog = (TextView) findViewById(R.id.txtlog);
        applogs = (TextView) findViewById(R.id.txtlogs);
        startb = (Button) findViewById(R.id.bstart);
        stopb = (Button) findViewById(R.id.bstop);
        pauseb = (Button) findViewById(R.id.bpause);
        resumeb = (Button) findViewById(R.id.bresume);

        timerStatusGet();

        if(!endtime && !running){
            timeleft = setendtime - System.currentTimeMillis();
            timerStart(timeleft);
        }

        startb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setendtime = System.currentTimeMillis() + 60000;
                timerStatusSave(setendtime, false);
                timeleft = setendtime - System.currentTimeMillis();
                timerStart(timeleft);
                applog.setText("Timer Started");
            }
        });

        stopb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cdt.cancel();
                timerStatusSave(System.currentTimeMillis(), true);
                running = false;
                timer.setText("Timer Stopped");
                applog.setText("Timer Stopped");
            }
        });

        pauseb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applog.setText("Not added this operation");
            }
        });

        resumeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applog.setText("Not added this operation");
            }
        });

    }

    public static void timerStart(long lefttime) {
        cdt = new CountDownTimer(lefttime, 1000) {
            @Override
            public void onTick(long l) {
                timer.setText((l / 1000) +  " seconds");
                running = true;
            }

            @Override
            public void onFinish() {
                endtime = true;
                running = false;
                timer.setText("Timer End");
                timerStatusSave(System.currentTimeMillis(), true);
            }
        }.start();
    }

    public static void timerStatusSave(long timenow, boolean statenow) {
        editor = sp.edit();
        editor.putLong("setendtime", timenow);
        editor.putBoolean("endtime", statenow);
        editor.apply();
    }
    public static void timerStatusGet() {
        setendtime = sp.getLong("setendtime", 0);
        endtime = sp.getBoolean("endtime", true);
    }
}
