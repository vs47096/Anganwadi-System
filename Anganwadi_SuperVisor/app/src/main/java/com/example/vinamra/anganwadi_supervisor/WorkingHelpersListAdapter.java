package com.example.vinamra.anganwadi_supervisor;

import android.content.Context;
import android.graphics.Bitmap;
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
 * Created by Vinamra on 2/28/2018.
 */

public class WorkingHelpersListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Helpers> helpersArrayList;
    StorageReference httspRef;

    public WorkingHelpersListAdapter(Context context, ArrayList<Helpers> helpersArrayList) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.workinghelperslistelementlayout,parent,false);

        final CircleImageView avatarImage= (CircleImageView) view.findViewById(R.id.avatar_image);
        TextView helperId= (TextView) view.findViewById(R.id.helperId);
        TextView helperAvatarUrl= (TextView) view.findViewById(R.id.helperAvatarUrl);
        TextView nameTextView= (TextView) view.findViewById(R.id.nameTextView);
        TextView helperProofUrl= (TextView) view.findViewById(R.id.helperProofUrl);
        TextView contact1TextView= (TextView) view.findViewById(R.id.contact1TextView);
        TextView contact2TextView= (TextView) view.findViewById(R.id.contact2TextView);

        //set helperimage
        //httspRef = FirebaseStorage.getInstance().getReferenceFromUrl(helpersArrayList.get(position).getPhotourl());
        // Load the image using Glide
        /*Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(httspRef)
                .into(avatarImage);*/
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
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.error)
                .into(avatarImage);
         /**************/

        helperId.setText(helpersArrayList.get(position).getHelperid());
        helperAvatarUrl.setText(helpersArrayList.get(position).getPhotourl());
        nameTextView.setText(helpersArrayList.get(position).getHelpername());
        helperProofUrl.setText(helpersArrayList.get(position).getIdproofurl());
        contact1TextView.setText(helpersArrayList.get(position).getContact1());
        contact2TextView.setText(helpersArrayList.get(position).getContact2());

        return view;
    }
}
