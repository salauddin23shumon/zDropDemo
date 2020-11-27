package com.sss.zdropdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (savedInstanceState == null) {
            fragment = new ProductFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

        }
    }

    public void arrowClick(View view) {
        Toast.makeText(this, "arrow click", Toast.LENGTH_SHORT).show();
    }

    public void imgViewClick(View view) {
        Toast.makeText(this, "image click", Toast.LENGTH_SHORT).show();
    }

}