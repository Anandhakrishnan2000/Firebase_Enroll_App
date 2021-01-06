package com.krizz.introtucedemoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<UserModel> userData;
    private Context context;
    DatabaseReference databaseReference;

    public RecyclerViewAdapter(List<UserModel> userData, Context context) {
        this.userData = userData;
        this.context = context;
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_user_row,parent,false);

        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        final UserModel user = userData.get(position);
        String fullName = user.getFirstName()+" "+user.getLastName();
        holder.userNameTv.setText(fullName);
        holder.genderTv.setText(user.getGender());
        holder.ageTv.setText(user.getAge());
        holder.placeTv.setText(user.getHomeTown());
        Glide.with(context).load(user.getImageUrl()).into(holder.profilePic);

        holder.deleteBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                databaseReference.child(user.getKey()).removeValue();
            }
        });


    }

    public void updateData(List<UserModel> data){
        userData.clear();
        userData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return userData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profilePic, deleteBtn;
        public TextView userNameTv,genderTv,ageTv,placeTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.profilepic);
            deleteBtn = itemView.findViewById(R.id.delete_btn);
            userNameTv = itemView.findViewById(R.id.tv_name);
            genderTv = itemView.findViewById(R.id.tv_gender);
            ageTv = itemView.findViewById(R.id.tv_age);
            placeTv = itemView.findViewById(R.id.tv_place);
        }
    }
}
