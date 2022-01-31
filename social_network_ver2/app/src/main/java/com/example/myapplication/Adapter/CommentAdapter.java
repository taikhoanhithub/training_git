package com.example.myapplication.Adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.models.Comment;
import com.example.myapplication.models.User;
import com.example.socialnetwork.R;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    Context context;
    ArrayList<Comment> list;

    public CommentAdapter(Context context, ArrayList<Comment> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_sample, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = list.get(position);
        holder.commentBody.setText(comment.getCommentBody());
        String time2 = TimeAgo.using(comment.getCommentAt());
        String time = (String) DateUtils.getRelativeTimeSpanString(comment.getCommentAt()); // so khoang thoi gian post comment voi thoi gian hien tai
        holder.timeComment.setText(time2);
        FirebaseDatabase.getInstance().getReference().child("Users").child(comment.getCommentBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Picasso.get().load(user.getProfile()).placeholder(R.drawable.neymar1).into(holder.profileComment);
                        holder.commentBody.setText(user.getName() + ": " + comment.getCommentBody());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView commentBody, timeComment;
        ImageView profileComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commentBody = itemView.findViewById(R.id.commentBody);
            timeComment = itemView.findViewById(R.id.timeComment);
            profileComment = itemView.findViewById(R.id.profileComment);
        }
    }
}
