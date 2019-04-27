package com.example.finalproject;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.util.Base64;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 13;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            final TextView latexCode = findViewById(R.id.latexCode);
            latexCode.setMovementMethod(new ScrollingMovementMethod());
            final Button copyButton = findViewById(R.id.copyButton);
            copyButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("LaTeX", latexCode.getText());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                }
            });
            final Button uploadPhoto = findViewById(R.id.uploadPhoto);
            uploadPhoto.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        getImage();
                    }
                }
            );
        }
    }

    /**
     * called when upload photo button is pressed
     */
    private void getImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT).addCategory(Intent.CATEGORY_OPENABLE).setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    File currentImageFile = null;
    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent resultData) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        Uri currentImageURI;
        if (requestCode != REQUEST_CODE) {
            return;
        }
        currentImageURI = resultData.getData();

        //loadPhoto(currentPhotoURI);
        currentImageFile = new File(currentImageURI.getPath());


    }

    /**
     * called when you want to convert picture to latex
     * @return latex string
     */
    public String makeApiCall() {
        if (currentImageFile == null) {
            return "You have to select a file bro";
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(currentImageFile);
            byte[] bytes = new byte[(int) currentImageFile.length()];
            fileInputStream.read(bytes);
            String base64 = Base64.encodeToString(bytes, Base64.NO_WRAP);


            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, String.format("{ \"src\" : \"data:image/jpeg;base64,{%s}\" }", base64));
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
            return response.toString();

        } catch (FileNotFoundException e) {
            return "Sure that file exists my dude?";

        } catch (IOException e) {
            return "our servers are weak af rn, sorry";
        }
    }


}
