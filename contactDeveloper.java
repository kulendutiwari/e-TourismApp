package com.example.ebihartourism;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
public class contactDeveloper extends AppCompatActivity {
    private Button submit_fbdk_btn;
    private EditText feedback_ed_name,feedback_ed_details,feedback_ed_city,feedback_UID_ed;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_developer);

        feedback_UID_ed = findViewById(R.id.feedback_UID_ed);
        feedback_ed_city = findViewById(R.id.feedback_city);
        feedback_ed_details = findViewById(R.id.feedback_details);
        feedback_ed_name = findViewById(R.id.feedback_name);
        submit_fbdk_btn = findViewById(R.id.feedback_submit_btn);

        loadingBar = new ProgressDialog(this);

        submit_fbdk_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeedback();
            }
        });

    }

    private void submitFeedback() {

        String name = feedback_ed_name.getText().toString();
        String details = feedback_ed_details.getText().toString();
        String city = feedback_ed_city.getText().toString();
        String feedbackUid =feedback_UID_ed.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Please Write UserName",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(feedbackUid)){
            Toast.makeText(this,"Please Write any random number in URN",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(details)) {
            Toast.makeText(this, "Please Write about problem", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(city)) {
            Toast.makeText(this, "Please Write City/State", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Submit Feedback");
            loadingBar.setMessage("Submitting...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateFeedback(name,feedbackUid,details,city);
        }
    }

    private void ValidateFeedback(final String name,final String feedback_UID, final String details,final String city) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Feedbacks").child(feedback_UID).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("URN",feedback_UID);
                    userdataMap.put("Details",details);
                    userdataMap.put("City",city);
                    userdataMap.put("Name",name);

                    RootRef.child("Feedbacks").child(feedback_UID).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(contactDeveloper.this,"Thanks for your feedaback!",Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(contactDeveloper.this,HomeActivity.class);
                                startActivity(intent);
                            }
                            else{
                                loadingBar.dismiss();
                                Toast.makeText(contactDeveloper.this,"Network Error! Please Try Again", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }


                else {
                    Toast.makeText(contactDeveloper.this, "We are already working on it!", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                    Toast.makeText(contactDeveloper.this,"Please write any other random number in URN field",Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(contactDeveloper.this,HomeActivity.class);
                    startActivity(intent);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
