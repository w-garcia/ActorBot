package com.group7.actorbot_speech;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.ResultReceiver;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.params.HttpParams;
import org.w3c.dom.Text;

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
import java.util.HashMap;
import java.util.Locale;

public class CharacterActivity extends AppCompatActivity implements CustomResultReceiver.Receiver
{
    protected static final int RESULT_SPEECH = 1;
    protected static final int RESULT_RESPONSE = 2;

    private ImageButton _btn_Speak;
    private ListView _lsv_context;

    private ArrayList<String> _context_history = new ArrayList<>();
    private CustomResultReceiver _customResultReceiver;

    private ActorBotApplication ABApp;
    private Boolean first_response = true;
    private ArrayAdapter adapter;

    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        _context_history.add("Speak Now");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        _customResultReceiver = new CustomResultReceiver(new Handler());
        _customResultReceiver.setReceiver(this);

        ABApp = (ActorBotApplication) getApplication();

        adapter = new ArrayAdapter<String>(this, R.layout.context_listview, _context_history);
        _lsv_context = (ListView) findViewById(R.id.lvw_context);
        _lsv_context.setAdapter(adapter);

        _btn_Speak= (ImageButton) findViewById(R.id.imb_Speak);

        _btn_Speak.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try
                {
                    startActivityForResult(intent, RESULT_SPEECH);
                }
                catch (ActivityNotFoundException a)
                {
                    Toast t = Toast.makeText(getApplicationContext(), "Your device isn't supported.", Toast.LENGTH_SHORT);
                    t.show();
                }

            }
        });

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener()
        {
            @Override
            public void onInit(int status)
            {
                if (status != TextToSpeech.ERROR)
                {
                    tts.setLanguage(Locale.UK);
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

                    if (first_response)
                    {
                        _context_history.remove(0);
                        first_response = false;
                    }

                    _context_history.add(0, text.get(0));
                    adapter.notifyDataSetChanged();

                    String response_gen_ip = ABApp.getResponseServerIP();

                    // Start the response background service
                    Intent intent = new Intent(this, ContextIntentService.class);
                    intent.putExtra(ContextIntentService.CONTEXT_EXTRA, text.get(0));
                    intent.putExtra(ContextIntentService.PENDING_RESULT_EXTRA, _customResultReceiver);
                    intent.putExtra(ContextIntentService.RESPONSE_SERVER_IP, response_gen_ip);
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
            Intent intent = new Intent(CharacterActivity.this, SettingsActivity.class);
            CharacterActivity.this.startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData)
    {
        String response = resultData.getString("Response");
        _context_history.add(0, response);

        // Update colors of response cell
        TextView tv1 = (TextView) _lsv_context.getChildAt(0);
        tv1.setTextColor(Color.parseColor("#3abab5"));
        adapter.notifyDataSetChanged();

        // Start text to speech
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            ttsGreater21(response);
        }
        else
        {
            ttsUnder20(response);
        }
    }

    @SuppressWarnings("deprecation")
    private void ttsUnder20(String s)
    {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        tts.speak(s, TextToSpeech.QUEUE_ADD, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String s)
    {
        String utteranceId = this.hashCode() + "";
        tts.speak(s, TextToSpeech.QUEUE_ADD, null, utteranceId);
    }
}
