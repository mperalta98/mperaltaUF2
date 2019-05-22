package com.example.crisp.mperaltauf2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DisplayCard extends AppCompatActivity {
    public TextView display_title;
    public TextView display_description;
    public Button exit;
    public ImageView image_display;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_card);

        exit = findViewById(R.id.btn_display_exit);
        display_title = findViewById(R.id.display_title);
        display_description = findViewById(R.id.display_description);
        image_display = findViewById(R.id.display_img);

        Intent intent = getIntent();
        String id = intent.getExtras().getString("id");

        mRef = FirebaseDatabase.getInstance().getReference().child("Cards").child(id);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Card card = dataSnapshot.getValue(Card.class);
                if(card != null){
                    String title = card.title;
                    String description = card.description;
                    display_title.setText(title);
                    display_description.setText(description);
                    GlideApp.with(getApplicationContext()).load(card.img).into(image_display);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DisplayCard.this, "Error while loading", Toast.LENGTH_SHORT).show();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}