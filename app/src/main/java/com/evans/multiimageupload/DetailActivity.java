package com.evans.multiimageupload;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.evans.multiimageupload.adapters.ImageUriPagerAdapter;
import com.evans.multiimageupload.fragments.ImageItemUrlFragment;
import com.evans.multiimageupload.models.House;
import com.evans.multiimageupload.models.ImageUrl;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";
    private TabLayout detailTabLayout;
    private ViewPager detailImgViewPager;
    private House mHouse;
    private List<ImageUrl> mImageUrlList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        mHouse = intent.getParcelableExtra("house");
        if (mHouse != null){
            mImageUrlList = mHouse.getImageUrls();
        } else {
            Log.d(TAG, "onCreate: No arguments passed");
        }

        initViews();

        initImageFragments();
    }

    private void initImageFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (ImageUrl imageUrl : mImageUrlList){
            ImageItemUrlFragment fragment = ImageItemUrlFragment.getInstance(imageUrl);
            fragments.add(fragment);
        }
        ImageUriPagerAdapter pagerAdapter = new ImageUriPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments);
        detailImgViewPager.setAdapter(pagerAdapter);
        detailTabLayout.setupWithViewPager(detailImgViewPager, true);
    }

    private void initViews() {
        detailImgViewPager = findViewById(R.id.detailImgViewPager);
        detailTabLayout = findViewById(R.id.detailTabLayout);
    }
}
