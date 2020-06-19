package com.example.ebihartourism;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ebihartourism.Model.Places;
import com.example.ebihartourism.ViewHolder.PlaceViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class searchPlaceActivity extends AppCompatActivity {

    private Button search_btn;
    private EditText input_Text_search_Places;
    private RecyclerView search_list;
    private String search_input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_place);

        search_btn = findViewById(R.id.search_places_button);
        input_Text_search_Places = findViewById(R.id.search_Places);
        search_list = findViewById(R.id.search_list_recycler);
        search_list.setLayoutManager(new LinearLayoutManager(searchPlaceActivity.this));

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_input = input_Text_search_Places.getText().toString();
                        onStart();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Places");

        FirebaseRecyclerOptions<Places> options =
                new FirebaseRecyclerOptions.Builder<Places>()
                        .setQuery(reference.orderByChild("Name").startAt(search_input).endAt(search_input),Places.class)
                        .build();

        FirebaseRecyclerAdapter<Places,PlaceViewHolder> adapter =
                new FirebaseRecyclerAdapter<Places, PlaceViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull PlaceViewHolder placeViewHolder, int i, @NonNull final Places places) {
                        placeViewHolder.textPlaceName.setText(places.getName());
                        placeViewHolder.textPlaceSpecification.setText(places.getSpecification());
                        placeViewHolder.txtPlaceSpeciality.setText(places.getSpeciality());
                        Picasso.get().load(places.getImage()).into(placeViewHolder.imageView);

                        placeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(searchPlaceActivity.this,PlaceDetails.class);
                                intent.putExtra("pid", places.getPid());
                                startActivity(intent);
                            }

                        });
                    }

                    @NonNull
                    @Override
                    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.places_name_layout,parent,false);

                        PlaceViewHolder holder = new PlaceViewHolder(view);
                        return holder;
                    }
                };
        search_list.setAdapter(adapter);
        adapter.startListening();
    }
}
