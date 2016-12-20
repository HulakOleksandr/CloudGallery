package com.gulaxoft.cloudgallery.uihelper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;
import com.gulaxoft.cloudgallery.Const;
import com.gulaxoft.cloudgallery.R;
import com.gulaxoft.cloudgallery.entity.Image;
import com.gulaxoft.cloudgallery.event.DeleteImageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by gos on 19.12.16.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ImageHolder> implements Const {

    private List<Image> mImages;
    private Context mContext;
    private StorageReference mImagesStorageRef;

    public class ImageHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView tvName;
        public TextView tvDate;
        public ImageView btnDelete;
        public ProgressBar progressBar;

        public ImageHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvDate = (TextView) view.findViewById(R.id.tv_date);
            btnDelete = (ImageView) view.findViewById(R.id.img_delete);
            progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        }
    }

    public GalleryAdapter(Context context, List<Image> images, StorageReference imagesStorageRef) {
        mContext = context;
        mImages = images;
        mImagesStorageRef = imagesStorageRef;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_thumbnail, parent, false);

        return new ImageHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ImageHolder holder, int position) {
        final Image image = mImages.get(position);

        Glide.with(mContext)
                .using(new FirebaseImageLoader())
                .load(mImagesStorageRef.child(image.getFileName()))
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.thumbnail);

        holder.tvName.setText(image.getName());
        String date = DateFormat.format("dd.MM.yyyy", image.getTimestamp()).toString();
        holder.tvDate.setText(date);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DeleteImageEvent(image));
            }
        });

//      TODO Fullscreen viewing
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

}