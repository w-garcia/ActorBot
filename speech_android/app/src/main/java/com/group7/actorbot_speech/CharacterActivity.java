package com.group7.actorbot_speech;

import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Handler;
import android.os.ResultReceiver;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.params.HttpParams;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.spec.ECField;
import java.util.ArrayList;

public class CharacterActivity extends AppCompatActivity implements CustomResultReceiver.Receiver
{
    protected static final int RESULT_SPEECH = 1;
    protected static final int RESULT_RESPONSE = 2;

    private ImageButton _btn_Speak;
    private TextView _txt_Result;
    private TextView _txt_Response;

    private ArrayList<String> _context_history = new ArrayList<>();
    private CustomResultReceiver _customResultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        _customResultReceiver = new CustomResultReceiver(new Handler());
        _customResultReceiver.setReceiver(this);

        _txt_Result = (TextView) findViewById(R.id.txt_Result);
        _txt_Response = (TextView) findViewById(R.id.txt_Response);
        _btn_Speak= (ImageButton) findViewById(R.id.imb_Speak);

        _btn_Speak.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try
                {
                    startActivityForResult(intent, RESULT_SPEECH);
                    _txt_Result.setText("");
                }
                catch (ActivityNotFoundException a)
                {
                    Toast t = Toast.makeText(getApplicationContext(), "Your device isn't supported.", Toast.LENGTH_SHORT);
                    t.show();
                }

            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case RESULT_SPEECH:
            {
                if (resultCode == RESULT_OK && null != data)
                {
                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    _txt_Result.setText(text.get(0));
                    _context_history.add(text.get(0));

                    // Start the response background service
                    Intent intent = new Intent(this, ContextIntentService.class);
                    intent.putExtra(ContextIntentService.CONTEXT_EXTRA, text.get(0));
                    intent.putExtra(ContextIntentService.PENDING_RESULT_EXTRA, _customResultReceiver);
                    startService(intent);
                }
                break;

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_character, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData)
    {
        String response = resultData.getString("Response");
        _txt_Response.setText(response);
    }
}
