package com.gulaxoft.cloudgallery.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gulaxoft.cloudgallery.Const;
import com.gulaxoft.cloudgallery.R;
import com.gulaxoft.cloudgallery.entity.Category;
import com.gulaxoft.cloudgallery.fragment.CategoryDetailsFragment;
import com.gulaxoft.cloudgallery.fragment.CategoryListFragment;

public class MainActivity extends AppCompatActivity implements Const {
    private DatabaseReference mCategoriesRef;
    private DatabaseReference mImagesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (savedInstanceState == null) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        mCategoriesRef = FirebaseDatabase.getInstance().getReference(CATEGORIES);
        mImagesRef = FirebaseDatabase.getInstance().getReference(IMAGES);

        if (savedInstanceState == null) {
            CategoryListFragment categoryListFragment = new CategoryListFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.fragment, categoryListFragment, CAT_LIST_FRAGMENT);
            ft.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        CategoryDetailsFragment detailsFragment = (CategoryDetailsFragment) getFragmentManager().findFragmentByTag(CAT_DETAILS_FRAGMENT);
        if (detailsFragment != null && detailsFragment.isVisible()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            setTitle(getString(R.string.app_name));
        }
        super.onBackPressed();
    }

    @Override
    public void setTitle(CharSequence title) {
        ((TextView) findViewById(R.id.toolbar_title)).setText(title);
    }

    public void showBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void displayCategoryDetails(Category category) {
        CategoryDetailsFragment categoryDetailsFragment = new CategoryDetailsFragment();
        categoryDetailsFragment.init(category);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, categoryDetailsFragment, CAT_DETAILS_FRAGMENT);
        ft.addToBackStack(CAT_DETAILS_FRAGMENT);
        ft.commit();
        setTitle(category.getName());
    }

    public DatabaseReference getCategoriesRef() {
        return mCategoriesRef;
    }

    public DatabaseReference getImagesRef() {
        return mImagesRef;
    }
}
