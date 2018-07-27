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
    public static long finaltime;

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
        timerStatusGet();
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
        pauseb = (Button) findViewById(R.id.bpause);
        resumeb = (Button) findViewById(R.id.bresume);

        if (!endtime) {
            timeleft = setendtime - System.currentTimeMillis();
            if (timeleft > 1000) {
                hourTimer(timeleft);
            } else {
                timerStatusSave(0, true);
            }
        }
        applogs.setText(setendtime + "" + "    timeleft" + timeleft + "   boolean" + endtime);

        plusSS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ss >= 59) {
                    ss = 0;
                } else {
                    ss = ss + 1;
                }

                if (ss < 10) {
                    timeSS.setText("0" + ss + "");
                } else {
                    timeSS.setText(ss + "");
                }
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
                if (ss < 10) {
                    timeSS.setText("0" + ss + "");
                } else {
                    timeSS.setText(ss + "");
                }
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

                if (mm < 10) {
                    timeMM.setText("0" + mm + "");
                } else {
                    timeMM.setText(mm + "");
                }
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
                if (mm < 10) {
                    timeMM.setText("0" + mm + "");
                } else {
                    timeMM.setText(mm + "");
                }
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

                if (hh < 10) {
                    timeHH.setText("0" + hh + "");
                } else {
                    timeHH.setText(hh + "");
                }
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
                if (hh < 10) {
                    timeHH.setText("0" + hh + "");
                } else {
                    timeHH.setText(hh + "");
                }
            }
        });

        startb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!endtime && running) {
                    applog.setText("Already a timer is running");
                } else {
                    if (hh != 0) {
                        finaltime = hh * 60 * 60;
                    }
                    if (mm != 0) {
                        finaltime = finaltime + (mm * 60);
                    }
                    finaltime = (finaltime + ss) * 1000;
                    setendtime = System.currentTimeMillis() + finaltime;
                    timerStatusSave(setendtime, false);
                    timeleft = finaltime;
                    hourTimer(timeleft);
                    applog.setText("Timer Started");

                    finaltime = ss = mm = hh = 0;
                }
            }
        });

        stopb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (running) {
                    cdt.cancel();
                    running = false;
                    timerStatusSave(0, true);
                    timeSS.setText("00");
                    timeMM.setText("00");
                    timeHH.setText("00");
                    applog.setText("Timer stopped by user. Last logged: " + hour + ":" + min + ":" + sec + "");
                } else {
                    applog.setText("No timer running to stop");
                }
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


    private void hourTimer(long lefttime) {
        cdt = new CountDownTimer(lefttime, 1000) {
            @Override
            public void onTick(long timeRunning) {
                long seconds = timeRunning / 1000;
                long hours = seconds / 3600;
                long mins = (seconds % 3600) / 60;
                long secs = (seconds % 3600) % 60;
                hour = (int) hours;
                min = (int) mins;
                sec = (int) secs;

                if (sec < 10) {
                    timeSS.setText("0" + sec + "");
                } else {
                    timeSS.setText(sec + "");
                }
                if (min < 10) {
                    timeMM.setText("0" + min + "");
                } else {
                    timeMM.setText(min + "");
                }
                if (hour < 10) {
                    timeHH.setText("0" + hour + "");
                } else {
                    timeHH.setText(hour + "");
                }
                applogs.setText(secs + "");
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
}
