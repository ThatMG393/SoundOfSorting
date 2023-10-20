package com.thatmg393.soundofsorting.loader;

import androidx.annotation.Nullable;

import dalvik.system.DexClassLoader;

import com.thatmg393.sortalgorithm.base.SortAlgorithm;
import com.thatmg393.soundofsorting.utils.Constants;

import java.io.File;
import java.util.HashMap;

public class SortAlgoLoader {
	private static volatile SortAlgoLoader INSTANCE;
	public static SortAlgoLoader getInstance() {
		if (INSTANCE == null) INSTANCE = new SortAlgoLoader();
		return INSTANCE;
	}
	
	private SortAlgoLoader() {
		if (INSTANCE != null) throw new RuntimeException("Use '" + getClass().getName() + "#getInstance()' instead!");
	}
	
	private HashMap<String, SortAlgorithm> sortingAlgorithms = new HashMap<>();
	
	public void addNewAlgorithm(File path) {
		try {
			DexClassLoader clsLoader = new DexClassLoader(
				path.getAbsolutePath(),
				Constants.SORT_CACHE_PATH,
				null,
				this.getClass().getClassLoader()
			);
			
			Class<? extends SortAlgorithm> sortingAlgoClass = (Class<? extends SortAlgorithm>) clsLoader.loadClass("com.thatmg393.sortalgorithm." + SortAlgoLoader.getNameWithoutExtension(path.getName()));
			Object sortingAlgo = sortingAlgoClass.newInstance();
			
			addNewAlgorithm(SortAlgoLoader.getNameWithoutExtension(path.getName()), (SortAlgorithm) sortingAlgo);
		} catch (IllegalAccessException | ClassNotFoundException | InstantiationException | ClassCastException e) {
			e.printStackTrace(System.err);
		}
	}
	
	public void addNewAlgorithm(String name, SortAlgorithm algorithm) {
		if (!sortingAlgorithms.containsKey(name)) sortingAlgorithms.put(name, algorithm);
		else {
			sortingAlgorithms.remove(name);
			sortingAlgorithms.put(name, algorithm);
		}
	}
	
	@Nullable
	public SortAlgorithm getAlgorithm(String name) {
		return sortingAlgorithms.get(name);
	}
	
	public static String getNameWithoutExtension(String path) {
		if (path.indexOf(".") > 0) {
			return path.substring(0, path.lastIndexOf("."));
		} else {
			return path;
		}
	}
}
