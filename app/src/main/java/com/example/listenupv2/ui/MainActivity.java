package com.example.listenupv2.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.listenupv2.R;
import com.example.listenupv2.databinding.ActivityMainBinding;
import com.example.listenupv2.ui.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ViewPagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        setSupportActionBar(binding.mainToolbar);
        buildViewPager();
    }


    public void buildViewPager(){
        pagerAdapter = new ViewPagerAdapter(this);
        binding.viewpager2.setAdapter(pagerAdapter);
        attachTablayoutWithViewPager();
    }

    private void attachTablayoutWithViewPager(){
        new TabLayoutMediator(binding.mainTablayout, binding.viewpager2, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Audios");
                    break;
                case 1:
                    tab.setText("Favorites");
                    break;
                case 2:
                    tab.setText("Playlists");
                    break;
            }
        }).attach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        return super.onCreateOptionsMenu(menu);
    }
}