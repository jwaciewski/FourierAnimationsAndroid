package com.julianserver.fourieranimations;

import com.parse.Parse;
import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);


        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("FourierAnimations")

                .server("http://192.168.1.182:1337/parse")
                .build()


        );
    }
}