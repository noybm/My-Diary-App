package com.app.mydiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DiaryActivity extends AppCompatActivity {

    FloatingActionButton addNoteBtn;
    RecyclerView recyclerView;
    ImageButton menuBtn;
    FirebaseRecyclerAdapter noteAdapter;
    String date;
    ProgressBar progress_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);



        progress_bar = findViewById(R.id.progress_bar);

        addNoteBtn = findViewById(R.id.add_note_btn);
        recyclerView = findViewById(R.id.recycler_view);
        menuBtn = findViewById(R.id.menu_btn);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiaryActivity.this, CalendarActivity.class);
                startActivity(intent);

            }
        });
        menuBtn.setOnClickListener((v)->showMenu() );
        setupRecyclerView();
    }

    void showMenu(){
        PopupMenu popupMenu  = new PopupMenu(DiaryActivity.this,menuBtn);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getTitle()=="Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(DiaryActivity.this,LoginActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });


    }

    public void setupRecyclerView(){


        String UserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReference().child("diary").child(UserId);
        com.google.firebase.database.Query firebaseSearchQuery = databaseReference;




        FirebaseRecyclerOptions<ModelClass> options =
                new FirebaseRecyclerOptions.Builder<ModelClass>()
                        .setQuery(firebaseSearchQuery, ModelClass.class)
                        .build();

        noteAdapter = new FirebaseRecyclerAdapter<ModelClass, DiaryViewHolder>(options) {
            @NonNull
            @Override
            public DiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                // attach layout to RecyclerView
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_item, parent, false);

                return new DiaryViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(DiaryViewHolder holder, int position, ModelClass model) {

                progress_bar.setVisibility(View.GONE);
                // set data in views
                holder.titleTextView.setText("Title: "+model.getTitle());
                holder.contentTextView.setText(model.getContent());
                holder.timestampTextView.setText("Date: "+getRef(position).getKey());

                holder.itemView.setOnClickListener((v)->{
                    Intent intent = new Intent(getApplicationContext(), AddDiaryDetailsActivity.class);
                    intent.putExtra("content",model.getContent());
                    intent.putExtra("title",model.getTitle());
                    intent.putExtra("docId",getRef(position).getKey());
                    intent.putExtra("date",getRef(position).getKey());

                    startActivity(intent);

                });

            }
        };


        noteAdapter.startListening();
        // add adapter to recyclerview

        recyclerView.setAdapter(noteAdapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static class DiaryViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView,contentTextView,timestampTextView;

        public DiaryViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.note_title_text_view);
            contentTextView = itemView.findViewById(R.id.note_content_text_view);
            timestampTextView = itemView.findViewById(R.id.note_timestamp_text_view);
        }

    }
}
