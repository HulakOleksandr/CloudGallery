package com.gulaxoft.cloudgallery.uihelper;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.gulaxoft.cloudgallery.Const;
import com.gulaxoft.cloudgallery.R;
import com.gulaxoft.cloudgallery.activity.MainActivity;
import com.gulaxoft.cloudgallery.entity.Category;
import com.gulaxoft.cloudgallery.util.GlobalData;

import java.util.ArrayList;

/**
 * Created by gos on 18.12.16.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> implements Const {

    private ArrayList<Category> items = new ArrayList<>();
    private MainActivity mActivity; // TODO refactor to remove this link

    public CategoryAdapter(MainActivity activity) {
        GlobalData.INSTANCE.getCategoriesRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                items.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Category category = new Category();
                    String id = snapshot.getKey();
                    category.setId(id);
                    category.setName(snapshot.child(CAT_NAME).getValue().toString());
                    category.setDescription(snapshot.child(CAT_DESC).getValue().toString());
                    category.setLastUpdate(Long.parseLong(snapshot.child(CAT_LAST).getValue().toString()));

                    items.add(category);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Read Category failed: " + databaseError.getMessage());
            }
        });
        mActivity = activity;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        final Category category = items.get(position);
        holder.setName(category.getName());
        if (category.getLastUpdate() == 0) {
            holder.setLastUpdate("No images");
        } else {
            String date = DateFormat.format("dd.MM.yyyy", category.getLastUpdate()).toString();
            holder.setLastUpdate(mActivity.getString(R.string.ph_date, date));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.displayCategoryDetails(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
