package com.gulaxoft.cloudgallery.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by gos on 20.12.16.
 */

public enum GlobalData {
    INSTANCE;

    @Getter @Setter private DatabaseReference categoriesRef;
    @Getter @Setter private DatabaseReference imagesDataRef;
    @Getter @Setter private StorageReference imagesStorageRef;
}
