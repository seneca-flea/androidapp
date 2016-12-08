package com.example.yugenshtil.finalproject.Adapter;

import android.content.Context;
//import android.support.v7.internal.view.menu.MenuView;
//import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.ItemSell.ListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleg Mytryniuk on 05/11/16.
 */

public class BuyItemAdapter extends RecyclerView.Adapter<BuyItemAdapter.DerpHolder>  {

    private LayoutInflater inflater;
    private JSONArray itemList;
    private List<String> ids;

    private ItemClickCallback itemClickCallBack;

    public interface ItemClickCallback{
        void onMyFavoriteDeleteClick(int p);
        void onMyFavoriteAddClick(int p);
        void onItemClick(int p);
        void onMessageIconClick(int p);

    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallBack){
        this.itemClickCallBack = itemClickCallBack;

    }

    public BuyItemAdapter(Context c, JSONArray jsonArray, List<String> ids){
        this.ids = ids;
        this.inflater = LayoutInflater.from(c);
        this.itemList = jsonArray;
    }

    @Override
    public DerpHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = inflater.inflate(R.layout.list_buy_item,viewGroup,false);

        return new DerpHolder(view);
    }

    @Override
    public void onBindViewHolder(DerpHolder derpHolder, int i) {
        String title="";
        String description="";
        String price="";
        //Here it sets to the view
        try {
            JSONObject item = (JSONObject)itemList.get(i);
            title = item.get("Title").toString();
            description = item.get("Description").toString();
            price = item.get("Price").toString();
            String type = item.get("Type").toString();
            if(type.equals("Book")){
               derpHolder.thumbnail.setImageResource(R.drawable.book);
            }
            else
                derpHolder.thumbnail.setImageResource(R.drawable.item);

            if(ids.contains(item.getString("ItemId"))){
                derpHolder.isFavorite = true;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //    ListItem item = listData.get(i);
        derpHolder.title.setText(title);
        derpHolder.description.setText(description);
        derpHolder.price.setText("$ " + price);

        if(derpHolder.isFavorite){
            derpHolder.favoriteIcon.setImageResource(R.drawable.ic_star_black_36dp);
            derpHolder.favoriteIcon.setTag("fav");
        }else{
            derpHolder.favoriteIcon.setTag("notfav");
        }
    }


    public void setListData(ArrayList<ListItem> exerciseList){
      //  this.listData.clear();
     //   this.listData.addAll(exerciseList);

    }

    @Override
    public int getItemCount() {

        return itemList.length();
    }

    class DerpHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        private TextView title;
        private TextView description;
        private TextView price;
        private View container;
        private ImageView favoriteIcon;
        private ImageView messageIcon;
        private boolean isFavorite=false;

        //New
        private ImageView thumbnail;
        private ImageView secondaryIcon;


        public DerpHolder(View itemView) {
            super(itemView);

            title =(TextView)itemView.findViewById(R.id.tv_title);
            description = (TextView)itemView.findViewById(R.id.tv_description);
            price = (TextView)itemView.findViewById(R.id.tv_price);
            favoriteIcon = (ImageView)itemView.findViewById(R.id.im_favorite_icon);
            messageIcon = (ImageView) itemView.findViewById(R.id.im_new_message);
            container = itemView.findViewById(R.id.cont_item_root);

            favoriteIcon.setOnClickListener(this);
            messageIcon.setOnClickListener(this);
            container.setOnClickListener(this);
            thumbnail = (ImageView)itemView.findViewById(R.id.im_main_image);
        }

        @Override
        public void onClick(View v){
            if(v.getId()==R.id.cont_item_root) {
                itemClickCallBack.onItemClick(getAdapterPosition());
            }
            else if(v.getId()==R.id.im_favorite_icon){
                 if(favoriteIcon.getTag().equals("fav")){
                    favoriteIcon.setTag("notfav");
                    favoriteIcon.setImageResource(R.drawable.ic_star_border_black_36dp);
                    itemClickCallBack.onMyFavoriteDeleteClick(getAdapterPosition());
                }else{
                    favoriteIcon.setTag("fav");
                    favoriteIcon.setImageResource(R.drawable.ic_star_black_36dp);
                    itemClickCallBack.onMyFavoriteAddClick(getAdapterPosition());
                }


                String backgroundImageName = String.valueOf(favoriteIcon.getTag());
            }
            else if(v.getId()==R.id.im_new_message){
                itemClickCallBack.onMessageIconClick(getAdapterPosition());
            }


        }
    }

}
