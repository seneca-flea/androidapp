package com.example.yugenshtil.finalproject.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yugenshtil on 05/11/16.
 */
public class DerpData {
    private static final String[] titles = {"title1","title2","title3"};
    private static final int[] icons = {android.R.drawable.ic_popup_reminder,android.R.drawable.ic_menu_add,android.R.drawable.ic_menu_delete};
    private static final String[] subTitles = {"subtitle1","subtitle2","subtitle3"};
    private static final int icon = android.R.drawable.ic_partial_secure;


    public static List<ListItem> getListData(){
        List<ListItem> data = new ArrayList<>();

            for(int x = 0;x<4;x++){
                for(int i = 0;i < titles.length && i < icons.length;i++){
                    ListItem item = new ListItem();
                    item.setSubTitle(subTitles[i]);
                    item.setTitle(titles[i]);
                    data.add(item);


                }

            }
        return data;
    }


}
