package com.gulaxoft.cloudgallery.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gulaxoft.cloudgallery.Const;
import com.gulaxoft.cloudgallery.R;
import com.gulaxoft.cloudgallery.entity.Category;
import com.gulaxoft.cloudgallery.entity.Image;
import com.gulaxoft.cloudgallery.event.DeleteImageEvent;
import com.gulaxoft.cloudgallery.uihelper.GalleryAdapter;
import com.gulaxoft.cloudgallery.util.FileUtils;
import com.gulaxoft.cloudgallery.util.GlobalData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by gos on 19.12.16.
 */

public class CategoryDetailsFragment extends Fragment implements Const {

    private DatabaseReference mImagesDataRef;
    private StorageReference mImagesStorageRef;
    private Category mCategory;
    private GalleryAdapter mGalleryAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(IMG_STORAGE_URL);
        mImagesStorageRef = storageRef.child(IMAGES);

        if (savedInstanceState != null) {
            mCategory = savedInstanceState.getParcelable(EXTRA_CATEGORY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category_details, null);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_category_details, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_choose_a_photo:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Choose an image"), REQ_CHOOSE_IMG);
                break;
            case R.id.action_make_a_photo:
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, REQ_MAKE_PHOTO);
                }
                break;
        }
        return true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mImagesDataRef = GlobalData.INSTANCE.getImagesDataRef();

        // load all images of this category
        GlobalData.INSTANCE.getCategoriesRef()
                .child(mCategory.getId())
                .child(IMAGES)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCategory.getImages().clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final String id = snapshot.getKey();
                    mImagesDataRef.child(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Image image = dataSnapshot.getValue(Image.class);
                            if (image != null) {
                                image.setId(id);
                                if (mCategory.getImages().contains(image))
                                    mCategory.getImages().remove(image);
                                mCategory.getImages().add(image);
                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.rv_images_of_category);
        mGalleryAdapter = new GalleryAdapter(getActivity(), mCategory.getImages());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mGalleryAdapter);

        updateInfoViews();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_MAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();

            Bitmap imageBitmap = (Bitmap) extras.get("data");
            byte[] bmpData = FileUtils.bitmapToByteArray(imageBitmap);

            Image image = new Image();
            image.setFormat(".jpg");
            image.setTimestamp(System.currentTimeMillis());
            image.setId(mImagesDataRef.push().getKey());
            String time = DateFormat.format("dd-MM-yyyy HH:mm:ss", image.getTimestamp()).toString();
            image.setName(IMG_PREFIX.concat(time));

            postImage(image);

            UploadTask uploadTask = mImagesStorageRef.child(image.getFileName()).putBytes(bmpData);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mGalleryAdapter.notifyDataSetChanged();
                }
            });
        } else if (requestCode == REQ_CHOOSE_IMG && resultCode == Activity.RESULT_OK) {
            // TODO compress big images
            Uri uri = data.getData();
            String path = FileUtils.getPath(getActivity(), uri);

            Image image = new Image();
            image.setFormat(FileUtils.getFileExtension(path));
            image.setTimestamp(System.currentTimeMillis());
            image.setId(mImagesDataRef.push().getKey());
            image.setName(FileUtils.getFileName(path));

            postImage(image);
            UploadTask uploadTask = mImagesStorageRef.child(image.getFileName()).putFile(uri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mGalleryAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(EXTRA_CATEGORY, mCategory);
        super.onSaveInstanceState(outState);
    }

    public void init(Category category) {
        this.mCategory = category;
    }

    public void postImage(Image image) {
        mImagesDataRef.child(image.getId()).setValue(image);
        mCategory.getImages().add(image);
        updateCategory();
    }

    private void updateCategory() {
        Map<String, Object> catUpdates = new HashMap<>();
        catUpdates.put(CAT_NAME, mCategory.getName());
        catUpdates.put(CAT_DESC, mCategory.getDescription());
        catUpdates.put(CAT_LAST, mCategory.getLastAddedImage() != null
                ? mCategory.getLastAddedImage().getTimestamp() : 0);
        Map<String, Object> imagesLinks = new HashMap<>();
        for (String imageId : mCategory.getImagesIds()) {
            imagesLinks.put(imageId, true);
        }
        catUpdates.put(IMAGES, imagesLinks);

        GlobalData.INSTANCE.getCategoriesRef().child(mCategory.getId()).updateChildren(catUpdates);
        notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteImageEvent(DeleteImageEvent event) {
        Image image = event.getImage();
        mCategory.getImages().remove(image);
        mImagesDataRef.child(image.getId()).removeValue();
        mImagesStorageRef.child(image.getFileName()).delete();
        updateCategory();
        notifyDataSetChanged();
    }

    private void notifyDataSetChanged() {
        mGalleryAdapter.notifyDataSetChanged();
        updateInfoViews();
    }

    private void updateInfoViews() {
        TextView tvOverall = (TextView) getActivity().findViewById(R.id.tv_overall);
        TextView tvLast = (TextView) getActivity().findViewById(R.id.tv_last_image);
        TextView tvNameLast = (TextView) getActivity().findViewById(R.id.tv_last_image_name);
        TextView tvDateLast = (TextView) getActivity().findViewById(R.id.tv_last_image_date);

        int imagesCount = mCategory.getImages().size();
        tvOverall.setText(getString(R.string.ph_overall, imagesCount));

        if (imagesCount == 0) {
            tvLast.setVisibility(View.GONE);
            tvNameLast.setVisibility(View.GONE);
            tvDateLast.setVisibility(View.GONE);
        } else {
            String date = DateFormat.format("dd.MM.yyyy", mCategory.getLastAddedImage().getTimestamp()).toString();
            tvLast.setVisibility(View.VISIBLE);
            tvNameLast.setVisibility(View.VISIBLE);
            tvDateLast.setVisibility(View.VISIBLE);
            tvNameLast.setText(getString(R.string.ph_name, mCategory.getLastAddedImage().getName()));
            tvDateLast.setText(getString(R.string.ph_date, date));
        }
    }
}
