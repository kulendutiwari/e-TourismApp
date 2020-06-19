package com.example.ebihartourism;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AdminCategoryActivity extends AppCompatActivity {
    private ImageView tourist_places;
    private Button add_Bihar_btn,add_UP_btn;
    private TextView slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        tourist_places = (ImageView)findViewById(R.id.tourist_places_admin);
        add_Bihar_btn = (Button)findViewById(R.id.add_Bihar_btn);
        slogan = (TextView)findViewById(R.id.slogan_category);
        add_UP_btn = (Button) findViewById(R.id.add_UP_btn);

        add_Bihar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewDestinationActivity.class);
                intent.putExtra("category", "Bihar");
                startActivity(intent);
            }
        });

        add_UP_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewDestinationActivity.class);
                intent.putExtra("category", "UP");
                startActivity(intent);
            }
        });
    }
}

