package com.gulaxoft.cloudgallery.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.gulaxoft.cloudgallery.Const;
import com.gulaxoft.cloudgallery.R;
import com.gulaxoft.cloudgallery.activity.AddCategoryActivity;
import com.gulaxoft.cloudgallery.activity.MainActivity;
import com.gulaxoft.cloudgallery.entity.Category;
import com.gulaxoft.cloudgallery.uihelper.CategoryAdapter;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by gos on 19.12.16.
 */

public class CategoryListFragment extends Fragment implements Const {

    private DatabaseReference mCategoriesRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category_list, null);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add_category:
                Intent intent = new Intent(getActivity(), AddCategoryActivity.class);
                startActivityForResult(intent, REQ_ADD_CATEGORY);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCategoriesRef = ((MainActivity) getActivity()).getCategoriesRef();
        CategoryAdapter adapter = new CategoryAdapter(mCategoriesRef, (MainActivity) getActivity());
        RecyclerView rvCategories = (RecyclerView) getActivity().findViewById(R.id.rv_categories);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(false);

        rvCategories.setHasFixedSize(false);
        rvCategories.setLayoutManager(layoutManager);
        rvCategories.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_ADD_CATEGORY && resultCode == RESULT_OK) {
            Category category = data.getParcelableExtra(EXTRA_CATEGORY);
            postCategory(category);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void postCategory(Category category) {
        category.setId(mCategoriesRef.push().getKey());
        Map<String, Object> catUpdates = new HashMap<>();
        catUpdates.put(CAT_NAME, category.getName());
        catUpdates.put(CAT_DESC, category.getDescription());
        catUpdates.put(CAT_LAST, category.getLastAddedImage() != null
                ? category.getLastAddedImage().getTimestamp() : 0);
        Map<String, Object> imagesLinks = new HashMap<>();
        for (String imageId : category.getImagesIds()) {
            imagesLinks.put(imageId, true);
        }
        catUpdates.put(IMAGES, imagesLinks);

        mCategoriesRef.child(category.getId()).updateChildren(catUpdates);
    }
}
