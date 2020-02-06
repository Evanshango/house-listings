package com.evans.multiimageupload.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.evans.multiimageupload.R;
import com.evans.multiimageupload.models.ImageUrl;

import java.util.Objects;

public class ImageItemUrlFragment extends Fragment {

    private ImageView mImageView;
    private ImageUrl mImageUrlUrl;

    public static ImageItemUrlFragment getInstance(ImageUrl imageUrl) {
        ImageItemUrlFragment fragment = new ImageItemUrlFragment();
        if (imageUrl != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("imageUrl", imageUrl);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImageUrlUrl = getArguments().getParcelable("imageUrl");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.image_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mImageView = view.findViewById(R.id.image);
        init();
    }

    private void init() {
        if (mImageUrlUrl != null) {
            Glide.with(Objects.requireNonNull(getActivity()))
                    .load(mImageUrlUrl.getImgUrl())
                    .into(mImageView);
        } else {
            mImageView.setImageURI(null);
        }
    }
}
