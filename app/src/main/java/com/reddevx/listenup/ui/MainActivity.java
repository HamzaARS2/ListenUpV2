package com.reddevx.listenup.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.reddevx.listenup.R;
import com.reddevx.listenup.databinding.ActivityMainBinding;
import com.reddevx.listenup.service.AudioService;
import com.reddevx.listenup.ui.adapters.ViewPagerAdapter;
import com.reddevx.listenup.ui.fragments.AudioControllerFragment;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {


    private static final int PERMISSION_REQ_CODE = 1;
    private ActivityMainBinding binding;
    private ViewPagerAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        setSupportActionBar(binding.mainToolbar);
        buildViewPager();
        requestPermission();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });




    }




    @Override
    protected void onResume() {
        super.onResume();
        showBottomAudioIfRunning();
    }

    public void showBottomAudioIfRunning( ){
        if (AudioService.mp != null) {
            binding.controllerContainer.setVisibility(View.VISIBLE);
            AudioControllerFragment fragment = AudioControllerFragment.newInstance(AudioService.audioIndex, AudioService.mp.isPlaying());
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setReorderingAllowed(true);
            ft.add(R.id.controller_container, fragment);
            ft.commit();
        }else {
            binding.controllerContainer.setVisibility(View.GONE);
        }
    }

    private void buildViewPager(){
        pagerAdapter = new ViewPagerAdapter(this);
        binding.viewpager2.setAdapter(pagerAdapter);
        attachTabLayoutWithViewPager();
    }

    private void attachTabLayoutWithViewPager(){
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


    // Menu may be available next update
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        return super.onCreateOptionsMenu(menu);
    }
*/

    public void requestPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQ_CODE);
            }else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQ_CODE);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQ_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
            binding.viewpager2.setVisibility(View.VISIBLE);
            //binding.requestPermBtn.setVisibility(View.GONE);
            binding.permissionNeededTv.setVisibility(View.GONE);

        }else {
            binding.viewpager2.setVisibility(View.GONE);
            //binding.requestPermBtn.setVisibility(View.VISIBLE);
            binding.permissionNeededTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PlayerActivity.isShufflePressed = false;
    }
}