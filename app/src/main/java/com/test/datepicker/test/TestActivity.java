package com.test.datepicker.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.test.datepicker.R;

/**
 * Created by Administrator on 2019\5\30 0030.
 */

public class TestActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);
        setContentView(R.layout.view_date_picker);
    }
}
