package com.thatmg393.sortalgorithms;

import static com.thatmg393.sortalgorithm.utils.ArrayUtils.SynchronizedArrayWrapper;

import com.thatmg393.sortalgorithm.base.SortAlgorithm;
import com.thatmg393.sortalgorithm.callback.SortEventCallback;

public class QuickSort extends SortAlgorithm {
	private int partition(SynchronizedArrayWrapper<Integer> arr, int begin, int end, SortEventCallback elCb) {
 	   int pivot = arr.getValue(end);
		elCb.onAccessElement(end);
		
   	 int i = (begin - 1);
		
  	  for (int j = begin; j < end; j++) {
			elCb.onAccessElement(j);
			elCb.onAccessElement(pivot);
			
    	    if (arr.getValue(j) <= pivot) {
     	       i++;
				
      	      arr.swap(j, i);
				elCb.onElementSwap(j, i);
    	    }
  	  }
		
		arr.swap(end, i + 1);
		elCb.onElementSwap(end, i + 1);
		
  	  return (i + 1);
	}
	
	private void quickSort(SynchronizedArrayWrapper<Integer> arr, int begin, int end, SortEventCallback elCb) {
		if (begin < end) {
    	    int partitionIndex = partition(arr, begin, end, elCb);

    	    quickSort(arr, begin, partitionIndex - 1, elCb);
   	     quickSort(arr, partitionIndex + 1, end, elCb);
 	   }
	}
	
	@Override
	public void sort(SynchronizedArrayWrapper<Integer> arr, SortEventCallback elCb) {
		quickSort(arr, 0, arr.length() - 1, elCb);
		
		elCb.onSortDone();
	}
}
