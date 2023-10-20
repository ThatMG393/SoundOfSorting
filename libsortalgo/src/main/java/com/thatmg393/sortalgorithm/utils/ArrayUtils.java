package com.thatmg393.sortalgorithm.utils;

public class ArrayUtils {
	public static class SynchronizedArrayWrapper<T> {
		private final T[] arr;
		public SynchronizedArrayWrapper(T[] array) {
			this.arr = array;
		}
		
		public void setValue(int i, T val) {
			synchronized (arr) {
				this.arr[i] = val;
			}
		}
		
		public T getValue(int i) {
			synchronized (arr) {
				return this.arr[i];
			}
		}
		
		public int length() {
			return this.arr.length;
		}
		
		public T[] getArray() {
			synchronized (arr) {
				return this.arr;
			}
		}
		
		public void swap(int i, int j) {
			T temp = getValue(i);
			setValue(i, getValue(j));
			setValue(j, temp);
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (T el : this.arr) {
				sb.append(el.toString() + ", ");
			}
				
			return sb.toString();
		}
	}
}
