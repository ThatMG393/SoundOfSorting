package com.thatmg393.soundofsorting.utils;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class ArrayUtils {
	public static void randomize(Integer[] arr) {
		for (int i = 0; i < arr.length; i++) {
			arr[i] = new Integer(ThreadLocalRandom.current().nextInt(1, arr.length));
		}
	}
}
