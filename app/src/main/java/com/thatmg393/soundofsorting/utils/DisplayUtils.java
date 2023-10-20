package com.thatmg393.soundofsorting.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Insets;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowInsets;
import android.view.WindowMetrics;

import androidx.annotation.NonNull;

public class DisplayUtils {
	public static int getScreenWidth(@NonNull Activity activity) {
  	  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
   	     WindowMetrics windowMetrics = activity.getWindowManager().getCurrentWindowMetrics();
     	   Insets insets = windowMetrics.getWindowInsets()
   	             .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
   	     return windowMetrics.getBounds().width() - insets.left - insets.right;
   	 } else {
   	     DisplayMetrics displayMetrics = new DisplayMetrics();
   	     activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
      	  return displayMetrics.widthPixels;
  	  }
	}
}
