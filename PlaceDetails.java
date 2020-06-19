package com.example.ebihartourism;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ebihartourism.Model.Places;
import com.example.ebihartourism.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class PlaceDetails extends AppCompatActivity {

    private Button wishlist;
    private ImageView placeImage;
    private ElegantNumberButton ratingButton;
    private TextView placeName,placeDescription,placeSpeciality,nearestRlyStn;
    private String placeID = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        placeID = getIntent().getStringExtra("pid");

        nearestRlyStn = findViewById(R.id.placeDetails_nearest_rly_stn);
        wishlist = findViewById(R.id.add_place_wishlist_details);
        placeImage = findViewById(R.id.place_Image_details);
        ratingButton = findViewById(R.id.rating_button);
        placeDescription = findViewById(R.id.place_description_place_details);
        placeSpeciality = findViewById(R.id.place_speciality_place_details);
        placeName = findViewById(R.id.place_name_place_details);

        getPlaceDetails(placeID);

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToWishlist();
            }
        });

    }

    private void addingToWishlist() {
        String saveCurrentDate,saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd,yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());
        final DatabaseReference wishlistRef = FirebaseDatabase.getInstance().getReference().child("wishlist");

        final HashMap<String,Object> wishlistMap = new HashMap<>();
        wishlistMap.put("pid",placeID);
        wishlistMap.put("Name",placeName.getText().toString());
        wishlistMap.put("Specification",placeDescription.getText().toString());
        wishlistMap.put("Speciality",placeSpeciality.getText().toString());
        wishlistMap.put("Nearest_Railway",nearestRlyStn.getText().toString());
        wishlistMap.put("date",saveCurrentDate);
        wishlistMap.put("time",saveCurrentTime);
        wishlistMap.put("rating",ratingButton.getNumber());

        wishlistRef.child("User View").child(Prevalent.
                currentOnlineUsers.getPhone()).child("Places")
                .updateChildren(wishlistMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            wishlistRef.child("Admin View").child(Prevalent.
                                    currentOnlineUsers.getPhone()).child("Places")
                                    .updateChildren(wishlistMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(PlaceDetails.this, "Added to Wishlist!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(PlaceDetails.this,HomeActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    }
                });



    }

    private void getPlaceDetails(String placeID) {

        DatabaseReference placeRef = FirebaseDatabase.getInstance().getReference().child("Places");
        placeRef.child(placeID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Places places = dataSnapshot.getValue(Places.class);

                    assert places != null;
                    placeName.setText(places.getName());
                    placeSpeciality.setText(places.getSpecification());
                    placeDescription.setText(places.getSpeciality());
                    nearestRlyStn.setText(places.getNearest_Railway());
                    Picasso.get().load(places.getImage()).into(placeImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
