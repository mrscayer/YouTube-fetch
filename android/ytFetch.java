package com.kcyoutubefetch;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class YoutubeFetch extends ReactContextBaseJavaModule {

    public YoutubeFetch(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "YoutubeFetch";
    }

    @ReactMethod
    public void fetchData(String url, ReadableMap headers, String body, Promise promise) {
        new Thread(() -> {
            try {
                URL urlObj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
                conn.setRequestMethod("POST");
                
                // Set headers
                ReadableMapKeySetIterator iterator = headers.keySetIterator();
                while (iterator.hasNextKey()) {
                    String key = iterator.nextKey();
                    conn.setRequestProperty(key, headers.getString(key));
                }

                // Set body
                conn.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(body);
                writer.flush();
                writer.close();

                int responseCode = conn.getResponseCode();
                StringBuilder response = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                promise.resolve(response.toString());
            } catch (Exception e) {
                promise.reject("ERROR", e);
            }
        }).start();
    }
}
