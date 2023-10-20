package com.thatmg393.soundofsorting.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.thatmg393.soundofsorting.R;
import com.thatmg393.soundofsorting.utils.Constants;
import com.thatmg393.soundofsorting.utils.DisplayUtils;

public class SortViewAdapter extends RecyclerView.Adapter<SortViewAdapter.ViewHolder> {
	private Context context;
	private Integer[] arr;
	
	public SortViewAdapter(Context context, Integer[] arr) {
		this.context = context;
		this.arr = arr;
	}
	
	@Override
	public SortViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(context).inflate(R.layout.grid_sort_element, parent, false);
		v.getLayoutParams().width = Math.round(DisplayUtils.getScreenWidth((Activity) context) / getItemCount());
		
		return new ViewHolder(v);
	}
	
	@Override
	public void onBindViewHolder(SortViewAdapter.ViewHolder viewHolder, int position) {
		viewHolder.bar.getLayoutParams().height = (Constants.DEFAULT_GRID_HEIGHT * arr[position].intValue());
	}
	
	@Override
	public int getItemCount() {
		return arr.length;
	}
	
	public void setNewArray(Integer[] arr) {
		this.arr = arr;
		notifyDataSetChanged();
	}
	
	public static class ViewHolder extends RecyclerView.ViewHolder {
		public final View bar;
		
		public ViewHolder(View layout) {
			super(layout);
			this.bar = layout;
		}
	}
}
