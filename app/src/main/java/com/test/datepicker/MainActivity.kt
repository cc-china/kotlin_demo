package com.test.datepicker

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.test.datepicker.test.TestActivity
import kotlinx.android.synthetic.main.main_activity.*

/**
 * Created by Administrator on 2019\5\20 0020.
 *
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        button.setOnClickListener {
            startActivity(Intent(this, TestActivity::class.java))
        }
    }

}

