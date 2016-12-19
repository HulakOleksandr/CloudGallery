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
    String IMG_PREFIX = "img";
    String IMG_STORAGE_URL = "gs://cloud-gallery-b02ec.appspot.com";

    // Extras
    String EXTRA_CATEGORY = "category";
    String EXTRA_IMAGE = "image";

    // Intent
    int REQ_ADD_CATEGORY = 100;
    int REQ_MAKE_PHOTO = 101;
    int REQ_CHOOSE_IMG = 102;

    // Fragments
    String CAT_LIST_FRAGMENT = "CategoryListFragment";
    String CAT_DETAILS_FRAGMENT = "CategoryDetailsFragment";

}
