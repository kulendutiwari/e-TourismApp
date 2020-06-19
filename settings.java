package com.example.ebihartourism;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ebihartourism.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class settings extends AppCompatActivity {
    private CircleImageView profileImageView;
    private EditText settings_phone_no,settings_fullName,settings_change_address;
    private TextView settings_change_profile_pic,update_settings,close_settings;

    private Uri imageUri;
    private String myUrl = "";
    private StorageReference storageProfilePicReference;
    private String checker = "";
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settings_change_profile_pic = findViewById(R.id.settings_change_profile_pic);
        profileImageView = findViewById(R.id.settings_profileImage);
        settings_phone_no = findViewById(R.id.settings_phone_no);
        settings_fullName = findViewById(R.id.settings_fullName);
        settings_change_address = findViewById(R.id.settings_change_address);
        update_settings = findViewById(R.id.update_Settings);
        close_settings = findViewById(R.id.close_settings);

        storageProfilePicReference = FirebaseStorage.getInstance().getReference().child("Profile");

        userInfoDisplay(profileImageView,settings_fullName,settings_phone_no,settings_change_address);

        close_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        update_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checker.equals("clicked")){
                    userInfoSaved();
                }

                else{
                    updateOnlyUserInfo();
                }

            }
        });
        
        settings_change_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(settings.this);
            }
        });
    }

    private void updateOnlyUserInfo() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String,Object> userMap = new HashMap<>();
        userMap.put("name",settings_fullName.getText().toString());
        userMap.put("address",settings_change_address.getText().toString());
        userMap.put("phone",settings_phone_no.getText().toString());
        ref.child(Prevalent.currentOnlineUsers.getPhone()).updateChildren(userMap);

        startActivity(new Intent(settings.this,HomeActivity.class));
        Toast.makeText(settings.this, "Changes Applied Successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            profileImageView.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Error!,Try Again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(settings.this,settings.class));
            finish();
        }
    }

    private void userInfoSaved() {

        if(TextUtils.isEmpty(settings_fullName.getText().toString())){
        Toast.makeText(this, "Name is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(settings_change_address.getText().toString())){
            Toast.makeText(this, "Address is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(settings_phone_no.getText().toString())){
            Toast.makeText(this, "Phone is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked")){
            uploadImage();
        }

    }

    private void uploadImage() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please Wait,your request is being processed");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(imageUri!=null){
            final StorageReference filereference = storageProfilePicReference
                    .child(Prevalent.currentOnlineUsers.getPhone() + ".jpg");
        uploadTask = filereference.putFile(imageUri);
        uploadTask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
               if(!task.isSuccessful()){
                   throw task.getException();
               }

                return filereference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Uri downloadUrl = (Uri) task.getResult();
                    myUrl = downloadUrl.toString();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                    HashMap<String,Object> userMap = new HashMap<>();
                    userMap.put("name",settings_fullName.getText().toString());
                    userMap.put("address",settings_change_address.getText().toString());
                    userMap.put("phone",settings_phone_no.getText().toString());
                    userMap. put("image", myUrl);
                    ref.child(Prevalent.currentOnlineUsers.getPhone()).updateChildren(userMap);

                    progressDialog.dismiss();
                    startActivity(new Intent(settings.this,MainActivity.class));
                    Toast.makeText(settings.this, "Changes Applied Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(settings.this, "Error! Rtry Again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        }
        else{
            Toast.makeText(settings.this, "Failed! Retry", Toast.LENGTH_SHORT).show();
        }
    }


    private void userInfoDisplay(final CircleImageView profileImageView, final EditText settings_fullName, final EditText settings_phone_no, final EditText settings_change_address) {

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUsers.getPhone());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("image").exists())
                    {
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(profileImageView);
                        settings_fullName.setText(name);
                        settings_change_address.setText(address);
                        settings_phone_no.setText(phone);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
