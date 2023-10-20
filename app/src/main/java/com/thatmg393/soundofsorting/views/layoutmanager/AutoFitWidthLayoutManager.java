package com.thatmg393.soundofsorting.views.layoutmanager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AutoFitWidthLayoutManager extends LinearLayoutManager {
	private final int displaySize;
	
	public AutoFitWidthLayoutManager(Context context, int displaySize) {
        super(context);
		this.displaySize = displaySize;
    }

    public AutoFitWidthLayoutManager(Context context, int orientation, boolean reverseLayout, int displaySize) {
        super(context, orientation, reverseLayout);
		this.displaySize = displaySize;
    }

    public AutoFitWidthLayoutManager(Context context, AttributeSet attributeSet, int defStyleAttr, int defStyleRes, int displaySize) {
        super(context, attributeSet, defStyleAttr, defStyleRes);
		this.displaySize = displaySize;
    }
	
	@Override
    public boolean canScrollVertically() {
        return false;
    }
	
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return spanLayoutSize(super.generateDefaultLayoutParams());
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(Context context, AttributeSet attributeSet) {
        return spanLayoutSize(super.generateLayoutParams(context, attributeSet));
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return spanLayoutSize(super.generateLayoutParams(layoutParams));
    }

	private int viewWidth = -1;

    private RecyclerView.LayoutParams spanLayoutSize(RecyclerView.LayoutParams layoutParams) {
		if (viewWidth == -1) viewWidth = (getWidth() - getPaddingRight() - getPaddingLeft()) / getItemCount() + 1;
		layoutParams.width = viewWidth;
		
		return layoutParams;
	}
}
