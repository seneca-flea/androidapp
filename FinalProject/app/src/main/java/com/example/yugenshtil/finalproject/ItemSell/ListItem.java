package com.example.yugenshtil.finalproject.ItemSell;

/**
 * Created by yugenshtil on 05/11/16.
 */
public class ListItem {
    private String title;
    private int imageResId;
    private String subTitle;
    private boolean favourite = false;

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
