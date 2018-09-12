package com.example.vinamra.anganwadi_supervisor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class adapter_for_child_list extends RecyclerView.Adapter<adapter_for_child_list.Adpter> {

Context context;
List<child_reg_list_class> list;

    public adapter_for_child_list(Context context, List<child_reg_list_class> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Adpter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_list_layout, parent, false);
        return new Adpter(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Adpter holder, int position) {
        child_reg_list_class obj=list.get(position);
        holder.id.setText(obj.getId());
        holder.name.setText(obj.getName());
        holder.fname.setText(obj.getFname());
        holder.weight.setText(obj.getWeight());
        holder.mname.setText(obj.getMname());
        holder.dob.setText(obj.getDob());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class  Adpter extends RecyclerView.ViewHolder{
        TextView id,name,fname,mname,weight,dob;
        ImageView img,child_img;
        LinearLayout l1,l2,l3,l4;
        public Adpter(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.name);
            fname= (TextView) itemView.findViewById(R.id.father);
            mname= (TextView) itemView.findViewById(R.id.mother);
            weight= (TextView) itemView.findViewById(R.id.weight);
            id= (TextView) itemView.findViewById(R.id.id);
            dob= (TextView) itemView.findViewById(R.id.dob);
            l1= (LinearLayout) itemView.findViewById(R.id.l1);
            l2= (LinearLayout) itemView.findViewById(R.id.l2);
            l3= (LinearLayout) itemView.findViewById(R.id.l3);
            l4= (LinearLayout) itemView.findViewById(R.id.l4);
            img= (ImageView) itemView.findViewById(R.id.img);
            child_img= (ImageView) itemView.findViewById(R.id.img_child);
            img.setTag("up");
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(img.getTag().equals("up")){
                        img.setTag("down");
                        img.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                        l1.setVisibility(View.VISIBLE);
                        l2.setVisibility(View.VISIBLE);
                        l3.setVisibility(View.VISIBLE);
                        l4.setVisibility(View.VISIBLE);
                        child_img.setVisibility(View.VISIBLE);
                        callImageDownload();
                    }else {
                        img.setTag("up");
                        img.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
                        l1.setVisibility(View.GONE);
                        l2.setVisibility(View.GONE);
                        l3.setVisibility(View.GONE);
                        l4.setVisibility(View.GONE);
                        child_img.setVisibility(View.GONE);
                    }
                }
            });

        }
        private void callImageDownload() {
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            StorageReference islandRef = mStorageRef.child("Child_images/"+id.getText().toString()+".jpg");

            final long ONE_MEGABYTE = 1024 * 1024;
            islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    child_img.setImageBitmap(Bitmap.createScaledBitmap(bmp, child_img.getWidth(),
                           child_img.getHeight(), false));
                    // Data for "images/island.jpg" is returns, use this as needed
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
    }

}
