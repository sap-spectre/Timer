package cdt.spectre.timer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class stoppless_timer extends AppCompatActivity {

    public static String hours = "00";
    public static String minutes = "00";
    public static String seconds = "00";

    public static long ss = 0;
    public static long mm = 0;
    public static long hh = 0;
    public static int sec = 0;
    public static int min = 0;
    public static int hour = 0;

    public static TextView timeHH;
    public static TextView timeMM;
    public static TextView timeSS;

    public static Button plusHH;
    public static Button plusMM;
    public static Button plusSS;

    public static Button minusHH;
    public static Button minusMM;
    public static Button minusSS;

    public static TextView applog;
    public static TextView applogs;
    public static Button startb;
    public static Button stopb;
    public static Button forceb;

    public static boolean paused = false;
    public static boolean running = false;
    public static boolean endtime = true;

    public static long finaltime;
    public static long millis = 0;
    public static long setendtime = 0;
    public static long timeleft = 0;

    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor;

    public static CountDownTimer cdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        sp = getSharedPreferences("timer", stoppless_timer.MODE_PRIVATE);
        timerStatusGet();
        pausedGet();

        timeHH = (TextView) findViewById(R.id.time_hh);
        timeMM = (TextView) findViewById(R.id.time_mm);
        timeSS = (TextView) findViewById(R.id.time_ss);

        plusSS = (Button) findViewById(R.id.ss_plus);
        minusSS = (Button) findViewById(R.id.ss_minus);
        plusMM = (Button) findViewById(R.id.mm_plus);
        minusMM = (Button) findViewById(R.id.mm_minus);
        plusHH = (Button) findViewById(R.id.hh_plus);
        minusHH = (Button) findViewById(R.id.hh_minus);


        applog = (TextView) findViewById(R.id.txtlog);
        applogs = (TextView) findViewById(R.id.txtlogs);

        startb = (Button) findViewById(R.id.bstart);
        stopb = (Button) findViewById(R.id.bstop);
        forceb = (Button) findViewById(R.id.bforce);

        if (!endtime && !paused) {
            timeleft = setendtime - System.currentTimeMillis();
            if (timeleft > 1000) {
                hourTimer(timeleft);
                startb.setText("LOG");
                stopb.setText("PAUSE");
            } else {
                timerStatusSave(0, true);
                startb.setText("START");
                stopb.setText("PAUSE");
            }
        } else if (!endtime && paused) {
            startb.setText("RESUME");
            stopb.setText("RESET");
            long seconds = timeleft / 1000;
            long hours = seconds / 3600;
            long mins = (seconds % 3600) / 60;
            long secs = (seconds % 3600) % 60;
            hour = (int) hours;
            min = (int) mins;
            sec = (int) secs;
            timeSS.setText(String.format("%02d", sec));
            timeMM.setText(String.format("%02d", min));
            timeHH.setText(String.format("%02d", hour));
            applog.setText("Timer Paused");
        }
        //applogs.setText(setendtime + "" + "    timeleft" + timeleft + "   boolean" + endtime);
        applogs.setText("");
        plusSS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ss >= 59) {
                    ss = 0;
                } else {
                    ss = ss + 1;
                }
                timeSS.setText(String.format("%02d", ss));

            }
        });
        minusSS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ss <= 0) {
                    ss = 59;
                } else {
                    ss = ss - 1;
                }
                timeSS.setText(String.format("%02d", ss));
            }
        });
        plusMM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mm >= 59) {
                    mm = 0;
                } else {
                    mm = mm + 1;
                }
                timeMM.setText(String.format("%02d", mm));
            }
        });
        minusMM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mm <= 0) {
                    mm = 59;
                } else {
                    mm = mm - 1;
                }
                timeMM.setText(String.format("%02d", mm));
            }
        });
        plusHH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hh >= 59) {
                    hh = 0;
                } else {
                    hh = hh + 1;
                }
                timeHH.setText(String.format("%02d", hh));
            }
        });
        minusHH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hh <= 0) {
                    hh = 59;
                } else {
                    hh = hh - 1;
                }
                timeHH.setText(String.format("%02d", hh));
            }
        });

        startb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!endtime && running) {
                    applogs.append(hour + ":" + min + ":" + sec + " \n");
                    applog.setText("Logged successfully");
                } else if (endtime && !running) {
                    if (hh != 0) {
                        finaltime = hh * 60 * 60;
                    }
                    if (mm != 0) {
                        finaltime = finaltime + (mm * 60);
                    }
                    finaltime = (finaltime + ss) * 1000;
                    if (finaltime <= 0) {
                        applog.setText("Please set a valid time");
                        return;
                    }
                    setendtime = System.currentTimeMillis() + finaltime;
                    endtime = false;
                    timerStatusSave(setendtime, false);
                    timeleft = finaltime;
                    hourTimer(timeleft);
                    applog.setText("Timer Started");
                    finaltime = ss = mm = hh = 0;
                    stopb.setText("PAUSE");
                    startb.setText("LOG");
                } else if (paused && !running) {
                    startb.setText("LOG");
                    stopb.setText("PAUSE");
                    hourTimer(timeleft);
                    paused = false;
                    pausedSet(0, false);
                    applog.setText("Timer Resumed");
                }
            }
        });

        stopb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (running) {
                    timeleft = millis;
                    paused = true;
                    applogs.setText(timeleft + "");
                    pausedSet(timeleft, paused);
                    cdt.cancel();
                    running = false;
                    applog.setText("Timer paused by user. Last logged: " + hour + ":" + min + ":" + sec + "");
                    stopb.setText("RESET");
                    startb.setText("RESUME");
                } else {
                    timeSS.setText("00");
                    timeMM.setText("00");
                    timeHH.setText("00");
                    applog.setText("Timer Stopped.  Cleared all logs");
                    endtime = true;
                    pausedSet(0, false);
                    paused = false;
                    timerStatusSave(0, true);
                    applogs.setText("");
                    startb.setText("START");
                    stopb.setText("RESET");
                }
            }
        });

        forceb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(running){
                cdt.cancel();}
                endtime = true;
                paused = false;
                timeSS.setText("00");
                timeMM.setText("00");
                timeHH.setText("00");
                startb.setText("START");
                stopb.setText("PAUSE");
                applog.setText("Timer force stopped, and cleared memory");
                applogs.setText("");
               editor = sp.edit();
                editor.clear();
                editor.commit();

            }
        });

    }


    private void hourTimer(long lefttime) {
        cdt = new CountDownTimer(lefttime, 1000) {
            @Override
            public void onTick(long timeRunning) {
                millis = timeRunning;
                long seconds = timeRunning / 1000;
                long hours = seconds / 3600;
                long mins = (seconds % 3600) / 60;
                long secs = (seconds % 3600) % 60;
                hour = (int) hours;
                min = (int) mins;
                sec = (int) secs;
                timeSS.setText(String.format("%02d", sec));
                timeMM.setText(String.format("%02d", min));
                timeHH.setText(String.format("%02d", hour));
                running = true;
            }

            @Override
            public void onFinish() {
                endtime = true;
                running = false;
                timeSS.setText("00");
                applog.setText("Timer Ended");
                timerStatusSave(0, true);
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

    public static void pausedSet(long left, boolean bool) {
        editor = sp.edit();
        editor.putBoolean("paused", bool);
        editor.putLong("pause", left);
        editor.apply();
    }

    public static void pausedGet() {
        paused = sp.getBoolean("paused", false);
        timeleft = sp.getLong("pause", 0);
    }

}
