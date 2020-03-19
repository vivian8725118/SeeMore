package com.vivian.seemore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    SeeMoreAdapter mAdapter;
    List<Item> mList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview);
        initSeeMoreRecyclerView();

    }


    void initSeeMoreRecyclerView(){
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new SeeMoreItemAdapter(this);
        for (int i = 0; i < 3; i++) {
            Item item = new Item();
            item.title = "this is item" + i;
            mList.add(item);
        }
        mAdapter.setmList(mList);
        mRecyclerView.setAdapter(mAdapter);
    }
}
