package com.example.vinamra.anganwadi_helpers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.panavtec.drawableview.DrawableView;
import me.panavtec.drawableview.DrawableViewConfig;

/**
 * Created by Vinamra on 3/24/2018.
 */

public class SignatureActivity extends AppCompatActivity {
    private static final String TAG ="Signature Activity";
    private DrawableView signatureDrawableView;
    private FloatingActionButton signatureDoneBtn,clearCanvas,undoStroke;
    private Bitmap bitmap=null;
    private String mCurrentPhotoPath="";
    public Uri sigantureFileUri=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        initialize();
    }

    private void initialize() {
        DisplayMetrics metrics = SignatureActivity.this.getResources().getDisplayMetrics();
        int screenwidth = metrics.widthPixels;
        int screenheight = metrics.heightPixels;

        signatureDrawableView=(DrawableView) findViewById(R.id.signatureDrawableView);
        DrawableViewConfig config = new DrawableViewConfig();
        config.setStrokeColor(getResources().getColor(android.R.color.holo_orange_light));
        config.setShowCanvasBounds(true); // If the view is bigger than canvas, with this the user will see the bounds (Recommended)
        config.setStrokeWidth(7.0f);
        config.setMinZoom(1.0f);
        config.setMaxZoom(3.0f);
        config.setCanvasHeight(screenheight);
        config.setCanvasWidth(screenwidth);
        signatureDrawableView.setConfig(config);
        undoStroke=(FloatingActionButton)findViewById(R.id.undoStroke);
        undoStroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signatureDrawableView.undo();
            }
        });
        clearCanvas=(FloatingActionButton) findViewById(R.id.clearCanvas);
        clearCanvas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signatureDrawableView.clear();
            }
        });
        signatureDoneBtn=(FloatingActionButton) findViewById(R.id.signatureDoneBtn);
        signatureDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    checkPermissionsAndExecute(SignatureActivity.this,8888);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**************************************Function for Checking Permission and Executing Code*********************************/
    private void checkPermissionsAndExecute(Context context, int code) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (code==8888)
            {
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    String[] permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permissions, code);
                }
                else if(ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    sendSignatureDataBack();
                }
            }

        }
        else
        {
            if(code==8888)
            {
                sendSignatureDataBack();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==2222)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                sendSignatureDataBack();
            }
            else
            {
                Toast.makeText(SignatureActivity.this,"Permission required for USING STORAGE are denied",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    /**************************************************Function End*************************************************************/

    /**************************************Function for storing sending Signature File URI***********************************/
    private void sendSignatureDataBack()
    {
        bitmap=signatureDrawableView.obtainBitmap();
        saveSigantureToFile(bitmap);
        if(sigantureFileUri!=null)
        {
            Intent i = new Intent();
            i.putExtra("signature",sigantureFileUri.toString());
            setResult(RESULT_OK,i);
            finish();
        }
        else if(sigantureFileUri==null)
        {
            Toast.makeText(SignatureActivity.this,"Signature File not Prepared",Toast.LENGTH_LONG).show();
        }
    }
    /**************************************************Function End*************************************************************/

    /**************************************Function for storing Signature in File*********************************************/
    private void saveSigantureToFile(Bitmap bmp)
    {
        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        }
        catch (IOException ex) {
            // Error occurred while creating the File
            return ;
        }
        // Continue only if the File was successfully created
        if (photoFile != null)
        {
            try {
                FileOutputStream out = new FileOutputStream(photoFile);
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (photoFile == null)
        {
            Toast.makeText(SignatureActivity.this,"FIle not created",Toast.LENGTH_SHORT).show();
        }
        sigantureFileUri = Uri.fromFile(new File(mCurrentPhotoPath));
    }
    /**************************************************Function End*************************************************************/

    /**************************************Function for creating Temp File******************************************/
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "signature_"+timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    /**************************************************Function End*************************************************************/

}
