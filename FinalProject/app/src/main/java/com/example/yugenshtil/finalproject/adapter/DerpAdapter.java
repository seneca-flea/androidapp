package com.example.yugenshtil.finalproject.adapter;

import android.content.Context;
import android.media.Image;
//import android.support.v7.internal.view.menu.MenuView;
//import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.model.ListItem;

import java.util.List;

/**
 * Created by yugenshtil on 05/11/16.
 */

public class DerpAdapter extends RecyclerView.Adapter<DerpAdapter.DerpHolder> {

    private List<ListItem> listData;
    private LayoutInflater inflater;

    public DerpAdapter(List<ListItem> listData, Context c){
        this.inflater = LayoutInflater.from(c);
        this.listData = listData;
    }

    @Override
    public DerpHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

       View view = inflater.inflate(R.layout.list_item,viewGroup,false);

        return new DerpHolder(view);
    }

    @Override
    public void onBindViewHolder(DerpHolder derpHolder, int i) {
        ListItem item = listData.get(i);
        derpHolder.title.setText(item.getTitle());
        derpHolder.icon.setImageResource(item.getImageResId());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class DerpHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private ImageView icon;
        private View container;


        public DerpHolder(View itemView) {
            super(itemView);

            title =(TextView)itemView.findViewById(R.id.tv_item_text);
            icon =(ImageView)itemView.findViewById(R.id.im_item_icon);
            container = itemView.findViewById(R.id.container_item_root);
        }
    }

}
