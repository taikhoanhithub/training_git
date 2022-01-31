package com.example.myapplication.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.FollowersAdapter;
import com.example.socialnetwork.R;
import com.example.myapplication.models.Follow;
import com.example.myapplication.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    ArrayList<Follow> friends;
    RecyclerView friendRv;
    ImageView changeCoverPhoto, coverImage, verifyAccount, profileAvatar;
    TextView name, profession, followerCount;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        friendRv = view.findViewById(R.id.friendRv);
        name = view.findViewById(R.id.name);
        profession = view.findViewById(R.id.profession);
        coverImage = view.findViewById(R.id.coverImage);
        profileAvatar = view.findViewById(R.id.profileAvatar);
        verifyAccount = view.findViewById(R.id.verifyAccount);
        followerCount = view.findViewById(R.id.followCount);

        changeCoverPhoto = view.findViewById(R.id.changeCoverPhoto);
        friends = new ArrayList<>();

        FollowersAdapter adapter = new FollowersAdapter(getContext(), friends);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        friendRv.setLayoutManager(linearLayoutManager);
        friendRv.setAdapter(adapter);

        changeCoverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 11);
            }
        });

        // chon anh dai dien
        verifyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent();
                intent1.setAction(Intent.ACTION_GET_CONTENT);// chuyen sang thu vien anh
                intent1.setType("image/*");
                startActivityForResult(intent1, 22);
            }
        });

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        // fetch user data from database
        database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    Picasso.get().load(user.getCoverImage()).placeholder(R.drawable.neymar1).into(coverImage);
                    name.setText(user.getName());
                    profession.setText(user.getProfession());
                    followerCount.setText(user.getFollowerCount() + "");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // lay ve danh sach thong tin nhung nguoi follow
        database.getReference().child("Users").child(auth.getUid()).child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friends.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Follow follow = dataSnapshot.getValue(Follow.class);
                    friends.add(follow);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11) {
            if (data.getData() != null) {
                Uri uri = data.getData(); // uri cua anh dc chon
                coverImage.setImageURI(uri); // setup anh cho imageView

                final StorageReference reference = storage.getReference().child("cover_photo").
                        child(auth.getUid());
                // luu uri cua coverPhoto len storage
                reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                // them url cua cover image vao value cua user hien tai
                                database.getReference().child("Users").child(auth.getUid()).child("coverPhoto").
                                        setValue(uri.toString());
                            }
                        });
                    }
                });
            }
        } else if (requestCode == 22) {
            if (data.getData() != null) {
                Uri uri = data.getData(); // uri cua anh dc chon
                profileAvatar.setImageURI(uri); // setup anh cho imageView


                final StorageReference reference = storage.getReference().child("profile_avatar").
                        child(auth.getUid());
                // luu uri cua coverPhoto len storage
                reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                // them url cua cover image vao value cua user hien tai
                                database.getReference().child("Users").child(auth.getUid()).child("profile").
                                        setValue(uri.toString());
                            }
                        });
                    }
                });
            }
        }
    }
}
