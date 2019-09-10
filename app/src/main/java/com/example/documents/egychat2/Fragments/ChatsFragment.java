package com.example.documents.egychat2.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.documents.egychat2.R;
import com.example.documents.egychat2.Settings_Pakage.Chat;
import com.example.documents.egychat2.Settings_Pakage.UserAdapter;
import com.example.documents.egychat2.Settings_Pakage.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    FirebaseUser fuser;
    DatabaseReference reference;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<Users> mUser;
    private List<String> userlist;


    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = view.findViewById(R.id.chats_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        userlist = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userlist.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);

                    if (chat.getSender().equals(fuser.getUid())) {
                        userlist.add(chat.getReceiver());
                    }
                    if (chat.getReceiver().equals(fuser.getUid())) {
                        userlist.add(chat.getSender());
                    }
                }

                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    private void readChats() {
        mUser = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Users users = snapshot.getValue(Users.class);

                    Log.e("snap", snapshot.toString());

                    for (String id : userlist) {
                        try {
                            if (users.getId().equals(id)) {
                                if (mUser.size() != 0) {
                                    for (Users users1 : mUser) {
                                        if (!users.getId().equals(users1.getId())) {
                                            mUser.add(users);
                                        }
                                    }
                                } else {
                                    mUser.add(users);
                                }
                            }
                        } catch (NullPointerException e) {

                        }

                    }
                }
                userAdapter = new UserAdapter(getContext(), mUser);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
