package com.example.vinamra.anganwadi_supervisor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vinamra on 2/26/2018.
 */

public class UnverifiedHelpersListAdpater extends BaseAdapter {
    Context context;
    ArrayList<Helpers> helpersArrayList;
    private StorageReference httspRef;

    public UnverifiedHelpersListAdpater(Context context, ArrayList<Helpers> helpersArrayList) {
        this.context = context;
        this.helpersArrayList = helpersArrayList;
    }

    @Override
    public int getCount() {
        return helpersArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return helpersArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("LongLogTag")
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.unverifiedhelperslistelementlayout,parent,false);

        final CircleImageView avatarImage= (CircleImageView) view.findViewById(R.id.avatar_imageVerifyView);
        TextView helperId= (TextView) view.findViewById(R.id.helperIdVerifyView);
        TextView helperAvatarUrl= (TextView) view.findViewById(R.id.helperAvatarUrl);
        TextView nameTextView= (TextView) view.findViewById(R.id.nameTextVerifyView);
        TextView helperProofUrl= (TextView) view.findViewById(R.id.helperProofUrlVerifyView);

        //set helperimage
        //httspRef = FirebaseStorage.getInstance().getReferenceFromUrl(helpersArrayList.get(position).getPhotourl());
        // Load the image using Glide
        /****************************************/
        /*Glide.with(context)
                .load(helpersArrayList.get(position).getPhotourl())
                .asBitmap()
                .centerCrop()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        avatarImage.setImageBitmap(resource);
                    }
                });*/
        Picasso.get()
                .load(helpersArrayList.get(position).getPhotourl())
                .error(R.drawable.error)
                .into(avatarImage);
        /**************/

        helperId.setText(helpersArrayList.get(position).getHelperid());
        helperAvatarUrl.setText(helpersArrayList.get(position).getPhotourl());
        nameTextView.setText(helpersArrayList.get(position).getHelpername());
        helperProofUrl.setText(helpersArrayList.get(position).getIdproofurl());

        return view;

    }
}
