package com.thatmg393.sortalgorithms;

import static com.thatmg393.sortalgorithm.utils.ArrayUtils.SynchronizedArrayWrapper;

import com.thatmg393.sortalgorithm.base.SortAlgorithm;
import com.thatmg393.sortalgorithm.callback.SortEventCallback;

public class BubbleSort extends SortAlgorithm {
	@Override
	public void sort(SynchronizedArrayWrapper<Integer> arr, SortEventCallback elCb) {
		for (int i = 0; i < arr.length(); i++) {
			for (int j = 1; j < (arr.length() - i); j++) {
				elCb.onAccessElement(j-1);
				elCb.onAccessElement(j);
				
				if (arr.getValue(j-1) > arr.getValue(j)) {
					arr.swap(j-1, j);
					elCb.onElementSwap(j-1, j);
				}
			}
		}
		
		elCb.onSortDone();
	}
}
