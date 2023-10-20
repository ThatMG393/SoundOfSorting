package com.thatmg393.soundofsorting.views;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public class SpacingItemDecoration extends RecyclerView.ItemDecoration {
	private int spacingSizePx;
	public SpacingItemDecoration(int spacingSizePx) {
		this.spacingSizePx = spacingSizePx / 2;
	}
	
	@Override
	public void getItemOffsets(
		Rect rect, View view,
		RecyclerView parent,
		RecyclerView.State state
	) {
		rect.left = spacingSizePx;
		rect.right = spacingSizePx;
	}
}
