package com.example.myapplication.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.myapplication.models.Post;
import com.example.myapplication.models.User;
import com.example.socialnetwork.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.Date;

public class AddFragment extends Fragment {
    View view;
    EditText postDescription;
    Button btnPost;
    ImageView addImg, postImg, profileUser;
    TextView name, profession;
    Uri uri;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add, container, false);
        initView();
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Post uploading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    Picasso.get().load(user.getProfile()).placeholder(R.drawable.neymar1).into(profileUser);
                    name.setText(user.getName());
                    profession.setText(user.getProfession());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        postDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String description = postDescription.getText().toString();
                if(!description.isEmpty()) {
                    btnPost.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.follow_active_btn));
                    btnPost.setTextColor(getContext().getResources().getColor(R.color.white));
                    btnPost.setEnabled(true);
                } else {
                    btnPost.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.color.white));
                    btnPost.setTextColor(getContext().getResources().getColor(R.color.black));
                    btnPost.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 10);
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                // luu anh cua bai dang len storage
                final StorageReference reference = storage.getReference().child("posts")
                        .child(FirebaseAuth.getInstance().getUid()).child(new Date().getTime()+ "");
                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // tai duong dan anh vua tai len ve de set cho thuoc tinh cua post
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Post post = new Post();
                                post.setPostImage(uri.toString());
                                post.setPostedBy(FirebaseAuth.getInstance().getUid());
                                post.setPostDescription(postDescription.getText().toString());
                                post.setPostedAt(new Date().getTime());

                                database.getReference().child("Posts").push().setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getContext(), "Posted successfully!", Toast.LENGTH_SHORT).show();
                                        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData() != null) {
            uri = data.getData();
            postImg.setImageURI(uri);
            postImg.setVisibility(View.VISIBLE);
            btnPost.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.follow_active_btn));
            btnPost.setTextColor(getContext().getResources().getColor(R.color.white));
            btnPost.setEnabled(true);
        }
    }

    void initView() {
        postDescription = view.findViewById(R.id.postDescription);
        btnPost = view.findViewById(R.id.btnPost);
        addImg = view.findViewById(R.id.addImg);
        postImg = view.findViewById(R.id.postImg);
        profileUser = view.findViewById(R.id.profileUserPost);
        name = view.findViewById(R.id.nameOfUser);
        profession = view.findViewById(R.id.professionOfUser);
    }
}
