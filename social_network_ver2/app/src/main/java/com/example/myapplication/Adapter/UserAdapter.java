package com.example.myapplication.Adapter;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.view.FocusFinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.models.Follow;
import com.example.myapplication.models.Notification;
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

// duoc dinh nghia de su dung cho viec hien thi danh sach ng dung tren recyclerView cua searchFragment
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context context;
    ArrayList<User> users;

    public UserAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_sample, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        Picasso.get().load(user.getProfile())
                .placeholder(R.drawable.neymar1).into(holder.profile);
        holder.name.setText(user.getName());
        holder.profession.setText(user.getProfession());

        // kiem tra da follow ng nay hay chua
        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUserId()).child("followers")
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    holder.btnFollow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.follow_active_btn));
                    holder.btnFollow.setText("Following");
                    holder.btnFollow.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
                    holder.btnFollow.setEnabled(false);
                } else {
                    holder.btnFollow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Follow follow = new Follow();
                            follow.setFollowedBy(FirebaseAuth.getInstance().getUid()); // id cua minh, la ng follow
                            follow.setFollowedAt(new Date().getTime()); // thoi gian bat dau follow

                            FirebaseDatabase.getInstance().getReference().child("Users")
                                    .child(user.getUserId())
                                    .child("followers")
                                    .child(FirebaseAuth.getInstance().getUid()).setValue(follow)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("Users")
                                                    .child(user.getUserId())
                                                    .child("followerCount")
                                                    .setValue(user.getFollowerCount() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    holder.btnFollow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.follow_active_btn));
                                                    holder.btnFollow.setText("Following");
                                                    holder.btnFollow.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
                                                    holder.btnFollow.setEnabled(false);
                                                    Toast.makeText(context.getApplicationContext(), "You followed " + user.getName(), Toast.LENGTH_SHORT).show();

                                                    Notification notification = new Notification();
                                                    notification.setNotificationBy(FirebaseAuth.getInstance().getTenantId());
                                                    notification.setNotificationAt(new Date().getTime());
                                                    notification.setType("follow");

                                                    FirebaseDatabase.getInstance().getReference().child("notification")
                                                            .child(user.getUserId()).push().setValue(notification);
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

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView name, profession;
        Button btnFollow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profileUser);
            name = itemView.findViewById(R.id.nameOfUser);
            profession = itemView.findViewById(R.id.professionOfUser);
            btnFollow = itemView.findViewById(R.id.followBtn);
        }
    }
}
