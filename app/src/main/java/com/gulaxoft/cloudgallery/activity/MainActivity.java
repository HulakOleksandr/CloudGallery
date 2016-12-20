package com.gulaxoft.cloudgallery.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.gulaxoft.cloudgallery.Const;
import com.gulaxoft.cloudgallery.R;
import com.gulaxoft.cloudgallery.entity.Category;
import com.gulaxoft.cloudgallery.fragment.CategoryDetailsFragment;
import com.gulaxoft.cloudgallery.fragment.CategoryListFragment;

public class MainActivity extends AppCompatActivity implements Const {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (savedInstanceState == null) {
            CategoryListFragment categoryListFragment = new CategoryListFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.fragment, categoryListFragment, CAT_LIST_FRAGMENT);
            ft.commit();
        } else {
            setTitle(savedInstanceState.getString(EXTRA_TITLE));
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(EXTRA_TITLE, ((TextView) findViewById(R.id.toolbar_title)).getText().toString());
        super.onSaveInstanceState(outState);
    }

    public void displayCategoryDetails(Category category) {
        CategoryDetailsFragment categoryDetailsFragment = new CategoryDetailsFragment();
        categoryDetailsFragment.init(category);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.fragment, categoryDetailsFragment, CAT_DETAILS_FRAGMENT);
        ft.addToBackStack(CAT_DETAILS_FRAGMENT);
        ft.commit();
        setTitle(category.getName());
    }
}
