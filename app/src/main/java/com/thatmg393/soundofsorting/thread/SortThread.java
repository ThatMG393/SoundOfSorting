package com.thatmg393.soundofsorting.thread;

import static com.thatmg393.sortalgorithm.utils.ArrayUtils.SynchronizedArrayWrapper;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;
import androidx.core.os.HandlerCompat;

import com.thatmg393.sortalgorithm.base.SortAlgorithm;
import com.thatmg393.sortalgorithm.callback.SortEventCallback;
import com.thatmg393.soundofsorting.adapter.SortViewAdapter;
import com.thatmg393.soundofsorting.databinding.ActivityMainBinding;
import com.thatmg393.soundofsorting.loader.SortAlgoLoader;
import com.thatmg393.soundofsorting.utils.ArrayUtils;
import com.thatmg393.soundofsorting.utils.Constants;
import com.thatmg393.soundofsorting.utils.ToneGenerator;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class SortThread extends Thread implements SortEventCallback {
	private final String algorithmName;
	private final SynchronizedArrayWrapper<Integer> arr;
	private final ActivityMainBinding contentView;
	
	private final Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
	private AtomicBoolean isSorting = new AtomicBoolean();
	
	private int soundDurationMillis = 10;
	private int defaultWaveFreq = 10;
	
	private ToneGenerator.Tone soundPlayerSine = new ToneGenerator.Builder().setVolume(0.5).build();
	private ToneGenerator.Tone soundPlayerSaw = new ToneGenerator.Builder().setVolume(0.5).setWaveForm(ToneGenerator.SAWTOOTH).build();
	private ToneGenerator.Tone soundPlayerPulse = new ToneGenerator.Builder().setVolume(0.5).setWaveForm(ToneGenerator.PULSE).build();
	
	public SortThread(String algorithmName, ActivityMainBinding contentView, int arrSize) {
		this.algorithmName = algorithmName;
		
		this.arr = new SynchronizedArrayWrapper<Integer>(new Integer[arrSize]);
		ArrayUtils.randomize(this.arr.getArray());
		
		this.contentView = contentView;
	}
	
	@Override
	public void run() {
		SortAlgorithm sa = SortAlgoLoader.getInstance().getAlgorithm(algorithmName);
		
		if (sa != null) {
			isSorting.set(true);
			sa.sort(arr, this);
		}
	}

	@Override
	public void onElementSwap(int index1, int index2) {
		CompletableFuture.allOf(
			CompletableFuture.runAsync(() -> soundPlayerSaw.play(defaultWaveFreq + ((index1 + index2) * 3))),
			CompletableFuture.runAsync(() -> {
				contentView.mainArraySwapLog.post(() -> contentView.mainArraySwapLog.append("Swapped " + index1 + " and " + index2 + System.lineSeparator()));
				CompletableFuture.runAsync(() -> {
					String s = getArray().toString();
					contentView.mainCurrentArray.post(() -> contentView.mainCurrentArray.setText(s));
				});
					
				sleepNoThrow(Constants.DEFAULT_UPDATE_TIME);
		
				updateBarView(index1, ContextCompat.getColor(contentView.mainVisualizer.getContext(), Constants.DEFAULT_BAR_SWAP_COLOR));
				updateBarView(index2, ContextCompat.getColor(contentView.mainVisualizer.getContext(), Constants.DEFAULT_BAR_SWAP_COLOR));
			})
		).join();
	}
	
	@Override
	public void onAccessElement(int index) {
		CompletableFuture.allOf(
			CompletableFuture.runAsync(() -> soundPlayerSine.play(defaultWaveFreq + (index * 3))),
			CompletableFuture.runAsync(() -> updateBarView(index, ContextCompat.getColor(contentView.mainVisualizer.getContext(), Constants.DEFAULT_BAR_ACCESS_COLOR), Constants.DEFAULT_UPDATE_TIME_HALVED))
		).join();
	}
	
	@Override
	public void onSortDone() {
		for (int i = 0; i < arr.length(); i++) {
			final int i2 = i;
			CompletableFuture.allOf(
				CompletableFuture.runAsync(() -> soundPlayerPulse.play(defaultWaveFreq + (i2 * 3))),
				CompletableFuture.runAsync(() -> highlightBarView(i2, ContextCompat.getColor(contentView.mainVisualizer.getContext(), Constants.DEFAULT_SORT_ENDING_COLOR)))
			).join();
			
			sleepNoThrow(Constants.DEFAULT_UPDATE_TIME);
		}
		
		contentView.mainVisualizer.post(() -> contentView.mainVisualizer.setAdapter(contentView.mainVisualizer.getAdapter()));
		
		isSorting.set(false);
		contentView.mainCurrentArray.post(() -> contentView.mainCurrentArray.setText(getArray().toString()));
		
		cleanup();
	}
	
	public void sleepNoThrow(long sleepMillis) {
		try {
    		sleep(sleepMillis);
		} catch (InterruptedException e) { }
	}
	
	public SortViewAdapter.ViewHolder highlightBarView(int position, @ColorRes int color) {
		SortViewAdapter.ViewHolder v = (SortViewAdapter.ViewHolder) contentView.mainVisualizer.findViewHolderForPosition(position);
		if (v != null) {
			if (!v.bar.post(() -> {
				v.bar.getLayoutParams().height = (Constants.DEFAULT_GRID_HEIGHT * arr.getValue(position));
				v.bar.setBackgroundColor(color);
				v.bar.requestLayout();
			})) System.out.println("F for " + position);
		}
		
		return v;
	}
	
	public void updateBarView(int position, @ColorRes int color, int sleepMillis) {
		SortViewAdapter.ViewHolder v = highlightBarView(position, color);
		sleepNoThrow(sleepMillis);
		if (v != null) v.bar.post(() -> v.bar.setBackgroundColor(ContextCompat.getColor(v.bar.getContext(), Constants.DEFAULT_BAR_COLOR)));
	}
	
	public void updateBarView(int position, @ColorRes int color) {
		updateBarView(position, color, Constants.DEFAULT_UPDATE_TIME_THIRDS);
	}
	
	public SynchronizedArrayWrapper<Integer> getArray() {
		return this.arr;
	}
	
	public synchronized boolean isSorting() {
		return this.isSorting.get();
	}
	
	private void cleanup() {
		soundPlayerSine.release();
		soundPlayerSaw.release();
	}
}
