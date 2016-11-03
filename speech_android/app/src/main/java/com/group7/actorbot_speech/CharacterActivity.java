package com.group7.actorbot_speech;

import android.content.ActivityNotFoundException;
import android.content.Intent;
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CharacterActivity extends AppCompatActivity
{
    protected static final int RESULT_SPEECH = 1;

    private ImageButton _btn_Speak;
    private TextView _txt_Result;

    private ArrayList<String> _context_history = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        _txt_Result = (TextView) findViewById(R.id.txt_Result);
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
                    Toast t = Toast.makeText(getApplicationContext(), "Your device isn't supported.",Toast.LENGTH_SHORT);
                    t.show();
                }

            }
        });
    }

    public void sendContext(String c)
    {
        Log.i(getClass().getSimpleName(), "sendContext task: start");

        URL url;
        HttpURLConnection connection = null;

        try
        {
            url = new URL("192.168.1.79:443");

            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setChunkedStreamingMode(0);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            OutputStream out = new BufferedOutputStream(connection.getOutputStream());




            InputStream in = connection.getInputStream();
            InputStreamReader isw = new InputStreamReader(in);
            int data = isw.read();
            while (data != -1)
            {
                char current = (char) data;
                data = isw.read();
                System.out.print(current);
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }
        }
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
                    sendContext(text.get(0));
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
}
