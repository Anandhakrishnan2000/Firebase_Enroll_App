package com.krizz.introtucedemoapp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class EnrollFragment extends Fragment {

    private ImageView profileImg;
    private EditText firstName;
    private EditText lastName;
    private EditText age;
    private EditText gender;
    private EditText country;
    private EditText state;
    private EditText homeTown;
    private EditText phoneNumber;
    private EditText telephoneNumber;
    private Button addUserBtn;

    private String firstNameStr;
    private String lastNameStr;
    private String ageStr;
    private String genderStr;
    private String countryStr;
    private String stateStr;
    private String homeTownStr;
    private String phoneNumberStr;
    private String telephoneNumberStr;
    private String imageUrlStr;


    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog ;
    String imgFileExtension;


    public interface AddUserUpdate{
        public void onAddUserClick();
    }

    AddUserUpdate addUserUpdate;

    public static EnrollFragment getInstance() {
        EnrollFragment fragment = new EnrollFragment();
        return fragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        addUserUpdate = (AddUserUpdate) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_enroll,container, false);


        storageReference = FirebaseStorage.getInstance().getReference("Images");
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        firstName = view.findViewById(R.id.et_firstname);
        lastName = view.findViewById(R.id.et_lastname);
        age = view.findViewById(R.id.et_age);
        gender = view.findViewById(R.id.et_gender);
        country = view.findViewById(R.id.et_country);
        state = view.findViewById(R.id.et_state);
        homeTown = view.findViewById(R.id.et_town);
        phoneNumber = view.findViewById(R.id.et_phoneNumber);
        telephoneNumber = view.findViewById(R.id.et_telNumber);

        profileImg = view.findViewById(R.id.profilepicImgV);
        addUserBtn = view.findViewById(R.id.adduserBtn);

        progressDialog = new ProgressDialog(getActivity());

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);

            }
        });

        addUserBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                assignStrings();
                if(isValid()){
                    if (isPhoneNumberUnique()){
                        saveUser();
                    }
                }
            }
        });



        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();
            imgFileExtension = GetFileExtension(FilePathUri);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), FilePathUri);
                profileImg.setImageBitmap(bitmap);
                profileImg.setBackgroundColor(getResources().getColor(R.color.white));
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }


    private void saveUser() {
        if (FilePathUri != null) {

            progressDialog.setTitle("Uploading User Data...");
            progressDialog.show();
            final StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            storageReference2.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageUrlStr = uri.toString();
                                    String ImageUploadId = databaseReference.push().getKey();
                                    @SuppressWarnings("VisibleForTests")
                                    UserModel userData = new UserModel(firstNameStr,lastNameStr,ageStr,genderStr,countryStr,stateStr,homeTownStr,phoneNumberStr,telephoneNumberStr,imageUrlStr,ImageUploadId);
                                    databaseReference.child(ImageUploadId).setValue(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(), "User Added Successfully ", Toast.LENGTH_LONG).show();

                                            updateLayout();
                                            addUserUpdate.onAddUserClick();
                                        }
                                    });
                                }
                            });

                        }
                    });
        }
        else {

            Toast.makeText(getActivity(), "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }

    private void updateLayout() {
        FilePathUri = null;
        Glide.with(getActivity()).load(getResources().getDrawable(R.drawable.ic_baseline_image_24)).into(profileImg);
        firstName.getText().clear();
        lastName.getText().clear();
        age.getText().clear();
        gender.getText().clear();
        country.getText().clear();
        state.getText().clear();
        homeTown.getText().clear();
        phoneNumber.getText().clear();
        telephoneNumber.getText().clear();
    }


    public void assignStrings(){

        firstNameStr = firstName.getText().toString();
        lastNameStr = lastName.getText().toString();
        ageStr = age.getText().toString();
        genderStr = gender.getText().toString();
        countryStr = country.getText().toString();
        stateStr = state.getText().toString();
        homeTownStr = homeTown.getText().toString();
        phoneNumberStr = phoneNumber.getText().toString();
        telephoneNumberStr = telephoneNumber.getText().toString();

    }

    public boolean isValid(){
        boolean flag = true;
        if (TextUtils.isEmpty(firstNameStr)){
            firstName.setError("Please enter first name");
            flag = false;
        }
        if (TextUtils.isEmpty(lastNameStr)){
            lastName.setError("Please enter last name");
            flag = false;
        }
        if (TextUtils.isEmpty(ageStr)){
            age.setError("Please enter age");
            flag = false;
        }
        if (TextUtils.isEmpty(genderStr)){
            gender.setError("Please enter gender");
            flag = false;
        }
        if (TextUtils.isEmpty(countryStr)){
            country.setError("Please enter country");
            flag = false;
        }
        if (TextUtils.isEmpty(stateStr)){
            state.setError("Please enter state");
            flag = false;
        }
        if (TextUtils.isEmpty(homeTownStr)){
            homeTown.setError("Please enter home town");
            flag = false;
        }
        if (TextUtils.isEmpty(phoneNumberStr)){
            phoneNumber.setError("Please enter phone number");
            flag = false;
        }
        if (TextUtils.isEmpty(telephoneNumberStr)){
            telephoneNumber.setError("Please enter telephone number");
            flag = false;
        }
        if (FilePathUri == null){
            Toast.makeText(getActivity(),"Please insert a profile picture",Toast.LENGTH_SHORT).show();
            flag = false;
        }

        return flag;
    }

    public boolean isPhoneNumberUnique(){

        boolean flag = true;

        for (UserModel user : UsersFragment.userData){
            if (phoneNumberStr.equals(user.getPhoneNumber()))
            {
                flag = false;
                Toast.makeText(getActivity(),"Phone Number must be unique",Toast.LENGTH_LONG).show();
            }
            if (telephoneNumberStr.equals(user.getPhoneNumber()))
            {
                flag= false;
                Toast.makeText(getActivity(),"Telephone Number must be unique",Toast.LENGTH_LONG).show();
            }

        }
        return flag;

    }


}
