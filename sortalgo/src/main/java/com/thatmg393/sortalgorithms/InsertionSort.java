package com.thatmg393.sortalgorithms;

import com.thatmg393.sortalgorithm.base.SortAlgorithm;
import com.thatmg393.sortalgorithm.callback.SortEventCallback;
import static com.thatmg393.sortalgorithm.utils.ArrayUtils.SynchronizedArrayWrapper;

public class InsertionSort extends SortAlgorithm {
	@Override
	public void sort(SynchronizedArrayWrapper<Integer> arr, SortEventCallback elCb) {
        for (int i = 1; i < arr.length(); ++i) {
			elCb.onAccessElement(i);
            int key = arr.getValue(i);
            int j = i - 1;
 
            while (j >= 0 && arr.getValue(j) > key) {
				elCb.onAccessElement(j);
                arr.setValue(j + 1, arr.getValue(j));
                j = j - 1;
            }
            arr.setValue(j + 1, key);
        }
		
		elCb.onSortDone();
	}
}
