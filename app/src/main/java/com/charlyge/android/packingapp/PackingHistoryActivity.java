package com.charlyge.android.packingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.charlyge.android.packingapp.Adapters.TravelHistoryAdapter;
import com.charlyge.android.packingapp.Database.Entities.PackingReminder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class PackingHistoryActivity extends AppCompatActivity {
      private FirebaseFirestore firebaseFirestore;
      private FirebaseAuth mFirebaseAuth;
      private FirebaseUser mFirebaseUser;
      private TravelHistoryAdapter travelHistoryAdapter;
      private List<PackingReminder> packingReminderList;
      private ArrayList<DocumentSnapshot> mSnapshots = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing_history);
         firebaseFirestore = FirebaseFirestore.getInstance();
         mFirebaseAuth = FirebaseAuth.getInstance();
         mFirebaseUser = mFirebaseAuth.getCurrentUser();
        RecyclerView recyclerView = findViewById(R.id.recycler_packing_history);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
         travelHistoryAdapter = new TravelHistoryAdapter();
        recyclerView.setAdapter(travelHistoryAdapter);

        loadHistory();
    }

    public void loadHistory(){

        //  private ListenerRegistration listenerRegistration;
        firebaseFirestore.collection("UsersTrips")
                .document(mFirebaseUser.getUid())
                .collection("Trips").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                for (DocumentChange change : queryDocumentSnapshots.getDocumentChanges()) {
                    packingReminderList = queryDocumentSnapshots.toObjects(PackingReminder.class);
                    travelHistoryAdapter.setTravelList(packingReminderList);
                    switch (change.getType()) {
                        case ADDED:
                            onDocumentAdded(change);
                            break;
                        case MODIFIED:
                            onDocumentModified(change);
                            break;
                        case REMOVED:
                            onDocumentRemoved(change);
                            break;
                    }
                }
            }
        });
    }


    protected void onDocumentAdded(DocumentChange change) {
        mSnapshots.add(change.getNewIndex(), change.getDocument());
        travelHistoryAdapter.notifyItemInserted(change.getNewIndex());
    }

    protected void onDocumentModified(DocumentChange change) {
        if (change.getOldIndex() == change.getNewIndex()) {
            // Item changed but remained in same position
            mSnapshots.set(change.getOldIndex(), change.getDocument());
            travelHistoryAdapter.notifyItemChanged(change.getOldIndex());
        } else {
            // Item changed and changed position
            mSnapshots.remove(change.getOldIndex());
            mSnapshots.add(change.getNewIndex(), change.getDocument());
            travelHistoryAdapter.notifyItemMoved(change.getOldIndex(), change.getNewIndex());
        }
    }

    protected void onDocumentRemoved(DocumentChange change) {
        mSnapshots.remove(change.getOldIndex());
        travelHistoryAdapter.notifyItemRemoved(change.getOldIndex());
    }
}
