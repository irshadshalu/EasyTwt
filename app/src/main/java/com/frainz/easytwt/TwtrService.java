package com.frainz.easytwt;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import de.greenrobot.event.EventBus;

public class TwtrService extends Service {
    EventBus myEventBus;
    public TwtrService() {
        super();
    }
    @Override
    public void onCreate(){
        super.onCreate();
        myEventBus = EventBus.getDefault();

        // displayWindow();
        Toast.makeText(getApplicationContext(),"Service Started",Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        //   super.onStartCommand(intent,flags,startId);
        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
