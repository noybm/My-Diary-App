package com.app.mydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AllUsersActivity extends AppCompatActivity {

    RecyclerView recycleView;
    String id,content,title,date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        id = getIntent().getStringExtra("id");
        content = getIntent().getStringExtra("content");
        title = getIntent().getStringExtra("title");
        date = getIntent().getStringExtra("date");

        //init
        recycleView = findViewById(R.id.RecycleList);
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new LinearLayoutManager(this));

        //============RecyclerView===============
        Query conversationQuery = FirebaseDatabase.getInstance().getReference().child("Users");


        FirebaseRecyclerOptions<users> options =
                new FirebaseRecyclerOptions.Builder<users>()
                        .setQuery(conversationQuery, users.class)
                        .build();

        FirebaseRecyclerAdapter friendsConvAdapter = new FirebaseRecyclerAdapter<users, Viewholder>(options) {
            @Override
            public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.mainitemgrid, parent, false);

                return new Viewholder(view);
            }

            @Override
            protected void onBindViewHolder(Viewholder viewholder, int position, users model) {

                viewholder.emailTextView.setText("Email: " + model.getEmail());


                viewholder.emailTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        DatabaseReference ddd = FirebaseDatabase.getInstance().getReference().child("todo").child(model.getUid()).child(id);
                        ddd.child("content").setValue(content);
                        ddd.child("title").setValue(title);
                        ddd.child("date").setValue(date).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                startActivity(new Intent(getApplicationContext(),TodoActivity.class));
                                finish();
                                Toast.makeText(AllUsersActivity.this, "TO DO list is shared", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                viewholder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        DatabaseReference ddd = FirebaseDatabase.getInstance().getReference().child("todo").child(model.getUid()).child(id);
                        ddd.child("content").setValue(content);
                        ddd.child("title").setValue(title);
                        ddd.child("date").setValue(date).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                startActivity(new Intent(getApplicationContext(),TodoActivity.class));
                                Toast.makeText(AllUsersActivity.this, "TO DO list is shared", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        });

                    }
                });


            }
        };


        friendsConvAdapter.startListening();
        recycleView.setAdapter(friendsConvAdapter);
        friendsConvAdapter.startListening();




    }

    static class Viewholder extends RecyclerView.ViewHolder {

        TextView nameTextview, emailTextView;
        CardView card_parent;
        ImageView imageView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            emailTextView = itemView.findViewById(R.id.emailTextView);
            card_parent = itemView.findViewById(R.id.card_parent);
            imageView = itemView.findViewById(R.id.imageViewId);


        }
    }


}
