package com.example.teradonburi.stickyheader;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.example.teradonburi.stickyheader.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        recyclerViewAdapter = new RecyclerViewAdapter();

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(recyclerViewAdapter);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        binding.recyclerView.addItemDecoration(new StickyHeaderItemDecoration(recyclerViewAdapter,1, 0));

        recyclerViewAdapter.setItems(createItems());
    }

    public List<Item> createItems(){

        List<Item> items = new ArrayList<>();
        for(int i = 0;i < 100;++i){
            Item item = null;
            boolean isHeader = Math.random() <= 0.25;
            if(isHeader){
                item = new Item(Item.Type.HEADER,"header" + i);
            }
            else{
                item = new Item(Item.Type.ITEM,"item" + i);
            }
            items.add(item);
        }

        return items;
    }

}
