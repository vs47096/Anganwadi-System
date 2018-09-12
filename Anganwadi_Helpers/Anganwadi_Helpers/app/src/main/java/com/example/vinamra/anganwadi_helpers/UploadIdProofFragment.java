package com.example.vinamra.anganwadi_helpers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import is.arontibo.library.ElasticDownloadView;

/**
 * Created by Vinamra on 10/5/2017.
 */

public class UploadIdProofFragment extends Fragment {
    private ImageButton uploadProofBtn;
    private static final int REQUEST_CAMERA =1234 ;
    private static final int SELECT_FILE =5678 ;
    private String mCurrentPhotoPath="",TAG="UploadIdProofFragment";
    private Uri uplaodFileURI;
    private String helperId,idProofUpdateURL;
    private StorageReference mStorageRef;
    private ElasticDownloadView elasticDownloadView;
    private boolean imageSetToIdProofImageView=false;
    private ImageView idProofImageView,expandedImageView,alertDialogImageView;
    private LinearLayout expanded_image_Parent,elastic_download_view_parent;
    private AlertDialog.Builder idProofViewAlertBuild;
    private  View alertLayoutForIdProofView;
    private FloatingActionButton idPhotoCorrectBtn,idPhotoCrossBtn;
    private AlertDialog idProofViewAlert;
    private Bitmap imageBitmap = null;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_uploadproof,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences regPref = getContext().getSharedPreferences("registration_preferences", Context.MODE_PRIVATE);
        helperId=regPref.getString("helperId",null);

        viewPager=(ViewPager) getActivity().findViewById(R.id.viewPager);
        uploadProofBtn= (ImageButton) view.findViewById(R.id.uploadProofBtn);
        uploadProofBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooserDialog();
            }
        });
        expanded_image_Parent= (LinearLayout) getActivity().findViewById(R.id.expanded_image_Parent);
        expandedImageView= (ImageView) getActivity().findViewById(R.id.expanded_image);
        alertLayoutForIdProofView=getActivity().getLayoutInflater().inflate(R.layout.alertdialoglayoutforidproofview,null);
        idProofViewAlertBuild =new AlertDialog.Builder(getContext());
        idProofViewAlertBuild.setView(alertLayoutForIdProofView);
        idProofViewAlertBuild.setCancelable(false);
        idProofViewAlert=idProofViewAlertBuild.create();
        idPhotoCorrectBtn= (FloatingActionButton) alertLayoutForIdProofView.findViewById(R.id.idPhotoCorrectBtn);
        idPhotoCorrectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPhotoAndUpload();
            }
        });
        idPhotoCrossBtn= (FloatingActionButton) alertLayoutForIdProofView.findViewById(R.id.idPhotoCrossBtn);
        idPhotoCrossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idProofViewAlert.dismiss();
            }
        });
        alertDialogImageView= (ImageView) alertLayoutForIdProofView.findViewById(R.id.alertDialogImageView);
        idProofImageView= (ImageView) view.findViewById(R.id.idProofImageView);
        idProofImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewIdProofImage();
            }
        });
        // Create a storage reference from our app
        mStorageRef = FirebaseStorage.getInstance().getReference();

    }

    /*********************************************Function for viewing avatar***************************************************/
    private void viewIdProofImage() {
        getActivity().findViewById(R.id.appbar).setVisibility(View.INVISIBLE);
        expanded_image_Parent.setVisibility(View.VISIBLE);
        expandedImageView.setVisibility(View.VISIBLE);
    }
    /**************************************************Function End*************************************************************/

    /**************************************Function for Showing First Image Chooser******************************************/
    private void openImageChooserDialog() {

        final CharSequence[] items = { "Take Photo", "Choose from Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Upload your Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo"))
                {
                    try {
                        checkPermissionsAndExecute(getContext(),2222);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (items[item].equals("Choose from Gallery"))
                {
                    try {
                        checkPermissionsAndExecute(getContext(),3333);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        builder.show();
    }
    /**************************************************Function End*************************************************************/

    /**************************************Function for Checking Permission and Executing Code*********************************/
    private void checkPermissionsAndExecute(Context context, int code) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (code==2222)
            {
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    String[] permissions = {android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permissions, code);
                }
                else if(ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    cameraIntent();
                }
                else
                {
                    Toast.makeText(getContext(),"Some permission for camera may be missing",Toast.LENGTH_LONG);
                }
            }
            else if(code==3333)
            {
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    String[] permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permissions, code);
                }
                else if(ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    galleryIntent();
                }
                else
                {
                    Toast.makeText(getContext(),"Some permission for gallery may be missing",Toast.LENGTH_LONG);
                }
            }
        }
        else
        {
            if(code==2222)
            {
                cameraIntent();
            }
            else if(code==3333)
            {
                galleryIntent();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==2222)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                try
                {
                    cameraIntent();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(getContext(),"Permissions required for USING CAMERA are denied",Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode==3333)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                galleryIntent();
            }
            else
            {
                Toast.makeText(getContext(),"Permission for ACCESSING GALLERY are denied",Toast.LENGTH_LONG).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    /**************************************************Function End*************************************************************/

    /************************************************Function for Camera Intent*************************************************/
    private void cameraIntent() throws IOException
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null)
        {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            }
            catch (IOException ex) {
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null)
            {
                Uri photoURI = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", createImageFile());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }
    /**************************************************Function End*************************************************************/

    /************************************************Function for Gallery Intent*************************************************/
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }
    /**************************************************Function End*************************************************************/

    /**********************************************Function for Handling Intent Result*****************************************/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult();
        }
    }
    /**************************************************Function End*************************************************************/

    /**********************************************Function for Handling Image from Gallery*****************************************/
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        //Bitmap bm = null;
        if (data != null) {
            try {
                uplaodFileURI = data.getData();
                imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uplaodFileURI);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        alertDialogImageView.setImageBitmap(imageBitmap);
        idProofViewAlert.show();
        //expandedImageView.setImageBitmap(imageBitmap);
        imageSetToIdProofImageView = true;
    }
    /**************************************************Function End*************************************************************/

    /**********************************************Function for Handling Image from Camera*****************************************/
    private void onCaptureImageResult() {
        //Bitmap bm = BitmapFactory.decodeFile(mCurrentPhotoPath);
        imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        uplaodFileURI = Uri.fromFile(new File(mCurrentPhotoPath));
        alertDialogImageView.setImageBitmap(imageBitmap);
        idProofViewAlert.show();
        //expandedImageView.setImageBitmap(imageBitmap);
        imageSetToIdProofImageView=true;
    }
    /**************************************************Function End*************************************************************/

    /**************************************Function for creating File for storing Image******************************************/
    private File createImageFile() throws IOException {
        // Create an image file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //String imageFileName = "JPEG_" + timeStamp + "_";
        String imageFileName = "helperidproof";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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

    /**************************************Function for adding clicked image to gallery******************************************/
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }
    /**************************************************Function End*************************************************************/

    /**************************************Function for Checking name and Photo and Upload******************************************/
    private void checkPhotoAndUpload() {

            Log.d(TAG,"All fine");
            Log.d(TAG,"Image Clicked path is "+mCurrentPhotoPath);
            if(isNetworkConnected (getActivity()))
            {
                idProofViewAlert.dismiss();
                //Upload file URI to be provided for firebase upload
                //Uri file = Uri.fromFile(new File(mCurrentPhotoPath));

                // Create file metadata including the content type
                StorageMetadata metadata = new StorageMetadata.Builder()
                        .setContentType("image/jpeg")
                        .build();

                // Upload file and metadata to the path 'images/mountains.jpg'
                UploadTask uploadTask = mStorageRef.child("Anganwadi_Helper_images/helper_proof/"+ helperId +".jpg").putFile(uplaodFileURI, metadata);

                //getting layour for alert builder
                View alertLayoutView=getActivity().getLayoutInflater().inflate(R.layout.alertdialoglayoutforupload,null);

                AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
                //alert.setTitle("Uploading");
                alert.setView(alertLayoutView);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(false);
                /*alert.setPositiveButton("DONE", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Photo Uploaded, Thank You ;-)", Toast.LENGTH_SHORT).show();
                    }
                });*/
                final FloatingActionButton doneFabBtn= (FloatingActionButton) alertLayoutView.findViewById(R.id.doneFabBtn);
                doneFabBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#43A047")));
                final ElasticDownloadView et= (ElasticDownloadView) alertLayoutView.findViewById(R.id.elastic_download_view);
                final AlertDialog dialog = alert.create();
                doneFabBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        doneFabBtn.setVisibility(View.INVISIBLE);
                        expandedImageView.setImageBitmap(imageBitmap);
                        idProofImageView.setVisibility(View.VISIBLE);
                        idProofImageView.setImageBitmap(imageBitmap);
                        Toast.makeText(getContext(),"Your ID Proof Uploaded, Thank You ;-)",Toast.LENGTH_LONG).show();
                    }
                });
                dialog.show();


                //elasticDownloadView.startIntro();
                et.startIntro();
                //elasticDownloadView.setProgress(0);
                et.setProgress(0);
                // Listen for state changes, errors, and completion of the upload.
                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        System.out.println("Upload is " + progress + "% done");
                        //elasticDownloadView.setProgress((float)progress);
                        et.setProgress((float)progress);
                        Log.d(TAG,"Upload is " + progress + "% done");
                    }
                }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                        System.out.println("Upload is paused");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        //elasticDownloadView.fail();
                        //elastic_download_view_parent.setVisibility(View.INVISIBLE);
                        et.fail();
                        doneFabBtn.setVisibility(View.VISIBLE);
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Handle successful uploads on complete
                        //elasticDownloadView.success();
                        //elastic_download_view_parent.setVisibility(View.INVISIBLE);
                        et.success();
                        doneFabBtn.setVisibility(View.VISIBLE);

                        //set image to image view to show
                        //idProofImageView.setImageBitmap(imageBitmap);



                        Toast.makeText(getContext(),"Success Upload",Toast.LENGTH_LONG).show();
                        Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                        Log.d(TAG,"Download URL-"+downloadUrl);


                        /*Push data to mysql database with avatar url specfying helper`s id*/
                        idProofUpdateURL="https://aproject2018.000webhostapp.com/loginFiles/helper/updateproofurl.php?helperuserid="+helperId+"&fileurl="+downloadUrl;
                        StringRequest request = new StringRequest(Request.Method.GET, idProofUpdateURL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                urlSaveParseJSONResponse(response);
                                //registrationLoginBtn.setProgress(0);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if(!isNetworkConnected(getActivity()))
                                {
                                    Toast.makeText(getActivity(),"Sorry,Check your Internet Connection",Toast.LENGTH_LONG).show();
                                    //registrationLoginBtn.setProgress(0);
                                }
                            }
                        });

                        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                        requestQueue.add(request);


                    }
                });
            }
            else
            {
                Toast.makeText(getContext(),"Check your Internet Connection",Toast.LENGTH_LONG).show();
            }

    }
    /**************************************************Function End*************************************************************/

    private void urlSaveParseJSONResponse(String response) {

        try
        {

            JSONObject mainObject = new JSONObject(response);
            JSONArray records = mainObject.getJSONArray("data");

            JSONObject jsonObject = records.getJSONObject(0);
            String saveStatus = jsonObject.getString("done");
            if(saveStatus.equals("true"))
            {
                Toast.makeText(getActivity(),"Photo URL and name saved successfully",Toast.LENGTH_SHORT).show();

                String updateRegStatusUrl = "https://aproject2018.000webhostapp.com/loginFiles/helper/updateregstatus.php?helperuserid="+helperId+"&registrationstatus=NULL&setregistrationcompleted=yes";
                StringRequest updateRegStatusRequest = new StringRequest(Request.Method.GET, updateRegStatusUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //chngHelperPwdBtn.setProgress(0);
                        updateRegStatusJSON(response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //chngHelperPwdBtn.setProgress(0);
                        if(isNetworkConnected (getActivity()))
                        {
                            Toast.makeText(getActivity(),"Sorry,Check your Internet Connection",Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Sorry, Some Error occured while updating registration status",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(updateRegStatusRequest);

            }
            else if(saveStatus.equals("false"))
            {
                //*****User ID is incorrect*****//
                //registrationLoginBtn.setProgress(0);
                Toast.makeText(getActivity(),"Error while saving proof url in database",Toast.LENGTH_SHORT).show();
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**************************************************Parsing of updateRegistration***********************************************/
    private void updateRegStatusJSON(String response) {
        try {
            JSONObject mainObject = new JSONObject(response);
            JSONArray records = mainObject.getJSONArray("data");

            JSONObject jsonObject = records.getJSONObject(0);
            String updatestatus = jsonObject.getString("done");
            if(updatestatus.equals("true"))
            {
                //registration status update success

                Toast.makeText(getActivity(),"Registration Status Updated and Registration COmpleted",Toast.LENGTH_SHORT).show();


                //Move to next Fragment
                viewPager.setCurrentItem(5, true);


            }
            else if(updatestatus.equals("false"))
            {
                //registration status update failed

                Toast.makeText(getActivity(),"Registration Status failed to Update",Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**************************************************Function End*************************************************************/

    /**************************************Function for Checking Connection to Internet******************************************/
    public boolean isNetworkConnected (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if(netInfos != null)
            {
                return netInfos.isConnected();
            }
        }
        return false;
    }
    /**************************************************Function End*************************************************************/
}
