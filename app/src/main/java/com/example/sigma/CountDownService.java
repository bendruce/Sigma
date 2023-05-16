// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// CODE FOR A CONTENT PROVIDER FOR A WORKOUT TIMER WHICH KEEPS RUNNING IF THE SUER MOVES OUTSIDE THE APP
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
package com.example.sigma;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class CountDownService extends Service {//this service manages a countdown timer that will broadcast updates, used for a workout timer

    public static final String COUNTDOWN_BR = "com.example.sigma.countdown_br";//the countdown broadcast receiver's action string
    Intent bi = new Intent(COUNTDOWN_BR);//an intent for broadcasting the countdown updates
    CountDownTimer cdt = null;//the countdown timer

    @Override
    public void onCreate() {//called when the service is created
        super.onCreate();

        cdt = new CountDownTimer(30000000, 1000) {//initialize the countdown timer to 30000000 milliseconds (approximately 8.3 hours which is more than enough as workouts are normally <3hrs) with 1-second intervals
            @Override
            public void onTick(long millisUntilFinished) {//called for every tick of the countdown (every 1 second)
                bi.putExtra("countdown", millisUntilFinished);
                LocalBroadcastManager.getInstance(CountDownService.this).sendBroadcast(bi);//send the broadcast with the countdown update
            }

            @Override
            public void onFinish() {//called when the countdown is finished
                bi.putExtra("finished", true);
                LocalBroadcastManager.getInstance(CountDownService.this).sendBroadcast(bi); //send the broadcast with the finished status
            }
        };
        cdt.start();
    }

    @Override
    public void onDestroy() {
        cdt.cancel();//cancel the countdown timer
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
