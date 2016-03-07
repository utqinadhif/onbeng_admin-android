package com.nadhif.onbengadmin;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by nadhif on 06/03/2016.
 */
public class Adapter extends RecyclerView.Adapter {
    private static ArrayList<Data> datas;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    Context context;

    public Adapter(ArrayList<Data> datas, RecyclerView recyclerView, Context context) {
        this.datas = datas;
        this.context = context;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                                 @Override
                                                 public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                                     super.onScrolled(recyclerView, dx, dy);

                                                     totalItemCount = linearLayoutManager.getItemCount();
                                                     lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                                                     if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                                         if (onLoadMoreListener != null) {
                                                             onLoadMoreListener.onLoadMore();
                                                         }
                                                         loading = true;
                                                     }
                                                 }
                                             }
            );
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        vh = new Holder(v);
        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Holder) {
            Data data = datas.get(position);
            ((Holder) holder).name.setText(data.getBengkel_name());
            ((Holder) holder).name.setTag(data.getBengkel_name());
            ((Holder) holder).company.setText(data.getBengkel_company());
            ((Holder) holder).contact.setText(data.getContact());
            ((Holder) holder).email.setText(data.getEmail());
            ((Holder) holder).location.setText(data.getLocation());
            ((Holder) holder).price.setText(data.getPrice_per_km());
            ((Holder) holder).latlng.setText(data.getLat() + "," + data.getLng());
            ((Holder) holder).latlng.setTag(R.string.pick_off, data.getLat());
            ((Holder) holder).latlng.setTag(R.string.pick_on, data.getLng());
            ((Holder) holder).data = data;
        }
    }

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }
}
