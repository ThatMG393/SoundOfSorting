package com.thatmg393.soundofsorting.loader;

import android.os.FileObserver;

import com.thatmg393.soundofsorting.utils.Constants;

import java.io.File;

public class SortAlgoFolderWatcher extends FileObserver {
	public SortAlgoFolderWatcher() {
		super(new File(Constants.SORT_ALGORITHMS_PATH));
	}
	
	@Override
	public void onEvent(int event, String file) {
		System.out.println("BANG! " + event + ", " + file);
		if (event == FileObserver.CREATE || event == FileObserver.MOVED_FROM && file.endsWith(".dex")) {
			handleFileUpdate(file);
		}
	}
	
	private void handleFileUpdate(String file) {
		SortAlgoLoader.getInstance().addNewAlgorithm(new File(file));
	}
}
