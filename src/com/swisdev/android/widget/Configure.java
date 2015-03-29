package com.swisdev.android.widget;
import java.io.File;

import com.coolsandie.android.widget.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.TextView;


public class Configure extends Activity {
	
	private Configure context; 
	private int widgetID;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configure);		
		context = this; 
		
		//Need to capture the extras, even if we don't use them
		Bundle extras = getIntent().getExtras(); 
		if (extras != null) {
			widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, 
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}
				
		final AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
		final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);		
		final Spinner spColorList = (Spinner) findViewById(R.id.colourSpinner);
		//final Spinner spBackGroundList = (Spinner) findViewById(R.id.backgroundSpinner);
		
		//On click of the submit button
		Button b = (Button) findViewById(R.id.btnSubmit);				
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//Pick text colour and background colour				
				String spColor = spColorList.getSelectedItem().toString();
				//String spBackground = spBackGroundList.getSelectedItem().toString();
				
				int colorChoice = Color.BLACK;
				int backgroundChoice = 1; 
				
				if(spColor.equals("Black")) {
					colorChoice = Color.BLACK;
				}
				else if(spColor.equals("Red")) {
					colorChoice = Color.RED;
				}
				else if(spColor.equals("Blue")){
					colorChoice = Color.BLUE;
				}
				else if(spColor.equals("Yellow")){
					colorChoice = Color.YELLOW;
				}
				else if(spColor.equals("Green")){
					colorChoice = Color.GREEN;
				}
				else if(spColor.equals("Gray")){
					colorChoice = Color.GRAY;
				}
				else if(spColor.equals("Magenta")){
					colorChoice = Color.MAGENTA;
				}
				else if(spColor.equals("White")){
					colorChoice = Color.WHITE;
				}
				else {
					colorChoice = Color.BLACK;
				}
				
				SharedPreferences settings = getSharedPreferences("Configure", 0);
			      SharedPreferences.Editor editor = settings.edit();
			      editor.putInt("textColor", colorChoice);
			      editor.commit();
			      	
				widgetManager.updateAppWidget(widgetID, views);
				
				//Need the intent to send result value
				Intent resultValue = new Intent(); 
				resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
				setResult(RESULT_OK, resultValue);
				
				
				//App info, on update method, way to call that instead??
				RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);
				ComponentName thisWidget = new ComponentName(context, HelloWidget.class);
				
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
					widgetManager.updateAppWidget(thisWidget, remoteViews);
				}
				else
				{
					remoteViews.setTextViewText(R.id.txtInternal, "Internal storage: \r\n" + "Available: " + getAvailableInternalMemorySize() + "\r\nCapacity: " + getTotalInternalMemorySize());
					remoteViews.setTextColor(R.id.txtInternal, colorChoice);
					
					widgetManager.updateAppWidget(thisWidget, remoteViews);					
				}
							
				finish();
				
			} //public void onClick(View v) 
		
		}); // b.setOnClickListener(new OnClickListener()
							
	} //protected void onCreate (Bundle savedInstanceState) 
	
	
	
	
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
