package com.gulaxoft.cloudgallery.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.gulaxoft.cloudgallery.Const;
import com.gulaxoft.cloudgallery.R;
import com.gulaxoft.cloudgallery.entity.Category;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by gos on 18.12.16.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> implements Const {

    private ArrayList<Category> items = new ArrayList<>();
    private Context mContext;

    public CategoryAdapter(DatabaseReference ref) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                items.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Category category = new Category();
                    String id = snapshot.getKey();
                    category.setId(id);
                    category.setName(snapshot.child(CAT_NAME).getValue().toString());
                    category.setDescription(snapshot.child(CAT_DESC).getValue().toString());

                    items.add(category);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Read Category failed: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_item, parent, false);
        mContext = parent.getContext();
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        final Category category = items.get(position);
        holder.setName(category.getName());
        holder.setLastUpdate(category.getLastAddedImage() != null
                ? new Date(category.getLastAddedImage().getTimestamp() * 1000).toString()
                : "No images");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO open activity with category details and request its images from Firebase in it
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
