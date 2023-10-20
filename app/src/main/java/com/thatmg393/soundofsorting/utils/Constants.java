package com.thatmg393.soundofsorting.utils;

import android.os.Environment;
import androidx.annotation.ColorRes;
import com.thatmg393.soundofsorting.R;

public class Constants {
	public static final String SORT_FOLDER_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SoundOfSort";
	public static final String SORT_ALGORITHMS_PATH = SORT_FOLDER_PATH + "/Algorithms";
	public static final String SORT_CACHE_PATH = SORT_FOLDER_PATH + "/Cache";
	
	public static final int DEFAULT_GRID_HEIGHT = 12 / 2;
	public static final int DEFAULT_UPDATE_TIME = 20;
	
	public static final int DEFAULT_UPDATE_TIME_HALVED = DEFAULT_UPDATE_TIME / 2;
	public static final int DEFAULT_UPDATE_TIME_THIRDS = DEFAULT_UPDATE_TIME / 3;
	
	@ColorRes
	public static final int DEFAULT_BAR_COLOR = R.color.white;
	
	@ColorRes
	public static final int DEFAULT_BAR_ACCESS_COLOR = android.R.color.holo_green_light;
	
	@ColorRes
	public static final int DEFAULT_BAR_SWAP_COLOR = android.R.color.holo_orange_light;
	
	@ColorRes
	public static final int DEFAULT_SORT_ENDING_COLOR = android.R.color.holo_red_light;
}
