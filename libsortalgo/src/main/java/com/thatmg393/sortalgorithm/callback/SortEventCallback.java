package com.thatmg393.sortalgorithm.callback;

public interface SortEventCallback {
	public void onElementSwap(int index1, int index2);
	
	public default void onAccessElement(int index) { }
	public default void onElementPassedCondition(int index, boolean passed) { }
	public default void onSortDone() { }
}
