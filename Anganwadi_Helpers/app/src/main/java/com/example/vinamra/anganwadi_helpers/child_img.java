package com.example.vinamra.anganwadi_helpers;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import static android.app.AlertDialog.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class child_img extends Fragment {
    private static final int RESULT_OK = 1;
    TextView select,smile,text,title;
    Uri path= Uri.parse("");
    Button upload;
    private final int IMG_REQUEST=1;
    private Bitmap bitmap;
    ImageView check,pic;
    child_reg child_reg;
    private final int IMG_REQUEST2=2;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private StorageReference mStorageRef;
    private static final int PERMISSION_REQUEST_CODE2 = 220;

    public child_img() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_child_img, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        select= (TextView) getView().findViewById(R.id.select);
        smile= (TextView) getView().findViewById(R.id.smile);
        upload= (Button) getView().findViewById(R.id.upload_button);
        pic= (ImageView) getView().findViewById(R.id.photo);
        check= (ImageView) getView().findViewById(R.id.check);
        title= (TextView) getView().findViewById(R.id.title);
        text= (TextView) getView().findViewById(R.id.text_show);

        child_reg=new child_reg();
        child_reg.next= (Button) getActivity().findViewById(R.id.next);
     //   d=new Driver_sign_in();
     //   view=getView().findViewById(R.id.elastic_download_view);
        mStorageRef = FirebaseStorage.getInstance().getReference();
      //  d.next=getActivity().findViewById(R.id.next);
      //  sessionManager=new SessionManager(getContext());
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallListdialog();
                smile.setVisibility(View.GONE);
                select.setVisibility(View.GONE);
                upload.setVisibility(View.VISIBLE);
            }
        });
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pic.getTag().equals("pic")){
                    CallListdialog();
                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pic.getTag().equals("upload")){
                    Toast.makeText(getContext(),"Please Select an image First", Toast.LENGTH_LONG).show();}
                else
                {
                    uploadImage();
                }
            }
        });
    }
    private void uploadImage() {
        final ProgressDialog loading=ProgressDialog.show(getContext(),"Uploading....","please wait",false,false);

        // Create file metadata including the content type
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();

        /*************Setting Child Preferences*****************/
        final SharedPreferences regPref = getContext().getSharedPreferences("registration_child", Context.MODE_PRIVATE);
        String chlidId=regPref.getString("childId",null);

        StorageReference riversRef = mStorageRef.child("Child_images/"+chlidId+".jpg");
      //  view.setVisibility(View.VISIBLE);
      //  view.startIntro();
        title.setVisibility(View.GONE);
        upload.setVisibility(View.GONE);
        pic.setVisibility(View.GONE);
      //  view.setProgress(20);
        riversRef.putFile(path, metadata)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        loading.dismiss();
                        // Get a URL to the uploaded content
                        // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                      //  view.success();
                        SharedPreferences.Editor supervisorPrefEditor = regPref.edit();
                        supervisorPrefEditor.clear();
                        supervisorPrefEditor.commit();
                        Toast.makeText(getContext(),"Image Uploaded Successfully",Toast.LENGTH_LONG).show();
                        check.setVisibility(View.VISIBLE);
                       // view.setVisibility(View.GONE);
                        text.setVisibility(View.VISIBLE);
                    //    child_reg.next.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                      //  view.fail();
                        loading.dismiss();
                        Toast.makeText(getContext(),"An Error Occur",Toast.LENGTH_LONG).show();
                        upload.setVisibility(View.VISIBLE);
                        pic.setVisibility(View.VISIBLE);
                        // ...
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                        .getTotalByteCount());
            //    view.setProgress((int)progress);
            }
        });
    }


    public  void CallListdialog(){
        Builder builderSingle = new Builder(getContext());
        builderSingle.setTitle("Select One Option");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("SELECT FROM GALLERY");
        arrayAdapter.add("TAKE A PICTURE");

        builderSingle.setNegativeButton("cancel", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                if(strName.equalsIgnoreCase("SELECT FROM GALLERY"))
                    selectImage();
                else openCamera();
            }
        });
        builderSingle.show();
    }
    private void openCamera() {
        requestPermission();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, IMG_REQUEST2);
    }

    private void selectImage(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode == IMG_REQUEST) && (data != null)){
            path=data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),path);
                pic.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else  if((requestCode == IMG_REQUEST2) && (data != null)){
            bitmap = (Bitmap) data.getExtras().get("data");
            //  Toast.makeText(ImageUploadActivity.this,data.toString(),Toast.LENGTH_SHORT).show();
            pic.setImageBitmap(bitmap);

        } pic.setTag("pic");
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity()
                , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
    }
}
