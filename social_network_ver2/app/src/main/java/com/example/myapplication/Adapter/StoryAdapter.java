package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialnetwork.R;
import com.example.myapplication.models.Story;

import java.util.ArrayList;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {
    ArrayList<Story> listStory;
    Context context;

    public StoryAdapter(ArrayList<Story> listStory, Context context) {
        this.listStory = listStory;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup root;
        View view = LayoutInflater.from(context).inflate(R.layout.story_review_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Story s = listStory.get(position);

    }

    @Override
    public int getItemCount() {
        return listStory.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView storyImg, profile, storyType;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
