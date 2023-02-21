package com.app.mydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TodoActivity extends AppCompatActivity {

    FloatingActionButton addNoteBtn;
    RecyclerView recyclerView;
    ImageButton menuBtn;
    FirebaseRecyclerAdapter noteAdapter;
    String date;
    ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);


        Intent intent = getIntent();
        if (getIntent().hasExtra("date")) {
            date = intent.getStringExtra("date");
            Log.e("oopp", "onCreate: "+date );
        }


        addNoteBtn = findViewById(R.id.add_note_btn);
        recyclerView = findViewById(R.id.recycler_view);
        menuBtn = findViewById(R.id.menu_btn);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progress_bar = findViewById(R.id.progress_bar);




        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodoActivity.this, AddTodoFolderActivity.class);
                intent.putExtra("date",date);
                startActivity(intent);

            }
        });
        menuBtn.setOnClickListener((v)->showMenu() );
        setupRecyclerView();
    }

    void showMenu(){
        PopupMenu popupMenu  = new PopupMenu(TodoActivity.this,menuBtn);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getTitle()=="Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(TodoActivity.this,LoginActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });


    }

    public void setupRecyclerView(){

        //query

        String UserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("todo").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        com.google.firebase.database.Query firebaseSearchQuery = databaseReference;

        //firebase recyclerview
        FirebaseRecyclerOptions<ModelClass> options =
                new FirebaseRecyclerOptions.Builder<ModelClass>()
                        .setQuery(firebaseSearchQuery, ModelClass.class)
                        .build();

        noteAdapter = new FirebaseRecyclerAdapter<ModelClass, TodoViewHolder>(options) {
            @NonNull
            @Override
            public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);

                return new TodoViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(TodoViewHolder holder, int position, ModelClass model) {
                progress_bar.setVisibility(View.GONE);


                holder.titleTextView.setText(model.getTitle());
                holder.timestampTextView.setText("Date: "+model.getDate());

                holder.itemView.setOnClickListener((v)->{
                    Intent intent = new Intent(getApplicationContext(), AddTodoFolderActivity.class);
                    intent.putExtra("content",model.getContent());
                    intent.putExtra("title",model.getTitle());
                    intent.putExtra("docId",getRef(position).getKey());
                    startActivity(intent);

                });

                holder.sharetv.setOnClickListener((v)->{
                    Intent intent = new Intent(getApplicationContext(), AllUsersActivity.class);
                    intent.putExtra("id",getRef(position).getKey());
                    intent.putExtra("content",model.getContent());
                    intent.putExtra("title",model.getTitle());
                    intent.putExtra("date",model.getDate());

                    startActivity(intent);

                });

                holder.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
                        selectorIntent.setData(Uri.parse("mailto:"));

                        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"address@mail.com"});
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Todo List "+model.getTitle());
                        emailIntent.putExtra(Intent.EXTRA_TEXT, model.getContent() );
                        emailIntent.setSelector( selectorIntent );

                        startActivity(Intent.createChooser(emailIntent, "Send email..."));
                    }
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        //noteAdapter.notifyDataSetChanged();
    }


    public static class TodoViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView,timestampTextView,sharetv;

        ImageView share;
        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.note_title_text_view);
            timestampTextView = itemView.findViewById(R.id.note_timestamp_text_view);
            share = itemView.findViewById(R.id.share);
            sharetv = itemView.findViewById(R.id.sharetv);

        }

    }
}
