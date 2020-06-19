package com.example.ebihartourism;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ebihartourism.HomeActivity1;
import com.example.ebihartourism.Model.Places;
import com.example.ebihartourism.Prevalent.Prevalent;
import com.example.ebihartourism.ViewHolder.PlaceViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

import static androidx.recyclerview.widget.RecyclerView.*;
public class HomeActivity extends AppCompatActivity implements HomeActivity1, NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private DatabaseReference PlaceRef;
    private RecyclerView recyclerView;
    public  LayoutManager layoutManager;
    private CircleImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        profile = findViewById(R.id.user_profile_image);
        PlaceRef = FirebaseDatabase.getInstance().getReference().child("Places");

        Paper.init(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView usernameTextView = headerView.findViewById(R.id.useProfile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

        usernameTextView.setText(Prevalent.currentOnlineUsers.getName());

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart(){
        super.onStart();

        FirebaseRecyclerOptions<Places> options = new FirebaseRecyclerOptions.Builder<Places>()
                .setQuery(PlaceRef,Places.class).build();

        FirebaseRecyclerAdapter<Places, PlaceViewHolder> adapter = new FirebaseRecyclerAdapter<Places, PlaceViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull PlaceViewHolder placeViewHolder, int i, @NonNull final Places places) {
                        placeViewHolder.textPlaceName.setText(places.getName());
                        placeViewHolder.textPlaceSpecification.setText(places.getSpecification());
                        placeViewHolder.txtPlaceSpeciality.setText(places.getSpeciality());
                        Picasso.get().load(places.getImage()).into(placeViewHolder.imageView);

                        placeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(HomeActivity.this,PlaceDetails.class);
                                intent.putExtra("pid", places.getPid());
                                startActivity(intent);
                            }

                        });
                    }

                    @NonNull
                    @Override
                    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.places_name_layout,parent,false);
                        PlaceViewHolder holder = new PlaceViewHolder(view);
                            return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }


    @Override

    public void onBackPressed() {
        DrawerLayout drawer = null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_places) {

        }
        else if (id == R.id.nav_contacts) {

        }
        else if (id == R.id.nav_search) {
            Intent intent = new Intent(HomeActivity.this,searchPlaceActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_settings) {
            Intent intent = new Intent(HomeActivity.this,settings.class);
            startActivity(intent);

        }

        else if (id == R.id.nav_logout) {

            Paper.book().destroy();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }

        else if (id == R.id.nav_getTrains_bw_stn_webViewMenu) {
            Intent intent = new Intent(HomeActivity.this, getTrains.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_safety_security) {
            Intent intent = new Intent(HomeActivity.this, safetyMeasures.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_feedback) {
            Intent intent = new Intent(HomeActivity.this, contactDeveloper.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
