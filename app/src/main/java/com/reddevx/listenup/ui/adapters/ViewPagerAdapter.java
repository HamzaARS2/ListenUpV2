package com.reddevx.listenup.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.reddevx.listenup.ui.fragments.AudiosFragment;
import com.reddevx.listenup.ui.fragments.FavoriteFragment;
import com.reddevx.listenup.ui.fragments.PlaylistFragment;


public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new AudiosFragment();
            case 1:
                return new FavoriteFragment();
            case 2:
                return new PlaylistFragment();
        }
        return new Fragment();
    }


    @Override
    public int getItemCount() {
        return 3;
    }

}
