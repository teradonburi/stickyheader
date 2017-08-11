package com.example.teradonburi.stickyheader;

/**
 * Created by daiki on 2017/08/11.
 */

public class Item {
    public enum Type{
        HEADER,
        ITEM
    }

    public Type type;
    public String text;

    public Item(Type type,String text){
        this.type = type;
        this.text = text;
    }
}
