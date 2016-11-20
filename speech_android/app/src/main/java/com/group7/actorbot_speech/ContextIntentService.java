package com.group7.actorbot_speech;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ContextIntentService extends IntentService {

    private static final String TAG = ContextIntentService.class.getSimpleName();

    public static final String CONTEXT_EXTRA = "context";
    public static final String PENDING_RESULT_EXTRA = "pending_result";
    public static final String RESPONSE_SERVER_IP = "response_server_ip";
    public static final String URL_EXTRA = "url";

    public static final int RESULT_CODE = 0;
    public static final int INVALID_URL_CODE = 1;
    public static final int ERROR_CODE = 2;
    protected static final int RESULT_RESPONSE = 2;

    public ContextIntentService()
    {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String context = intent.getStringExtra(CONTEXT_EXTRA);
        String ip = intent.getStringExtra(RESPONSE_SERVER_IP);

        HttpURLConnectionContext connect = new HttpURLConnectionContext();
        String response = connect.sendContext(context, ip);

        ResultReceiver rec = intent.getParcelableExtra(PENDING_RESULT_EXTRA);
        Bundle b = new Bundle();
        b.putString("Response", response);
        rec.send(RESULT_RESPONSE, b);
    }
}