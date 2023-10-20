package com.thatmg393.soundofsorting.adapter.callback;

import androidx.recyclerview.widget.DiffUtil;
import java.util.ArrayList;

public class SortDiffCallback extends DiffUtil.Callback {
	private final ArrayList<Integer> oldSortArr;
	private final ArrayList<Integer> newSortArr;
	
	public SortDiffCallback(ArrayList<Integer> oldSortArr, ArrayList<Integer> newSortArr) {
		this.oldSortArr = oldSortArr;
		this.newSortArr = newSortArr;
	}

	@Override
	public int getOldListSize() {
		return oldSortArr.size();
	}
	
	@Override
	public int getNewListSize() {
		return newSortArr.size();
	}

	@Override
	public boolean areItemsTheSame(int oldItemIndex, int newItemIndex) {
		return oldSortArr.get(oldItemIndex) == newSortArr.get(newItemIndex);
	}
	
	@Override
	public boolean areContentsTheSame(int oldItemIndex, int newItemIndex) {
		return areItemsTheSame(oldItemIndex, newItemIndex);
	}
	
	@Override
	public Object getChangePayload(int oldItemIndex, int newItemIndex) {
		return super.getChangePayload(oldItemIndex, newItemIndex);
	}
	
}
