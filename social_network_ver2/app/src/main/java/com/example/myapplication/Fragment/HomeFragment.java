package com.example.myapplication.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.example.myapplication.Adapter.PostAdapter;
import com.example.myapplication.Adapter.StoryAdapter;
import com.example.myapplication.models.Post;
import com.example.myapplication.models.Story;
import com.example.myapplication.models.UserStories;
import com.example.socialnetwork.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {
    RecyclerView storyRv;
    ShimmerRecyclerView postRv;
    ArrayList<Story> listStory;
    ArrayList<Post> listPost;
    Adapter storyAdapter, postAdapter;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    RoundedImageView roundedImageView;
    ActivityResultLauncher<String> galleryLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        postRv = view.findViewById(R.id.recyclerViewPost);
        postRv.showShimmerAdapter();

        storyRv = view.findViewById(R.id.recyclerViewStory);
        listStory = new ArrayList<>();

        storyAdapter = new StoryAdapter(listStory, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        storyRv.setLayoutManager(linearLayoutManager);
        storyRv.setAdapter(storyAdapter);
        storyRv.setNestedScrollingEnabled(false);

        database.getReference().child("stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot storySnapshot : snapshot.getChildren()) {
                        Story story = new Story();
                        story.setStoryBy(storySnapshot.getKey());
                        story.setStoryAt(storySnapshot.child("postedBy").getValue(Long.class));

                        ArrayList<UserStories> stories = new ArrayList<>();
                        for(DataSnapshot snapshot1 : storySnapshot.child("userStories").getChildren()){
                            UserStories userStories = snapshot1.getValue(UserStories.class);
                            stories.add(userStories);
                        }
                        story.setStories(stories);
                        listStory.add(story);
                    }
                    storyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        storyRv = view.findViewById(R.id.recyclerViewStory);
        listPost = new ArrayList<>();

        postAdapter = new PostAdapter(getContext(), listPost);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        postRv.setLayoutManager(linearLayoutManager1);

        database.getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listPost.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    post.setPostId(dataSnapshot.getKey());
                    listPost.add(post);
                }
                postRv.setAdapter(postAdapter);
                postRv.hideShimmerAdapter();
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        roundedImageView = view.findViewById(R.id.roundedImg);
        roundedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryLauncher.launch("image/*");
            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                roundedImageView.setImageURI(result);
                final StorageReference reference = storage.getReference().child("stories")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child(new Date().getTime() + "");
                reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Story story = new Story();
                                database.getReference().child("stories").child(FirebaseAuth.getInstance().getUid())
                                        .child("postedBy").setValue(story.getStoryAt()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        UserStories userStories = new UserStories(uri.toString(), story.getStoryAt());
                                        database.getReference().child("stories").child(FirebaseAuth.getInstance().getUid())
                                                .child("userStories").push().setValue(userStories);
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

        return view;
    }
}
