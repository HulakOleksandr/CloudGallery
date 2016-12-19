package com.gulaxoft.cloudgallery.uihelper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;
import com.gulaxoft.cloudgallery.Const;
import com.gulaxoft.cloudgallery.R;
import com.gulaxoft.cloudgallery.entity.Image;

import java.util.List;

/**
 * Created by gos on 19.12.16.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> implements Const {

    private List<Image> mImages;
    private Context mContext;
    private StorageReference mImagesStorageRef;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }

    public GalleryAdapter(Context context, List<Image> images, StorageReference imagesStorageRef) {
        mContext = context;
        mImages = images;
        mImagesStorageRef = imagesStorageRef;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_thumbnail, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Image image = mImages.get(position);

        Glide.with(mContext)
                .using(new FirebaseImageLoader())
                .load(mImagesStorageRef.child(image.getFileName()))
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.thumbnail);

//        // TODO Fix fullscreen viewing
//        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//                    v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
//                    v.buildDrawingCache(true);
//                    Bitmap bitmap = v.getDrawingCache(true).copy(Bitmap.Config.RGB_565, false);
//                    v.destroyDrawingCache();
//
//                    holder.thumbnail.setImageBitmap(bitmap);
//
//                    FileOutputStream fos = new FileOutputStream(mContext.getCacheDir().getPath().concat("/").concat("fullScreenImage.jpg"));
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
//                    fos.close();
//
//                    File file = new File(mContext.getCacheDir(), "fullScreenImage.jpg");
//
//                    Intent intent = new Intent();
//                    intent.setAction(Intent.ACTION_VIEW);
//                    intent.setDataAndType(Uri.fromFile(file), "image/*");
//                    v.getContext().startActivity(intent);
//
//                } catch (ActivityNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    Log.e(TAG, "Failed to create cache file for displaying image in fullscreen : " + e.getMessage());
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

}