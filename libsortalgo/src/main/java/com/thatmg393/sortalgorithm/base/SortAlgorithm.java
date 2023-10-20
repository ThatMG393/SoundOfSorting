package com.thatmg393.sortalgorithm.base;

import com.thatmg393.sortalgorithm.utils.ArrayUtils;
import com.thatmg393.sortalgorithm.callback.SortEventCallback;

public abstract class SortAlgorithm {
	public abstract void sort(ArrayUtils.SynchronizedArrayWrapper<Integer> arr, SortEventCallback elCb);
}
