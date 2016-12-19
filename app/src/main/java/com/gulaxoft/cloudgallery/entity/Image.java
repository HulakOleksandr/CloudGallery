package com.gulaxoft.cloudgallery.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by gos on 17.12.16.
 */

@NoArgsConstructor
@AllArgsConstructor
public class Image implements Comparable<Image>, Parcelable {
    @Getter(onMethod=@__({@Exclude}))
    @Setter private String id;
    @Getter @Setter private String name;
    @Getter @Setter private long timestamp;
    @Getter @Setter private String format;

    @Override
    public int compareTo(Image img) {
        return Long.valueOf(img.getTimestamp()).compareTo(timestamp);
    }

    @Exclude
    public String getFileName() {
        return id.concat(format);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeLong(timestamp);
        dest.writeString(format);
    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    private Image(Parcel src) {
        id = src.readString();
        name = src.readString();
        timestamp = src.readLong();
        format = src.readString();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Image && id != null && ((Image) o).getId() != null && ((Image) o).getId().equals(id);
    }
}
