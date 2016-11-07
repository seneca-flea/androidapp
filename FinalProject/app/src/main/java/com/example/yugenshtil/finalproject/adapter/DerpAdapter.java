package com.example.yugenshtil.finalproject.adapter;

import android.content.Context;
import android.media.Image;
//import android.support.v7.internal.view.menu.MenuView;
//import android.support.v7.widget.RecyclerView;
import android.media.browse.MediaBrowser;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.model.ListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yugenshtil on 05/11/16.
 */

public class DerpAdapter extends RecyclerView.Adapter<DerpAdapter.DerpHolder>  {

    private List<ListItem> listData;
    private LayoutInflater inflater;
    private JSONArray itemList;

    private ItemClickCallback itemClickCallBack;

    public interface ItemClickCallback{
        void onItemClick(int p);
        void onSecondaryIconClick(int p);


    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallBack){
        this.itemClickCallBack = itemClickCallBack;

    }

    public DerpAdapter(List<ListItem> listData, Context c,JSONArray jsonArray){
        this.inflater = LayoutInflater.from(c);
        this.listData = listData;
        Log.d("Oleg",jsonArray.toString());
        this.itemList = jsonArray;
    }

    @Override
    public DerpHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

       View view = inflater.inflate(R.layout.list_item,viewGroup,false);

        return new DerpHolder(view);
    }

    @Override
    public void onBindViewHolder(DerpHolder derpHolder, int i) {
        Log.d("Oleg", "Setting onbind "+i);
        String ItemId="";
        //Here it sets to the view
        try {
            JSONObject item = (JSONObject)itemList.get(i);
            ItemId = item.get("ItemId").toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }




        ListItem item = listData.get(i);
        derpHolder.title.setText("Id is " + ItemId);
    //    derpHolder.icon.setImageResource(item.getImageResId());
        derpHolder.tv_id.setText(i+"");

        if(item.isFavourite())
        {
          derpHolder.icon.setImageResource(R.drawable.ic_update_black_36dp);

        }
        else
            derpHolder.icon.setImageResource(R.drawable.ic_picture_in_picture_black_36dp);


    }

    //NEW


    public void setListData(ArrayList<ListItem> exerciseList){
        this.listData.clear();
        this.listData.addAll(exerciseList);

    }

    @Override
    public int getItemCount() {

       Log.d("Oleg","Size of JSON"+itemList.length());
        Log.d("Oleg","Size of listData"+listData.size());
        return itemList.length();
     //   return listData.size();
    }

    class DerpHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        private TextView title;
        private ImageView icon;
        private View container;
        private TextView tv_id;
        //New
        private ImageView thumbnail;
        private ImageView secondaryIcon;


        public DerpHolder(View itemView) {
            super(itemView);

            title =(TextView)itemView.findViewById(R.id.tv_item_text);
            icon =(ImageView)itemView.findViewById(R.id.im_item_icon);
            container = itemView.findViewById(R.id.cont_item_root);
            tv_id = (TextView)itemView.findViewById(R.id.tv_id);
            icon.setOnClickListener(this);
            container.setOnClickListener(this);

         //   thumbnail = (ImageView)itemView.findViewById(R.id.im_item_icon)
        }

        @Override
        public void onClick(View v){
            if(v.getId()==R.id.cont_item_root){
                itemClickCallBack.onItemClick(getAdapterPosition());

            }else{
                itemClickCallBack.onSecondaryIconClick(getAdapterPosition());

            }

        }
    }

}
