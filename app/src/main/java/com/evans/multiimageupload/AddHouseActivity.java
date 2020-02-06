package com.evans.multiimageupload;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.evans.multiimageupload.adapters.ImageUriPagerAdapter;
import com.evans.multiimageupload.fragments.ImageItemUriFragment;
import com.evans.multiimageupload.models.House;
import com.evans.multiimageupload.models.ImageUri;
import com.evans.multiimageupload.models.ImageUrl;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static com.evans.multiimageupload.helpers.Constants.HOUSES;
import static com.evans.multiimageupload.helpers.Constants.UPLOADS;

public class AddHouseActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Button mChooseImages, mClearImages, mAddHouse;
    private static int GALLERY_PICK = 1001;
    private StorageReference mStorageReference;
    private DatabaseReference houseRef;
    private String houseId;
    private int looper = 0;
    private List<ImageUri> mImageUriList;
    private List<ImageUrl> mImageUrlList;
    private EditText mCategory, mLocation, mDescription;
    private String category, location, desc;
    private ProgressBar uploadProgress, saveProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_house);

        mStorageReference = FirebaseStorage.getInstance().getReference(UPLOADS);
        houseRef = FirebaseDatabase.getInstance().getReference(HOUSES);
        houseId = houseRef.push().getKey();

        initViews();

        mChooseImages.setOnClickListener(v -> openGallery());

        mClearImages.setOnClickListener(v -> clearImages());

        mAddHouse.setOnClickListener(v -> saveHouseDetails());
    }

    private void clearImages() {
        mImageUriList.clear();
        init(mImageUriList);
        mChooseImages.setVisibility(View.VISIBLE);
        mClearImages.setVisibility(View.GONE);
    }

    private void init(List<ImageUri> imageUriList) {
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (ImageUri imageUri : imageUriList) {
            ImageItemUriFragment fragment = ImageItemUriFragment.getInstance(imageUri);
            fragments.add(fragment);
        }
        ImageUriPagerAdapter pagerAdapter = new ImageUriPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager, true);
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Images"), GALLERY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                int totalItems = data.getClipData().getItemCount();
                List<Uri> uriList = new ArrayList<>();
                mImageUriList = new ArrayList<>();
                for (int i = 0; i < totalItems; i++) {
                    Uri imgUri = data.getClipData().getItemAt(i).getUri();
                    ImageUri imageUri = new ImageUri(imgUri);
                    uriList.add(imgUri);
                    mImageUriList.add(imageUri);
                }
                mChooseImages.setVisibility(View.GONE);
                mClearImages.setVisibility(View.VISIBLE);
                init(mImageUriList);
                loadSelectedImages(uriList);
            } else if (data.getData() != null) {
                Toast.makeText(this, "Please select at least two images",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No images selected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadSelectedImages(List<Uri> uriList) {
        uploadProgress.setVisibility(View.VISIBLE);
        List<String> imageUrls = new ArrayList<>();
        for (int i = 0; i < uriList.size(); i++) {
            StorageReference fileRef = mStorageReference.child(houseId).child(
                    System.currentTimeMillis() + "." + getFileExtension(uriList.get(i)));
            fileRef.putFile(uriList.get(i)).addOnSuccessListener(taskSnapshot ->
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        looper++;
                        imageUrls.add(uri.toString());
                        if (looper == uriList.size()) {
                            uploadProgress.setVisibility(View.GONE);
                            loadImageLinks(imageUrls);
                        }
                    }).addOnFailureListener(e -> {
                        fileRef.delete();
                        uploadProgress.setVisibility(View.GONE);
                        Toast.makeText(this, "Error " + e, Toast.LENGTH_SHORT).show();
                    })).addOnProgressListener(taskSnapshot -> {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() /
                        taskSnapshot.getTotalByteCount());
                uploadProgress.incrementProgressBy((int) progress);
            });
        }
    }

    private void loadImageLinks(List<String> imageUrls) {
        List<ImageUrl> imageUrlList = new ArrayList<>();
        for (int i = 0; i < imageUrls.size(); i++) {
            String imgUrl = imageUrls.get(i);
            ImageUrl imageUrl = new ImageUrl(imgUrl);
            imageUrlList.add(imageUrl);
        }
        mImageUrlList = new ArrayList<>();
        mImageUrlList.addAll(imageUrlList);
    }

    private void saveHouseDetails() {
        saveProgress.setVisibility(View.VISIBLE);
        category = mCategory.getText().toString().trim();
        location = mLocation.getText().toString().trim();
        desc = mDescription.getText().toString().trim();
        if (mImageUriList != null) {
            if (!category.isEmpty()) {
                if (!location.isEmpty()) {
                    if (!desc.isEmpty()) {
                        doSaveInfo();
                    } else {
                        saveProgress.setVisibility(View.GONE);
                        mDescription.setError("Description can't be empty");
                        mDescription.requestFocus();
                    }
                } else {
                    saveProgress.setVisibility(View.GONE);
                    mLocation.setError("Location can't be empty");
                    mLocation.requestFocus();
                }
            } else {
                saveProgress.setVisibility(View.GONE);
                mCategory.setError("Category can't be empty");
                mCategory.requestFocus();
            }
        } else {
            saveProgress.setVisibility(View.GONE);
            Toast.makeText(this, "No images selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void doSaveInfo() {
        House house = new House(houseId, category, location, desc, "0", mImageUrlList);
        houseRef.child(houseId).setValue(house).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                toHomeActivity();
            } else {
                saveProgress.setVisibility(View.GONE);
                clearImages();
                mCategory.setText("");
                mLocation.setText("");
                mDescription.setText("");
            }
        });
    }

    private void toHomeActivity() {
        saveProgress.setVisibility(View.GONE);
        Toast.makeText(this, "House added successfully", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(this, HomeActivity.class));
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void initViews() {
        mChooseImages = findViewById(R.id.btnChooseImage);
        mViewPager = findViewById(R.id.imagesViewPager);
        mTabLayout = findViewById(R.id.tab_layout);
        mClearImages = findViewById(R.id.btnClearImages);
        mAddHouse = findViewById(R.id.btnAddHouse);
        mCategory = findViewById(R.id.houseCategory);
        mLocation = findViewById(R.id.houseLocation);
        mDescription = findViewById(R.id.houseDesc);
        uploadProgress = findViewById(R.id.uploadProgress);
        saveProgress = findViewById(R.id.saveProgress);
    }
}
