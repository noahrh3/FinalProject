package com.example.finalproject;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import android.os.AsyncTask;
import java.net.URI;
import java.nio.file.Files;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;

public class Tasks extends AsyncTask<File, Void, String> {

    private String compress(File imageFile) {
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.NO_WRAP);
        Log.e("CHECK LENGTH", Integer.toString(byteArray.length));
        return encoded;
    }
    private String compress(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.NO_WRAP);
        Log.e("CHECK LENGTH", Integer.toString(byteArray.length));
        return encoded;
    }

    protected String doInBackground(File... imageFiles) {
        if (imageFiles.length <= 0) {
            return "you have to select a file bro";
        }
        File imageFile = imageFiles[0];
        if (imageFile == null) {
            return "need to select picture";
        }

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
                return "Sorry fam, our servers are not lit rn.";
            }
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                return "fug";
            }
            return responseBody.string();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "Sure that file exists my dude?";
        } catch (IOException e) {
            e.printStackTrace();
            return "check your internet man";
        }
    }
}
