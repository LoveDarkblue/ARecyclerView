package com.lcp.adapter;

import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Aislli on 2018/9/4 0004.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {
    private final SparseArray<View> views;

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.views = new SparseArray<>();
    }

    public static BaseViewHolder create(int layoutId, ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new BaseViewHolder(itemView);
    }

    public static BaseViewHolder create(View itemView) {
        return new BaseViewHolder(itemView);
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public BaseViewHolder setText(@IdRes int viewId, CharSequence value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    public BaseViewHolder setText(@IdRes int viewId, @StringRes int strId) {
        TextView view = getView(viewId);
        view.setText(strId);
        return this;
    }
}
