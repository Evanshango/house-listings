package com.evans.multiimageupload.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.evans.multiimageupload.models.ImageUri;
import com.evans.multiimageupload.R;

public class ImageItemUriFragment extends Fragment {

    private ImageView mImageView;
    private ImageUri mImageUri;

    public static ImageItemUriFragment getInstance(ImageUri imageUri){
        ImageItemUriFragment fragment = new ImageItemUriFragment();
        if (imageUri != null){
            Bundle bundle = new Bundle();
            bundle.putParcelable("imageUri", imageUri);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mImageUri = getArguments().getParcelable("imageUri");
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

    private void init(){
        if (mImageUri != null){
            mImageView.setImageURI(mImageUri.getUri());
        } else {
            mImageView.setImageURI(null);
        }
    }
}
