package com.gulaxoft.cloudgallery.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gulaxoft.cloudgallery.R;

/**
 * Created by gos on 18.12.16.
 */

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    private final TextView mNameField;
    private final TextView mLastField;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        mNameField = (TextView) itemView.findViewById(R.id.tv_name);
        mLastField = (TextView) itemView.findViewById(R.id.tv_last_update);
    }

    public void setName(String name) {
        mNameField.setText(name);
    }

    public void setLastUpdate(String lastUpdateDate) {
        mLastField.setText(lastUpdateDate);
    }
}
