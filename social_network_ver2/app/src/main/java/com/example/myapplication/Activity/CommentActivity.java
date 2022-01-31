package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapter.CommentAdapter;
import com.example.myapplication.models.Comment;
import com.example.myapplication.models.Post;
import com.example.myapplication.models.User;
import com.example.socialnetwork.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class CommentActivity extends AppCompatActivity {
    Intent intent;
    String postId, postedBy;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ImageView postImgComment, profileCommenter, postCommentBtn;
    TextView tvLike, tvComment, tvDescription, nameCommenter;
    EditText commentBody;
    RecyclerView commentRv;
    Toolbar toolbar;
    ArrayList<Comment> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initView();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        setSupportActionBar(toolbar);
        CommentActivity.this.setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = getIntent();
        postId = intent.getStringExtra("postId");
        postedBy = intent.getStringExtra("postedBy");

        database.getReference().child("Posts").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                Picasso.get().load(post.getPostImage()).into(postImgComment);
                tvDescription.setText(post.getPostDescription());
                tvLike.setText(post.getPostLike() + "");
                tvComment.setText(post.getCommentCount()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // lay ra thong tin cua nguoi dang bai post
        database.getReference().child("Users").child(postedBy).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Picasso.get().load(user.getProfile()).into(profileCommenter);
                nameCommenter.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        postCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment comment = new Comment();
                comment.setCommentBody(commentBody.getText().toString());
                comment.setCommentAt(new Date().getTime());
                comment.setCommentBy(FirebaseAuth.getInstance().getUid());
                database.getReference().child("Posts").child(postId).child("comments").push()
                        .setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("Posts").child(postId)
                                .child("commentCount").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int commentCount = 0;
                                if (snapshot.exists()) {
                                    commentCount = snapshot.getValue(Integer.class);
                                }
                                database.getReference().child("Posts").child(postId)
                                        .child("commentCount").setValue(commentCount + 1)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                commentBody.setText("");
                                                Toast.makeText(getApplicationContext(), "Commented", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
            }
        });

        CommentAdapter adapter = new CommentAdapter(this, list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        commentRv.setLayoutManager(linearLayoutManager);
        commentRv.setAdapter(adapter);

        database.getReference().child("Posts").child(postId).child("comments")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Comment comment = snapshot1.getValue(Comment.class);
                            list.add(comment);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    void initView() {
        toolbar = findViewById(R.id.toolbar);
        postImgComment = findViewById(R.id.postImgComment);
        tvLike = findViewById(R.id.like);
        tvComment = findViewById(R.id.comment);
        tvDescription = findViewById(R.id.descriptionComment);
        profileCommenter = findViewById(R.id.profileImageComment);
        nameCommenter = findViewById(R.id.nameCommenter);
        postCommentBtn = findViewById(R.id.postCommentBtn);
        commentBody = findViewById(R.id.edtComment);
        commentRv = findViewById(R.id.commentRv);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}