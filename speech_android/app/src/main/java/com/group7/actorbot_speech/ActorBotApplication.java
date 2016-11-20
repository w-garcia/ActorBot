package com.group7.actorbot_speech;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Owner on 11/19/2016.
 */
public class ActorBotApplication extends Application
{
    public String ResponseServerIP = "192.168.1.77:8080";

    private static final String CLASS_NAME = ActorBotApplication.class.getSimpleName();

    public ActorBotApplication()
    {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks()
        {
            @Override
            public void onActivityCreated(Activity activity,Bundle savedInstanceState) {
                Log.d(CLASS_NAME, "Activity created: " + activity.getLocalClassName());
            }
            @Override
            public void onActivityStarted(Activity activity) {
                Log.d(CLASS_NAME, "Activity started: " + activity.getLocalClassName());
            }
            @Override
            public void onActivityResumed(Activity activity) {
                Log.d(CLASS_NAME, "Activity resumed: " + activity.getLocalClassName());
            }
            @Override
            public void onActivitySaveInstanceState(Activity activity,Bundle outState) {
                Log.d(CLASS_NAME, "Activity saved instance state: " + activity.getLocalClassName());
            }
            @Override
            public void onActivityPaused(Activity activity) {
                Log.d(CLASS_NAME, "Activity paused: " + activity.getLocalClassName());
            }
            @Override
            public void onActivityStopped(Activity activity) {
                Log.d(CLASS_NAME, "Activity stopped: " + activity.getLocalClassName());
            }
            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.d(CLASS_NAME, "Activity destroyed: " + activity.getLocalClassName());
            }
        });
    }

    public void setResponseServerIP(String s)
    {
        ResponseServerIP = s;
    }

    public String getResponseServerIP()
    {
        return ResponseServerIP;
    }
}
