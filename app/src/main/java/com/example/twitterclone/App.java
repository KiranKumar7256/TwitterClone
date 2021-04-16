package com.example.twitterclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("3IRosBiB7kL2DxBuGUWUVl7NanSIvN86VdAClXld")
                // if defined
                .clientKey("raYq77aYkpDwbsQ1wSM2DoStk9HqbK2DrPNf2KRp")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
