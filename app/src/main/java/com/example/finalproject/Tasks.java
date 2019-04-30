package com.example.finalproject;

import android.util.Log;
import android.util.Base64;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import android.os.AsyncTask;

public class Tasks extends AsyncTask<File, Void, String> {
    static boolean ApiIsWorking = false;
    protected String doInBackground(File... imageFiles) {
        Log.e("API CHECK", "API CALLED");
        if (imageFiles.length <= 0) {
            return "you have to select a file bro";
        }
        File imageFile = imageFiles[0];
        if (imageFile == null) {
            return "need to select picture";
        }
        ApiIsWorking = true;
        try {
            //String base64 = compress(imageFile);

            FileInputStream fileInputStream = new FileInputStream(imageFile);

            byte[] bytes = new byte[(int) imageFile.length()];

            Log.e("checklength:", "image size = " + bytes.length);
            fileInputStream.read(bytes);
            String base64 = Base64.encodeToString(bytes, Base64.NO_WRAP);

            Log.e("check base64", base64);
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");

            RequestBody body = RequestBody.create(mediaType, String.format("{ \"url\" : \"data:image/jpeg;base64,%s\" }", base64));

            Request request = new Request.Builder()
                    .url("https://api.mathpix.com/v3/latex")
                    .addHeader("content-type", "application/json")
                    .addHeader("app_id", "nvj1300_gmail_com")
                    .addHeader("app_key", "92848d9f3f0f373434c3")
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            if (response == null) {
                return "SERVERS_DOWN";
            }
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                return "SERVERS_DOWN";
            }
            return responseBody.string();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "FILE_NOT_FOUND";
        } catch (IOException e) {
            e.printStackTrace();
            return "NO_INTERNET";
        } finally {
            ApiIsWorking = false;
        }
    }
}
