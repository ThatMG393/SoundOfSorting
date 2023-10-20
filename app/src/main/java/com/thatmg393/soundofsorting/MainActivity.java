package com.thatmg393.soundofsorting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.thatmg393.sortalgorithms.*;
import com.thatmg393.soundofsorting.adapter.SortViewAdapter;
import com.thatmg393.soundofsorting.databinding.ActivityMainBinding;
import com.thatmg393.soundofsorting.loader.SortAlgoFolderWatcher;
import com.thatmg393.soundofsorting.loader.SortAlgoLoader;
import com.thatmg393.soundofsorting.thread.SortThread;
import com.thatmg393.soundofsorting.utils.Constants;
import com.thatmg393.soundofsorting.utils.DisplayUtils;
import com.thatmg393.soundofsorting.views.layoutmanager.AutoFitWidthLayoutManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainActivity extends AppCompatActivity {
	private ActivityMainBinding contentView;
	private ActivityResultLauncher<Intent> askForStoragePerm;
	
	private SortAlgoFolderWatcher safw = new SortAlgoFolderWatcher();
	private SortThread sortThr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(contentView.getRoot());
		
		askForStoragePerm = registerForActivityResult(
			new ActivityResultContracts.StartActivityForResult(),
			new ActivityResultCallback<ActivityResult>() {
				@Override
                public void onActivityResult(ActivityResult result) {
					System.out.println(result.getResultCode());
                    if (result.getResultCode() == RESULT_OK) {
                        if (Environment.isExternalStorageManager()) {
							initRootDirectory();
							init();
						} else {
							finishAffinity();
						}
                    } else {
						finishAffinity();
					}
                }
			}
		);
		
		acquireStoragePermission();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		safw.stopWatching();
	}
	
	private void init() {
		final Handler h = new Handler(Looper.getMainLooper());
		DirectoryStream.Filter<Path> dexFilter = file -> {
			return Files.size(file) < 10_000L && file.toString().endsWith(".dex");
		};
		
		try (DirectoryStream<Path> dir = Files.newDirectoryStream(Paths.get(Constants.SORT_ALGORITHMS_PATH), dexFilter)) {
			dir.forEach(path -> {
				SortAlgoLoader.getInstance().addNewAlgorithm(new File(path.toString()));
			});
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
		
		safw.startWatching();
		
		SortAlgoLoader.getInstance().addNewAlgorithm("BubbleSort", new BubbleSort());
		SortAlgoLoader.getInstance().addNewAlgorithm("QuickSort", new QuickSort());
		SortAlgoLoader.getInstance().addNewAlgorithm("InsertionSort", new InsertionSort());
	
		int arraySize = 75;
		
		contentView.mainArraySwapLog.setMovementMethod(new ScrollingMovementMethod());
		contentView.mainOriginalArray.setMovementMethod(new ScrollingMovementMethod());
		
		sortThr = new SortThread("QuickSort", contentView, arraySize);
		contentView.mainOriginalArray.setText(sortThr.getArray().toString());
		
		SortViewAdapter adap = new SortViewAdapter(this, sortThr.getArray().getArray());
		
		// contentView.mainVisualizer.addItemDecoration(new SpacingItemDecoration(2));
		contentView.mainVisualizer.setLayoutManager(
			new LinearLayoutManager(
				this,
				LinearLayoutManager.HORIZONTAL,
				false
				// DisplayUtils.getScreenWidth(this)
			)
		);
		contentView.mainVisualizer.setAdapter(adap);
		
		contentView.mainStartSort.setOnClickListener(v -> {
			if (!sortThr.isSorting()) {
				contentView.mainArraySwapLog.setText("");
				
				sortThr = new SortThread("QuickSort", contentView, arraySize);
				contentView.mainOriginalArray.setText(sortThr.getArray().toString());
				adap.setNewArray(sortThr.getArray().getArray());
				
				sortThr.start();
			}
		});
	}
	
	private void initRootDirectory() {
		new File(Constants.SORT_ALGORITHMS_PATH).mkdirs();
		new File(Constants.SORT_CACHE_PATH).mkdirs();
	}
	
	private void acquireStoragePermission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
          	  Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
          	  intent.setData(Uri.fromParts("package", getPackageName(), null));
         	   askForStoragePerm.launch(intent);
      	  } else {
				initRootDirectory();
				init();
			}
        } else {
            ActivityCompat.requestPermissions(
				this,
				new String[] {
					Manifest.permission.WRITE_EXTERNAL_STORAGE,
					Manifest.permission.READ_EXTERNAL_STORAGE
				},
				0
			);
        }
	}
	
	@Override
	public void onRequestPermissionsResult(int code, String[] permission, int[] result) {
		switch (code) {
			case 0:
				if (result[0] == PackageManager.PERMISSION_GRANTED
				&& result[1] == PackageManager.PERMISSION_GRANTED) {
					initRootDirectory();
					init();
				
					break;
				} else {
					finishAffinity();
				}
			default:
				return;
		}
	}
}
