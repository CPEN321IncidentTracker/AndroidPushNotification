package com.example.locationtest;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class UploadTokensToServer {
    private static final String TAG = "UploadTokensToServer";

    public String uploadToServer(String query, String tokens) throws JSONException, IOException {
        URL url = new URL(query);
        JSONObject tokens_json = new JSONObject(tokens);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");

        OutputStream os = conn.getOutputStream();
        os.write(tokens.getBytes());
        os.flush();
        os.close();
        conn.disconnect();

        Log.d(TAG,"The token is uploaded to server!");
        return TAG;
    }
}
