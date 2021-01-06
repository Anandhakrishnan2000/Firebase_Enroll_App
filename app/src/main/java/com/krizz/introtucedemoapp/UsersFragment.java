package com.krizz.introtucedemoapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class UsersFragment extends Fragment {

    RecyclerView usersRecyclerView;

    DatabaseReference databaseReference;
    RecyclerViewAdapter adapter;
    public static List<UserModel> userData = new ArrayList<>();


    public static UsersFragment getInstance(){
        UsersFragment usersFragment = new UsersFragment();
        return usersFragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_users,container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        usersRecyclerView = view.findViewById(R.id.usersRecyclerView);



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                List<UserModel> users = new ArrayList<>();
                while (dataSnapshots.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
                    UserModel user = dataSnapshotChild.getValue(UserModel.class);
                    Log.e("User",user.toString());
                    users.add(user);

                }

                // Check your arraylist size and pass to list view like
                if(users.size()>0)
                {
                    //fcmUsers arraylist pass to list view
                    userData.clear();
                    userData.addAll(users);
                    for(UserModel user : userData)
                        Log.e("UserData",user.toString());
                    Collections.reverse(userData);
                    adapter = new RecyclerViewAdapter(userData,getActivity());
                    usersRecyclerView.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    usersRecyclerView.setLayoutManager(linearLayoutManager);
                    usersRecyclerView.setAdapter(adapter);

                }else
                {
                    // Display dialog for there is no user available.
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;
    }
}
