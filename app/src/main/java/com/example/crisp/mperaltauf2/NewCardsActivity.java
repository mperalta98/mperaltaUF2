package com.example.crisp.mperaltauf2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import java.util.UUID;

public class NewCardsActivity extends AppCompatActivity {

    static final int RC_IMG_PICK = 9000;

    public Button btn_public, btn_img;
    public EditText new_title, new_context;
    public String title_string, context_string;
    public DatabaseReference mRef;
    public String id;
    public ImageView preview;
    Uri mediaUri;
    String mediaType;
    String downloadUri;
    Card card;
    boolean new_card = true;
    boolean card_image = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);

        mRef = FirebaseDatabase.getInstance().getReference();

        btn_public = findViewById(R.id.btn_publicar);
        new_title = findViewById(R.id.new_titulo);
        new_context = findViewById(R.id.new_context);
        btn_img = findViewById(R.id.btn_image);
        preview = findViewById(R.id.img_preview);


        btn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),RC_IMG_PICK);
            }
        });

        btn_public.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title_string = new_title.getText().toString();
                context_string = new_context.getText().toString();
                id = UUID.randomUUID().toString();
                if(card_image){
                    FirebaseStorage.getInstance().getReference(mediaType + "/" + id + mediaUri.getLastPathSegment()).putFile(mediaUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            return task.getResult().getStorage().getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                new_card = false;
                                downloadUri = task.getResult().toString();
                                card = new Card(id,title_string,context_string,downloadUri);
                                mRef.child("Cards").child(id).setValue(card);
                            }
                        }
                    });
                }
                if(new_card){
                    card = new Card(id,title_string,context_string,downloadUri);
                    mRef.child("Cards").child(id).setValue(card);
                }
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_IMG_PICK && resultCode == RESULT_OK){
            mediaUri = data.getData();
            mediaType = "image";
            GlideApp.with(this).load(mediaUri).into(preview);
            card_image = true;
        }
    }
}