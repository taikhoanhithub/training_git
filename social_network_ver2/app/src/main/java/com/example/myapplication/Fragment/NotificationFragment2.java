package com.example.myapplication.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.NotificationAdapter;
import com.example.socialnetwork.R;
import com.example.myapplication.models.Notification;

import java.util.ArrayList;

public class NotificationFragment2 extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Notification> notifications;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification2, container, false);
        recyclerView = view.findViewById(R.id.NotificationRv);
        notifications = new ArrayList<>();
        notifications.add(new Notification(R.drawable.neymar1, "<b>Neymar</b> The only thing necessary for the triumph of evil is for good men to do nothing.", "1 hour ago"));
        notifications.add(new Notification(R.drawable.neymar1, "<b>Ronaldo</b> Life is like riding a bicycle. To keep your balance, you must keep moving. ", "1 hour ago"));
        notifications.add(new Notification(R.drawable.neymar1, "<b>Vu The Duong</b> The pessimist complains about the wind; the optimist expects it to change; the realist adjusts the sails.", "1 hour ago"));
        notifications.add(new Notification(R.drawable.neymar1, "<b>Messi</b> Do not dwell in the past, do not dream of the future, concentrate the mind on the present moment.", "1 hour ago"));
        NotificationAdapter adapter = new NotificationAdapter(getContext(), notifications);
        Context context;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
