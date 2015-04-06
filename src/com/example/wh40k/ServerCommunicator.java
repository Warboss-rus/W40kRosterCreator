package com.example.wh40k;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Urgak_000 on 05.04.2015.
 */
public class ServerCommunicator {

    private String url;

    public ServerCommunicator(String address) {
        url = address;
    }

    public void getCodexVersion(String fileName, final MyActivity activity){
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(url);

                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    final String output = EntityUtils.toString(httpEntity);

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.GetVersionComplete(Integer.parseInt(output));
                        }
                    });
                } catch (IOException e) {
                    Log.d("getCodexVersion", e.getMessage());
                }
            }
        });
    }

    public void downloadCodex(final String fileName, final Context ctx) {
        final URL codexUrl;
        try {
            codexUrl = new URL(url + "/" + fileName);
        }
        catch (MalformedURLException e) {
            Log.d("", e.getMessage());
            return;
        }
        new Thread(new Runnable() {
            public void run() {
                try {
                    URLConnection connection = codexUrl.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    FileOutputStream outputStream = ctx.openFileOutput(fileName, Context.MODE_PRIVATE);
                    byte[] buffer = new byte[1024]; // Adjust if you want
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1)
                    {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.close();
                    inputStream.close();
                    Toast.makeText(ctx, "Download complete", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Log.d("downloadCodex", e.getMessage());
                }
            }
        });
    }

    public void reportUserLogin(final Map<String, String> userData) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(url);

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(userData.size());
                    for(Map.Entry<String,String> data : userData.entrySet())
                    {
                        nameValuePairs.add(new BasicNameValuePair(data.getKey(), data.getValue()));
                    }
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpclient.execute(httppost);
                } catch (IOException e) {
                    Log.d("Server", e.getMessage());
                }
            }
        });
    }
}
