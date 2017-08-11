package com.example.teradonburi.stickyheader;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.teradonburi.stickyheader.databinding.ViewHeaderBinding;
import com.example.teradonburi.stickyheader.databinding.ViewItemBinding;

import java.util.ArrayList;
import java.util.List;

import static com.example.teradonburi.stickyheader.Item.Type.HEADER;

/**
 * Created by daiki on 2017/08/11.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter
        implements StickyHeaderItemDecoration.StickyHeaderInterface
{
    private List<Item> items = new ArrayList<>();
    private String headerTitle;

    public void setItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    // 数
    @Override
    public int getItemCount() {
        return items.size();
    }

    // データの種別
    @Override
    public int getItemViewType(int position) {
        return items.get(position).type.ordinal();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == HEADER.ordinal()){
            return HeaderViewHolder.create(parent);
        }
        else{
            return ItemViewHolder.create(parent);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeaderViewHolder){
            ((HeaderViewHolder)holder).update(items.get(position));
        }else if(holder instanceof ItemViewHolder){
            ((ItemViewHolder)holder).update(items.get(position));
        }
    }

    private interface ViewHolderInterface{
        void update(Item item);
    }


    //region HeaderViewHolder

    private static class HeaderViewHolder extends RecyclerView.ViewHolder
        implements ViewHolderInterface
    {
        private ViewHeaderBinding binding;

        public HeaderViewHolder(ViewHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public static HeaderViewHolder create(ViewGroup parent){
            ViewHeaderBinding binding =  DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.view_header,parent,false);
            return new HeaderViewHolder(binding);
        }

        @Override
        public void update(Item item){
            binding.setItem(item);
            binding.executePendingBindings();
        }

    }

    //endregion


    //region ItemViewHolder

    private static class ItemViewHolder extends RecyclerView.ViewHolder
            implements ViewHolderInterface
    {
        private ViewItemBinding binding;

        public ItemViewHolder(ViewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public static ItemViewHolder create(ViewGroup parent){
            ViewItemBinding binding =  DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.view_item,parent,false);
            return new ItemViewHolder(binding);
        }

        @Override
        public void update(Item item){
            binding.setItem(item);
            binding.executePendingBindings();
        }

    }

    //endregion


    //region StickyHeaderInterface

    // Stickyヘッダーの前の位置取得
    @Override
    public int getHeaderPositionForItem(int itemPosition) {
        int headerPosition = -1;
        do {
            if (this.isHeader(itemPosition)) {
                headerPosition = itemPosition;
                break;
            }
            itemPosition -= 1;
        } while (itemPosition >= 0);
        return headerPosition;
    }

    // Stickyヘッダーレイアウト取得
    @Override
    public int getHeaderLayout(int headerPosition) {
        return R.layout.view_sticky_header;
    }

    // Stickyヘッダーのデータバインド
    @Override
    public void bindHeaderData(View header, int headerPosition) {

        if(items.get(headerPosition).type == HEADER){
            TextView headerTextView = (TextView) header.findViewById(R.id.header);
            headerTextView.setText(items.get(headerPosition).text);
            if(TextUtils.isEmpty(headerTitle) || !TextUtils.equals(headerTitle,items.get(headerPosition).text)){
                headerTitle = items.get(headerPosition).text;
            }
        }

    }
    // Stickyヘッダーの判定
    @Override
    public boolean isHeader(int itemPosition) {

        if(items.get(itemPosition).type == HEADER){
            return true;
        }

        return false;
    }

    //endregion
}
