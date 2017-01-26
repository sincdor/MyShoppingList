package com.sincdor.myshoppinglist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class EditItemActivity extends Activity {

    Item it;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
    }

    public void bl_new_item(View view) {
    }

    public void bl_cancel_new_item(View view) {
    }
}
