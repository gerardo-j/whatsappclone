package com.example.whatsappclone.Utils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessageChannelDB {
    private final DatabaseReference databaseReference;
    private final UserDB userDB;

    public MessageChannelDB() {
        databaseReference = FirebaseDatabase.getInstance().getReference(MessageChannel.class.getSimpleName());
        userDB = new UserDB();
    }

    public Task<Void> create(MessageChannel messageChannel) {
        String key = databaseReference.push().getKey();

        ArrayList<User> users = messageChannel.getUsers();
        users.forEach(user -> {
            MessageChannel temp = new MessageChannel();
            userDB.addChannel(messageChannel.getName(), "https://icons.iconarchive.com/icons/paomedia/small-n-flat/1024/profile-icon.png", user.getUid(), key);
        });

        return databaseReference.child(key).setValue(messageChannel);
    }

    public Task<Void> update() {
        String key = databaseReference.push().getKey();

        User user = new User("d", "custom_username", "userId", "username", "ggg@test.com");
        Map<String, Object> postValues = user.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + "userId" + "/" + key, postValues);

        return databaseReference.updateChildren(childUpdates);
    }

}
