package com.group7.actorbot_speech;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Owner on 11/3/2016.
 */
public class HttpURLConnectionContext
{

    public String sendContext(String c, String ip)
    {
        Log.i(getClass().getSimpleName(), "sendContext task: start");

        URL url;
        java.net.HttpURLConnection connection = null;

        StringBuffer response = new StringBuffer();
        try
        {
            c = c.replaceAll(" ", "%20");
            String url_text = String.format("http://%s/generate?phrase=%s", ip, c);
            url = new URL(url_text);

            Log.d(getClass().getSimpleName(), url_text);

            connection = (java.net.HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "ActorBot");
            int responseCode = connection.getResponseCode();
            Log.d(getClass().getSimpleName(), Integer.toString(responseCode));

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null)
            {
                response.append(inputLine);
            }
            in.close();

            //_txt_Response.setText(response.toString());
            Log.d(getClass().getSimpleName(), response.toString());

        }
        catch(Exception e)
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

        return response.toString();
    }
}
