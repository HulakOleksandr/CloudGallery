package com.gulaxoft.cloudgallery.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * Created by gos on 17.12.16.
 */

@NoArgsConstructor
@AllArgsConstructor
public class Category implements Parcelable {
    @Getter @Setter private String id;
    @Getter @Setter private String name;
    @Getter @Setter private String description;
    @Setter @Exclude private long lastUpdate;
    @Getter @Setter @NonNull private List<Image> images = new ArrayList<Image>() {
        @Override
        public boolean add(Image object) {
            int index = Collections.binarySearch(this, object);
            if (index < 0) index = ~index;
            super.add(index, object);
            return true;
        }
    };

    public Image getLastAddedImage() {
        return images.size() > 0 ? images.get(0) : null;
    }

    public List<String> getImagesIds() {
        List<String> result = new ArrayList<>();
        for (Image img : images) {
            result.add(img.getId());
        }
        return result;
    }

    public long getLastUpdate() {
        return getLastAddedImage() == null ? lastUpdate : getLastAddedImage().getTimestamp();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeList(images);
    }

    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    private Category(Parcel src) {
        id = src.readString();
        name = src.readString();
        description = src.readString();
        images = src.readArrayList(Image.class.getClassLoader());
    }
}
