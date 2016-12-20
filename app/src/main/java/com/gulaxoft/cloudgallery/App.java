package com.gulaxoft.cloudgallery;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.gulaxoft.cloudgallery.util.GlobalData;

/**
 * Created by gos on 20.12.16.
 */

public class App extends Application implements Const {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(IMG_STORAGE_URL);

        GlobalData.INSTANCE.setCategoriesRef(FirebaseDatabase.getInstance().getReference(CATEGORIES));
        GlobalData.INSTANCE.setImagesDataRef(FirebaseDatabase.getInstance().getReference(IMAGES));
        GlobalData.INSTANCE.setImagesStorageRef(storageRef.child(IMAGES));
    }
}
