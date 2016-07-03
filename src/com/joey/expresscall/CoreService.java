package com.joey.expresscall;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.joey.general.utils.MyLog;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/6/12.
 */
public class CoreService extends Service {

    private Timer checkTimer;
    private TimerTask mTask  = new TimerTask() {
        @Override
        public void run() {
            MyLog.i("timer start");
        }
    };
    private long TIME_DELAY = 1000*60*60;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        scheduleTimer();
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseTimer();
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    private  void scheduleTimer(){
        if(checkTimer != null){
            return;
        }
        checkTimer = new Timer();
        checkTimer.schedule(mTask,6000,1000);
    }

    private void releaseTimer(){
        if(checkTimer ==null){
           return;
        }
        checkTimer.cancel();
        checkTimer.purge();
        mTask.cancel();
        checkTimer = null;
    }
}
