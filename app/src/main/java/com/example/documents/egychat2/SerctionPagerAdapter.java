package com.example.documents.egychat2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.documents.egychat2.Fragments.ChatsFragment;
import com.example.documents.egychat2.Fragments.FriendsFragment;
import com.example.documents.egychat2.Fragments.RequestsFragment;

class SerctionPagerAdapter extends FragmentPagerAdapter {

    public SerctionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                RequestsFragment requestsFragment = new RequestsFragment();
                return requestsFragment;
            case 1:
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;
            case 2:
                FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "Requestes";
            case 1:
                return "Chats";
            case 2:
                return "Friends";
            default:
                return null;
        }

    }
}
