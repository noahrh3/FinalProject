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
import android.widget.ImageView;
import android.graphics.BitmapFactory;
import android.content.ActivityNotFoundException;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.yalantis.ucrop.UCropFragment;
import com.yalantis.ucrop.UCropFragmentCallback;
import com.soundcloud.android.crop.Crop;
import android.os.Environment;
import org.w3c.dom.Text;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class MainActivity extends AppCompatActivity {
    private static final int UPLOAD_REQUEST_CODE = 13;
    private static final int EXTERNAL_STORAGE_REQUEST = 1;
    private static String[] STORAGE_PERMISSION = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            //Making TextView scrollable
            final TextView latexCode = findViewById(R.id.latex_code);
            latexCode.setMovementMethod(new ScrollingMovementMethod());
            //Adding Button Handlers
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
                    if (!Tasks.ApiIsWorking) {
                        setLatex();
                    }
                }
            });
            final Button uploadPhoto = findViewById(R.id.upload_photo);
            uploadPhoto.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final TextView defaultText = findViewById(R.id.default_text);
                    defaultText.setVisibility(View.INVISIBLE);
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
        startActivityForResult(intent, UPLOAD_REQUEST_CODE);
    }
    private void setLatex() {
        final TextView latexCode = findViewById(R.id.latex_code);
        try {
            String jsonString = new Tasks().execute(currentImageFile).get();
            latexCode.setText(JsonParser.getLatex(jsonString));
        } catch (Exception e) {
            Log.e("lol", "caught");
        }
    }

    private File currentImageFile = null;
    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent resultData) {

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == UPLOAD_REQUEST_CODE) {
            Uri currentImageURI = resultData.getData();
            beginCrop(currentImageURI);
            return;
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(resultData);
            Uri resultUri = result.getUri();
            ImageView imageView = findViewById(R.id.math_picture);
            imageView.setImageURI(resultUri);
            File imageFile = new File(resultUri.getPath());
            currentImageFile = imageFile;
        }
    }


    public static void checkStoragePermission(Activity mainActivity) {
        int hasPermission = ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(mainActivity, STORAGE_PERMISSION, EXTERNAL_STORAGE_REQUEST);
        }
    }


    private void beginCrop(Uri picUri) {
        CropImage.activity(picUri).start(this);
    }
    
}