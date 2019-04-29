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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import android.database.Cursor;
import android.provider.MediaStore;
import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.content.pm.PackageManager;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 13;
    public static Context contextOfApplication;
    public static Context getContextOfApplication() {
        return contextOfApplication;
    }
    private static final int EXTERMAL_STORAGE_REQUEST = 1;
    private static String[] STORAGE_PERMISSION = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        {
            contextOfApplication = getApplicationContext();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            final TextView latexCode = findViewById(R.id.latex_code);
            latexCode.setMovementMethod(new ScrollingMovementMethod());
            final Button copyButton = findViewById(R.id.copy_button);
            copyButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("LaTeX", latexCode.getText());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                }
            });
            final Button toLatexButton = findViewById(R.id.to_latex);
            toLatexButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                }
            });
            final Button uploadPhoto = findViewById(R.id.upload_photo);
            uploadPhoto.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    getImage();
                }
            });
        }
    }

    /**
     * called when upload photo button is pressed
     */
    private void getImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        checkStoragePermission(this);
        startActivityForResult(intent, REQUEST_CODE);
    }


    /**
     * finds true path of a file given the Uri
     * @param imageURI
     * @return path
     */
    public String getTruePath(Uri imageURI) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(imageURI, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int colI = cursor.getColumnIndex(filePathColumn[0]);
        String path = cursor.getString(colI);
        cursor.close();
        return path;
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent resultData) {

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        Uri currentImageURI = resultData.getData();
        if (requestCode != REQUEST_CODE) {
            return;
        }

        File imageFile = new File(getTruePath(currentImageURI));

        final TextView latexCode = findViewById(R.id.latex_code);
        try {
            latexCode.setText(new Tasks().execute(imageFile).get());
        } catch (Exception e) {
            Log.e("lol", "caught");
        }
    }


    public static void checkStoragePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            ActivityCompat.requestPermissions(activity, STORAGE_PERMISSION, EXTERMAL_STORAGE_REQUEST);
        }
    }

}
