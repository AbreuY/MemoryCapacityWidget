package com.swisdev.android.widget;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.coolsandie.android.widget.R;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.os.Environment;
import android.os.MemoryFile;
import android.os.StatFs;
import android.widget.ProgressBar;
import android.widget.RemoteViews;

public class HelloWidget extends AppWidgetProvider {
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);
		ComponentName thisWidget = new ComponentName(context, HelloWidget.class);	
		
	   // Restore preferences
	   SharedPreferences settings = context.getSharedPreferences("Configure", 0);
	   int colorChoice = settings.getInt("textColor", Color.BLACK);
		
		String state = Environment.getExternalStorageState();
		//long internalPercent =  getPercentage(getAvailableInternalMemorySizeLong(), getTotalInternalMemorySizeLong());
		
		if(state.equals(Environment.MEDIA_MOUNTED))
		{
			//long externalPercent = getPercentage(getAvailableExternalMemorySizeLong(), getTotalExternalMemorySizeLong());
			
			remoteViews.setTextViewText(R.id.txtInternal, "Internal storage: \r\n" + "Available: " + getAvailableInternalMemorySize() + "\r\nCapacity: " + getTotalInternalMemorySize());			
			remoteViews.setTextViewText(R.id.txtExternal,  "External storage: \r\n" + "Available: " + getAvailableExternalMemorySize() + "\r\nCapacity: " + getTotalExternalMemorySize());
			remoteViews.setTextColor(R.id.txtInternal, colorChoice);
			remoteViews.setTextColor(R.id.txtExternal, colorChoice);
			
			//mProgress.setProgress((int)internalPercent);
			appWidgetManager.updateAppWidget(thisWidget, remoteViews);
		}
		else
		{
			remoteViews.setTextViewText(R.id.txtInternal, "Internal storage: \r\n" + "Available: " + getAvailableInternalMemorySize() + "\r\nCapacity: " + getTotalInternalMemorySize());
			remoteViews.setTextColor(R.id.txtInternal, colorChoice);
			
			appWidgetManager.updateAppWidget(thisWidget, remoteViews);
		}
		
	}
	

	@SuppressWarnings("null")
	public static void drawcircle() {
		Canvas canvas = null; 
        Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG |
                Paint.DITHER_FLAG |
                Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);

        int size = 200;
        int radius = 190;
        int delta = size - radius;
        int arcSize = (size - (delta / 2)) * 2;
        int percent = 42;

        //Thin circle
        canvas.drawCircle(size, size, radius, mPaint);

        //Arc
        mPaint.setColor(Color.parseColor("#33b5e5"));
        mPaint.setStrokeWidth(15);
        RectF box = new RectF(delta,delta,arcSize,arcSize);
        float sweep = 360 * percent * 0.01f;
        canvas.drawArc(box, 0, sweep, false, mPaint);
	}
	
	public static long getPercentage(long percentNumber, long total) {
		return (int) (((float)percentNumber / total) * 100); 
	}

    @SuppressLint("NewApi")
	public static String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();       
        long availableBlocks = stat.getFreeBlocksLong();
        return formatSize(availableBlocks * blockSize);
    }

    @SuppressLint("NewApi")
	public static String getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong(); 
        long totalBlocks = stat.getBlockCountLong();
        return formatSize(totalBlocks * blockSize);
    }

    @SuppressLint("NewApi")
	public static String getAvailableExternalMemorySize() {
    	String path = "";
        if (Build.BRAND.equals("samsung")) {
        	path = Environment.getExternalStorageDirectory().getParent();
        	path = "/storage/extSdCard/";
        }
        else {
        	path = Environment.getExternalStorageDirectory().getPath();
        }
        StatFs stat = new StatFs(path);
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return formatSize(availableBlocks * blockSize);
    }

    @SuppressLint("NewApi")
	public static String getTotalExternalMemorySize() {
    	String path = "";
        if (Build.BRAND.equals("samsung")) {
        	path = Environment.getExternalStorageDirectory().getParent();
        	path = "/storage/extSdCard/";
        }
        else {
        	path = Environment.getExternalStorageDirectory().getPath();
        }
        StatFs stat = new StatFs(path);
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return formatSize(totalBlocks * blockSize);
    }
    
    
    @SuppressLint("NewApi")
	public static long getAvailableInternalMemorySizeLong() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();       
        long availableBlocks = stat.getFreeBlocksLong();
        return (availableBlocks * blockSize);
    }

    @SuppressLint("NewApi")
	public static long getTotalInternalMemorySizeLong() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong(); 
        long totalBlocks = stat.getBlockCountLong();
        return (totalBlocks * blockSize);
    }
    
    
    @SuppressLint("NewApi")
	public static long getAvailableExternalMemorySizeLong() {
    	String path = "";
        if (Build.BRAND.equals("samsung")) {
        	path = Environment.getExternalStorageDirectory().getParent();
        	path = "/storage/extSdCard/";
        }
        else {
        	path = Environment.getExternalStorageDirectory().getPath();
        }
        StatFs stat = new StatFs(path);
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return (availableBlocks * blockSize);
    }

    @SuppressLint("NewApi")
	public static long getTotalExternalMemorySizeLong() {
    	String path = "";
        if (Build.BRAND.equals("samsung")) {
        	path = Environment.getExternalStorageDirectory().getParent();
        	path = "/storage/extSdCard/";
        }
        else {
        	path = Environment.getExternalStorageDirectory().getPath();
        }
        StatFs stat = new StatFs(path);
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return (totalBlocks * blockSize);
    }
    
    

    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;                
                if (size >= 1024) {
                	suffix = "GB";
                	size /= 1024;		
                }
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }
	 
}
