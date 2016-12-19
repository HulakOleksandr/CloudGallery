package com.gulaxoft.cloudgallery.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.gulaxoft.cloudgallery.Const;
import com.gulaxoft.cloudgallery.R;
import com.gulaxoft.cloudgallery.entity.Category;

public class AddCategoryActivity extends AppCompatActivity implements Const {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_add_category);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_done) {
            String name = ((EditText) findViewById(R.id.et_name)).getText().toString();
            String description = ((EditText) findViewById(R.id.et_description)).getText().toString();
            if (name.trim().length() == 0) {
                Toast.makeText(this, "Please fill field 'Name'", Toast.LENGTH_SHORT).show();
            } else {
                Category category = new Category();
                category.setName(name);
                category.setDescription(description);

                Intent intent = new Intent();
                intent.putExtra(EXTRA_CATEGORY, category);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
