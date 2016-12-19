package com.gulaxoft.cloudgallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gulaxoft.cloudgallery.entity.Category;
import com.gulaxoft.cloudgallery.entity.Image;
import com.gulaxoft.cloudgallery.view.CategoryAdapter;

public class MainActivity extends AppCompatActivity implements Const {
    private DatabaseReference mCategoriesRef;
    private DatabaseReference mImagesRef;
    private final int REQ_ADD_CATEGORY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mCategoriesRef = FirebaseDatabase.getInstance().getReference(CATEGORIES);
        mImagesRef = FirebaseDatabase.getInstance().getReference(IMAGES);

        CategoryAdapter adapter = new CategoryAdapter(mCategoriesRef);

        RecyclerView rvCategories = (RecyclerView) findViewById(R.id.rv_categories);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(false);

        rvCategories.setHasFixedSize(false);
        rvCategories.setLayoutManager(layoutManager);
        rvCategories.setAdapter(adapter);
    }

    private void addCategory(Category category) {
        category.setId(mCategoriesRef.push().getKey());
        mCategoriesRef.child(category.getId()).child(CAT_NAME).setValue(category.getName());
        mCategoriesRef.child(category.getId()).child(CAT_DESC).setValue(category.getDescription());
        mCategoriesRef.child(category.getId()).child(CAT_LAST).setValue(category.getLastAddedImage() != null
                ? category.getLastAddedImage().getTimestamp() : 0);
        for (String imageId : category.getImagesIds()) {
            mCategoriesRef.child(category.getId()).child(IMAGES).child(imageId).setValue(true);
        }

        for (Image img : category.getImages()) {
            addImage(img, "remove it");
        }
    }

    private void addImage(Image image, String categoryId) {
        image.setId(mImagesRef.push().getKey());
        mImagesRef.child(image.getId()).setValue(image);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add_category:
                Intent intent = new Intent(this, AddCategoryActivity.class);
                startActivityForResult(intent, REQ_ADD_CATEGORY);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_ADD_CATEGORY && resultCode == RESULT_OK) {
            Category category = data.getParcelableExtra(EXTRA_CATEGORY);
            addCategory(category);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
