package com.hit.ddmonkey.app;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.ViewHolder> {

    private List<BillRecord> items;
    private int itemLayout;

    //private String deleteFlag;

    public interface OnItemClickerListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }
    private OnItemClickerListener mOnItemClickListener;
    public void setmOnItemClickListener(OnItemClickerListener listener){
        this.mOnItemClickListener = listener;
    }

    public MyRecycleViewAdapter(List<BillRecord> items, int itemLayout){
        this.items = items;
        this.itemLayout = itemLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
       BillRecord item = items.get(position);
        holder.IOtext.setText(item.getIO());
        holder.Catext.setText(item.getCategory());
        holder.Datext.setText(item.getDate());
        holder.Notext.setText(item.getNote());
        holder.Motext.setText(String.valueOf(item.getMoney()));

        if(mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder.itemView, position);
                    return false;
                }
            });
        }




       // holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount(){
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView IOtext;
        public TextView Catext;
        public TextView Motext;
        public TextView Datext;
        public TextView Notext;
        public ViewHolder(View itemView){
            super(itemView);
            IOtext = (TextView)itemView.findViewById(R.id.IO);
            Catext = (TextView)itemView.findViewById(R.id.category);
            Motext = (TextView)itemView.findViewById(R.id.moneyNumber);
            Datext = (TextView)itemView.findViewById(R.id.date);
            Notext = (TextView)itemView.findViewById(R.id.note);
        }

    }

//    public void deleteItem(int pos){
//        BillRecord item = items.get(pos);
//        deleteFlag = item.getDateId();
//        notifyItemRemoved(pos);
//    }

}
