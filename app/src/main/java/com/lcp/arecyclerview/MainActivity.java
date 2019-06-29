package com.lcp.arecyclerview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lcp.arecyclerview.activity.GridMoreActivity;
import com.lcp.arecyclerview.activity.GridMoreActivity2;
import com.lcp.arecyclerview.activity.HeaderActivity;
import com.lcp.arecyclerview.activity.ItemTouchHelperActivity;
import com.lcp.arecyclerview.activity.ListMoreActivity;
import com.lcp.arecyclerview.activity.ListMoreActivity2;
import com.lcp.arecyclerview.activity.ScrollerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void listMore(View view) {
        startActivity(new Intent(this,ListMoreActivity.class));
    }

    public void gridMore(View view) {
        startActivity(new Intent(this,GridMoreActivity.class));
    }

    public void listMore2(View view) {
        startActivity(new Intent(this,ListMoreActivity2.class));
    }

    public void gridMore2(View view) {
        startActivity(new Intent(this,GridMoreActivity2.class));
    }

    public void header(View view) {
        startActivity(new Intent(this,HeaderActivity.class));
    }

    public void scroller(View view) {
        startActivity(new Intent(this,ScrollerActivity.class));
    }

    public void itemTouchHelper(View view) {
        startActivity(new Intent(this, ItemTouchHelperActivity.class));
    }
}
