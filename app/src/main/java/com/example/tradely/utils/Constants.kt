package com.example.tradely.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {

    const val USERS: String = "users"
    const val PRODUCTS:String = "products"

    const val TRADELY_PREFERENCES: String = "TradelyPrefs"
    const val USER_ID:String = "user_id"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_USER_DETAILS:String = "extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 2
    const val FIRST_NAME:String = "firstName"
    const val LAST_NAME:String = "lastName"
    const val MALE:String = "male"
    const val FEMALE:String = "female"
    const val MOBILE:String = "mobile"
    const val GENDER:String = "gender"
    const val COMPLETE_PROFILE:String = "profileCompleted"
    const val IMAGE:String = "image"
    const val USER_PROFILE_IMAGE:String = "User_Profile_Image"
    const val PRODUCT_IMAGE:String = "Product_Image"
    const val EXTRA_PRODUCT_ID:String = "extra_product_id"
    const val EXTRA_PRODUCT_OWNER_ID:String = "extra_product_owner_id"
    const val DEFAULT_CART_QUANTITY:String = "1"
    const val CART_ITEMS:String = "cart_items"
    const val PRODUCT_ID:String = "product_id"
    const val CART_QUANTITY:String = "cart_quantity"
    const val HOME: String = "Home"
    const val OFFICE: String = "Office"
    const val OTHER: String = "Other"
    const val ADDRESS:String = "address"
    const val EXTRA_ADDRESS_DETAILS:String = "AddressDetails"

    fun showImageChooser(activity: Activity) {
        // An intent for launching the image selection of phone storage.
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        // Launches the image selection of phone storage using the constant code.
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    /**
     * A function to get the image file extension of the selected image.
     *
     * @param activity Activity reference.
     * @param uri Image file uri.
     */
    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        /*
         * MimeTypeMap: Two-way map that maps MIME-types to file extensions and vice versa.
         *
         * getSingleton(): Get the singleton instance of MimeTypeMap.
         *
         * getExtensionFromMimeType: Return the registered extension for the given MIME type.
         *
         * contentResolver.getType: Return the MIME type of the given content URL.
         */
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}