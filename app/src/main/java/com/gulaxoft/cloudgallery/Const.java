package com.gulaxoft.cloudgallery;

/**
 * Created by gos on 18.12.16.
 */

public interface Const {
    String TAG = "CloudGallery";

    // Firebase
    String CATEGORIES = "categories";
    String IMAGES = "images";
    String CAT_NAME = "name";
    String CAT_DESC = "description";
    String CAT_LAST = "lastUpdate";

    // Extras
    String EXTRA_CATEGORY = "category";
    String EXTRA_IMAGE = "image";

    // Intent
    int REQ_ADD_CATEGORY = 1;

    // Fragments
    String CAT_LIST_FRAGMENT = "CategoryListFragment";
    String CAT_DETAILS_FRAGMENT = "CategoryDetailsFragment";

}
