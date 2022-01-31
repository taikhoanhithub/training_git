package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Activity.CommentActivity;
import com.example.myapplication.models.Notification;
import com.example.myapplication.models.User;
import com.example.socialnetwork.R;
import com.example.myapplication.models.Post;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    Context context;
    ArrayList<Post> lisPosts;

    public PostAdapter(Context context, ArrayList<Post> lisPosts) {
        this.context = context;
        this.lisPosts = lisPosts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup root;
        View view = LayoutInflater.from(context).inflate(R.layout.post_rv_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = lisPosts.get(position);
        Picasso.get().load(post.getPostImage()).placeholder(R.drawable.neymar1).into(holder.postImage);
        holder.postDescription.setText(post.getPostDescription());
        holder.like.setText(post.getPostLike() + "");
        holder.comment.setText(post.getCommentCount()+"");
        FirebaseDatabase.getInstance().getReference().child("Users").child(post.getPostedBy())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Picasso.get().load(user.getProfile()).placeholder(R.drawable.neymar1).into(holder.profile);
                        holder.name.setText(user.getName());
                        holder.about.setText(user.getProfession());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        FirebaseDatabase.getInstance().getReference().child("Posts").child(post.getPostId())
                .child("likes").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart2, 0, 0, 0);
                } else {
                    // click vao nut like
                    holder.like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseDatabase.getInstance().getReference().child("Posts")
                                    .child(post.getPostId()).child("likes").child(FirebaseAuth.getInstance().getUid())
                                    .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    FirebaseDatabase.getInstance().getReference().child("Posts")
                                            .child(post.getPostId()).child("postLike")
                                            .setValue(post.getPostLike() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart2, 0, 0, 0);
                                            Notification notification = new Notification();
                                            notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                            notification.setNotificationAt(new Date().getTime());
                                            notification.setPostedBy(post.getPostedBy());
                                            notification.setType("like");
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // click vao nut comment
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("postId", post.getPostId()); // gui postId, postedBy: id bai post va id nguoi post
                intent.putExtra("postedBy", post.getPostedBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lisPosts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile, postImage, save;
        TextView name, about, like, comment, share;
        TextView postDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.dashboardAvatar);
            postImage = itemView.findViewById(R.id.imgDashBoard);
            save = itemView.findViewById(R.id.save);
            name = itemView.findViewById(R.id.dashboardName);
            about = itemView.findViewById(R.id.about);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            share = itemView.findViewById(R.id.share);
            postDescription = (TextView) itemView.findViewById(R.id.postDescription);
        }
    }
}

