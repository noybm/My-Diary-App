package com.app.mydiary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;

public class AddDiaryDetailsActivity extends AppCompatActivity {

    EditText titleEditText,contentEditText;
    Button saveNoteBtn;
    TextView pageTitleTextView;
    String title,content,docID;
    boolean isEditMode = false;
    ImageView deleteNoteTextViewBtn;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary_details);


        Intent intent = getIntent();
        if (getIntent().hasExtra("date")) {
            date = intent.getStringExtra("date");
            Log.e("oopp", "onCreate: "+date );
        }


        titleEditText = findViewById(R.id.notes_titles_text);
        contentEditText = findViewById(R.id.notes_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteNoteTextViewBtn = findViewById(R.id.delete_note_text_view_btn);


        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docID = getIntent().getStringExtra("docId");

        if (docID!=null && !docID.isEmpty()){
            isEditMode = true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);
        if(isEditMode){
            pageTitleTextView.setText("Edit your note");
            deleteNoteTextViewBtn.setVisibility(View.VISIBLE);
        }

        saveNoteBtn.setOnClickListener( (v)-> saveNote());

        deleteNoteTextViewBtn.setOnClickListener((v)-> deleteNoteFromFirebase());
    }

    void saveNote(){
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();
        if (noteTitle==null || noteTitle.isEmpty() ){
            titleEditText.setError("Title is required");
            return;

        }
        ModelClass note = new ModelClass();
        note.setTitle(noteTitle);
        note.setContent(noteContent);

        saveNoteToFirebase(noteTitle,noteContent);


    }

    void saveNoteToFirebase(String title,String contentStr){
        DocumentReference documentReference;
        if (isEditMode){

            FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("diary").child(currentUser.getUid()).child(date);

            databaseReference.child("title").setValue(title);
            databaseReference.child("content").setValue(contentStr).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                    Utility.showToast(AddDiaryDetailsActivity.this,"Added successfully");
                    finish();

                }
            });



        }else{


            FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("diary").child(currentUser.getUid()).child(date);

            databaseReference.child("title").setValue(title);
            databaseReference.child("date").setValue(date);

            databaseReference.child("content").setValue(contentStr).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Utility.showToast(AddDiaryDetailsActivity.this,"Added successfully");
                    finish();

                }
            });

        }



    }

    void deleteNoteFromFirebase(){
        DocumentReference documentReference;

        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("diary").child(currentUser.getUid()).child(date);




        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Utility.showToast(AddDiaryDetailsActivity.this,"deleted successfully");
                    finish();

                }else{
                    Utility.showToast(AddDiaryDetailsActivity.this,"Failed whilst deleting note");

                }

            }
        });

    }


}