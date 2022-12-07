package com.example.whatsappclone.Utils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserDB {

    private final static DatabaseReference userReference = FirebaseDatabase.getInstance().getReference(User.class.getSimpleName());
    private final static FirebaseFirestore usernameDB = FirebaseFirestore.getInstance();

    public UserDB() {
//        userReference = FirebaseDatabase.getInstance().getReference(User.class.getSimpleName());
    }

    public Task<Void> addChannel(String name, String imageUrl, String uid, String channelId) {
        MessageChannel newChannel = new MessageChannel(name, imageUrl);
        return userReference.child(uid).child(MessageChannel.class.getSimpleName()).child(channelId).setValue(newChannel);
    }

    public void addChannelListener(String uid, ValueEventListener valueEventListener) {
        userReference.child(uid).child(MessageChannel.class.getSimpleName()).addValueEventListener(valueEventListener);
    }

    public void removeChannelListener(String uid, ValueEventListener valueEventListener) {
        userReference.child(uid).child(MessageChannel.class.getSimpleName()).removeEventListener(valueEventListener);
    }

    public Task<DocumentSnapshot> findUserByUsername(String username) {
        return usernameDB.collection("usernames").document(username).get();
    }

}
