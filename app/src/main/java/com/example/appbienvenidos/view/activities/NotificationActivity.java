package com.example.appbienvenidos.view.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbienvenidos.R;
import com.example.appbienvenidos.model.Notifications;
import com.example.appbienvenidos.view.adapter.NotificationAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends BaseActivity{

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Notifications> list;
    private TextView emptyView;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle saveTnstanceState){

        super.onCreate(saveTnstanceState);
        setContentView(R.layout.activity_notifications);

        recyclerView = findViewById(R.id.recycler_notifs);
        emptyView = findViewById(R.id.empty_view);
        btnBack = findViewById(R.id.Back);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new NotificationAdapter(list);
        recyclerView.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());

        loadNotifications();
    }

    private void loadNotifications(){
        String myId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance().collection("Notifications")
                .whereEqualTo("recipientId", myId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()){
                        List<Notifications> data = queryDocumentSnapshots.toObjects(Notifications.class);
                        list.addAll(data);
                        adapter.notifyDataSetChanged();
                        emptyView.setVisibility(View.GONE);
                    } else {
                        emptyView.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(e -> {

                });
    }
}
