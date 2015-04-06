package com.anthony.fernandez.nightly.util;

import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.anthony.fernandez.nightly.R;

public class Utils {
	
	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}

	public final static void createToast(Context context, int textID){
		Toast toast = Toast.makeText(context, textID, Toast.LENGTH_SHORT);
		toast.getView().setBackgroundColor(context.getResources().getColor(R.color.overlay_splashscreen));
		TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
		v.setTextColor(Color.WHITE);
		toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 20);
		toast.show();
	}
	
	public final static void createToast(Context context, String text){
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.getView().setBackgroundColor(context.getResources().getColor(R.color.overlay_splashscreen));
		TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
		v.setTextColor(Color.WHITE);
		toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 20);
		toast.show();
	}
	
	public final static String getPhoneLanguage(){
		String locale = Locale.getDefault().getLanguage();
		if(locale.equals("fr")){
			return "fr";
		} else {
			return "en";
		}
	}
}
