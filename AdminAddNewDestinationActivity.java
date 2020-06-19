package com.example.ebihartourism;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewDestinationActivity extends AppCompatActivity {
    private String CategoryName,saveCurretnDate,saveCurrentTime,place_name,place_specifications,place_speciality,nearestRlyStn;
    private Button add_new_placeBtn;
    private ImageView inputImage_place;
    private EditText Inputplace_name,Inputplace_spec,Inputplace_speciality,InputNearestRlyStn;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String placeRandomKey,downloadImageUri;
    private StorageReference placeImageRef;
    private DatabaseReference PlaceRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_destination);

        CategoryName = getIntent().getExtras().get("category").toString();
        placeImageRef = FirebaseStorage.getInstance().getReference().child("Place Images");
        PlaceRef = FirebaseDatabase.getInstance().getReference().child("Places");

        InputNearestRlyStn = findViewById(R.id.place_nearest_rly_stn);
        add_new_placeBtn = (Button)findViewById(R.id.add_new_place);
        inputImage_place = (ImageView)findViewById(R.id.add_place_image_add);
        Inputplace_name = (EditText)findViewById(R.id.place_name);
        Inputplace_spec = (EditText)findViewById(R.id.place_specification);
        Inputplace_speciality = (EditText)findViewById(R.id.place_speciality);


        inputImage_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        add_new_placeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidatePlaceData();
            }
        });
    }

    private void OpenGallery(){
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null){
            ImageUri = data.getData();
            inputImage_place.setImageURI(ImageUri);
        }

    }

    private void ValidatePlaceData(){
        place_specifications = Inputplace_spec.getText().toString();
        place_speciality = Inputplace_speciality.getText().toString();
        place_name = Inputplace_name.getText().toString();
        nearestRlyStn = InputNearestRlyStn.getText().toString();

        if(ImageUri == null){
            Toast.makeText(this,"Place Image Required",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(place_specifications)){
            Toast.makeText(this,"Please write Place Specifications",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(place_speciality)){
            Toast.makeText(this,"Please write Place Speciality",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(place_name)){
            Toast.makeText(this,"Please write Place Name",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(nearestRlyStn)){
            Toast.makeText(this,"Please write Nearest Railway Station",Toast.LENGTH_LONG).show();
        }
        else{
            StorePlaceInfo();
        }
    }

    private void StorePlaceInfo() {

        final ProgressDialog loadingBar = new ProgressDialog(this);
        loadingBar.setTitle("Add New Place");
        loadingBar.setMessage("Please wait,We are adding the Place");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,YYYY");
        saveCurretnDate = currentDate.format(calendar.getTime());

        placeRandomKey = saveCurretnDate + saveCurrentTime;
        final StorageReference filePath = placeImageRef.child(ImageUri.getLastPathSegment() + placeRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminAddNewDestinationActivity.this,"Error: " + message,Toast.LENGTH_LONG).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewDestinationActivity.this, "Place Image Uploaded!", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadImageUri = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }

                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUri = task.getResult().toString();
                            Toast.makeText(AdminAddNewDestinationActivity.this, "Got The Place URL Successfully", Toast.LENGTH_SHORT).show();
                            SavePlaceInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SavePlaceInfoToDatabase(){

        final ProgressDialog loadingBar = new ProgressDialog(this);

        HashMap<String,Object> PlaceMap = new HashMap<>();
        PlaceMap.put("pid",placeRandomKey);
        PlaceMap.put("date",saveCurretnDate);
        PlaceMap.put("time",saveCurrentTime);
        PlaceMap.put("Specification",place_specifications);
        PlaceMap.put("Name",place_name);
        PlaceMap.put("Image",downloadImageUri);
        PlaceMap.put("Category",CategoryName);
        PlaceMap.put("Speciality",place_speciality);
        PlaceMap.put("Nearest_Railway",nearestRlyStn);

        PlaceRef.child(placeRandomKey).updateChildren((PlaceMap)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Intent intent_AddPlace = new Intent(AdminAddNewDestinationActivity.this,AdminCategoryActivity.class);
                    startActivity(intent_AddPlace);
                    loadingBar.dismiss();

                    Toast.makeText(AdminAddNewDestinationActivity.this, "Place is Added!", Toast.LENGTH_SHORT).show();

            }
                else{
                    loadingBar.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(AdminAddNewDestinationActivity.this, "Error: " + message , Toast.LENGTH_SHORT).show();
                }
        }
    });
}
}