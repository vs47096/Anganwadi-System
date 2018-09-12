package com.example.vinamra.anganwadi_supervisor;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vinamra on 2/28/2018.
 */

public class HelperDetailsViewActivity extends AppCompatActivity {
    private View viewImageDialogView;
    private ImageView dialogImageView;
    private FloatingActionButton closeViewImageDialogBtn;
    private Dialog viewImageDialog;
    private AlertDialog.Builder viewImageDialogBuilder;
    private CircleImageView avatarView;
    private TextView nameTextView;
    private TextView helperIdViewTextView;
    private ImageButton idProofViewBtn;
    private TextView contact1View;
    private TextView contact2View;
    private String helperid;
    private StorageReference httspRef;
    private String helperAvatarUrl;
    private String helperProofUrl;
    private String contact1;
    private String contact2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpersdetailsviewlayout);
        initialize();
    }

    private void initialize() {

        /**************View Image Dialog********/
        viewImageDialogView=getLayoutInflater().inflate(R.layout.alertdialoglayout_viewimage,null);
        dialogImageView= (ImageView) viewImageDialogView.findViewById(R.id.dialogImageView);
        closeViewImageDialogBtn= (FloatingActionButton) viewImageDialogView.findViewById(R.id.closeViewImageDialogBtn);
        closeViewImageDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewImageDialog.dismiss();
            }
        });
        viewImageDialogBuilder =new AlertDialog.Builder(HelperDetailsViewActivity.this);
        viewImageDialogBuilder.setView(viewImageDialogView);
        viewImageDialogBuilder.setCancelable(true);
        viewImageDialog=viewImageDialogBuilder.create();
        /**************************************/

        /**********************Initializing Views*************************/
        avatarView= (CircleImageView) findViewById(R.id.avatarView);
        nameTextView= (TextView) findViewById(R.id.nameTextView);
        contact1View= (TextView) findViewById(R.id.contact1View);
        contact2View= (TextView) findViewById(R.id.contact2View);
        helperIdViewTextView =(TextView)findViewById(R.id.helperIdViewTextView);
        idProofViewBtn= (ImageButton) findViewById(R.id.idProofViewBtn);
        idProofViewBtn.setAdjustViewBounds(true);
        idProofViewBtn.setScaleType(ImageButton.ScaleType.CENTER_CROP);
        /**************************************/

        /**********************Getting Intent Data*************************/
        Bitmap bitmap=getIntent().getParcelableExtra("avatarimage");
        avatarView.setImageBitmap(bitmap);
        Bundle bundle=getIntent().getExtras();
        helperid = bundle.getString("helperId");
        String helperName=bundle.getString("helperName");
        helperAvatarUrl=bundle.getString("helperAvatarUrl");
        helperProofUrl = bundle.getString("helperProofUrl");
        contact1 = bundle.getString("contact1");
        contact2 = bundle.getString("contact2");
        /******************************************************************/

        helperIdViewTextView.setText(helperid);
        nameTextView.setText(helperName);
        contact1View.setText(contact1);
        contact2View.setText(contact2);
        avatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Glide.with(HelperDetailsViewActivity.this)
                        .load(helperAvatarUrl)
                        .asBitmap()
                        .centerCrop()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                dialogImageView.setImageBitmap(resource);
                            }
                        });*/
                Picasso.get()
                        .load(helperAvatarUrl)
                        .error(R.drawable.error)
                        .into(dialogImageView);
                viewImageDialog.show();
            }
        });
        idProofViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogImageView.setImageDrawable(idProofViewBtn.getDrawable());
                viewImageDialog.show();
            }
        });

        //set proof image to imageviewBtn
        // Create a storage reference from our app
        //httspRef = FirebaseStorage.getInstance().getReferenceFromUrl(helperProofUrl);
        // Load the image using Glide
        /*Glide.with(HelperDetailsViewActivity.this)
                .load(helperProofUrl)
                .asBitmap()
                .placeholder(R.drawable.mountain_material_wallpaper)
                .fitCenter()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        idProofViewBtn.setImageBitmap(resource);
                    }
                });*/
        Picasso.get()
                .load(helperProofUrl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(idProofViewBtn);

    }

}
