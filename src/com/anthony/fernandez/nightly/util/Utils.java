package com.anthony.fernandez.nightly.util;

import com.anthony.fernandez.nightly.R;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;

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
		toast.getView().setBackgroundColor(context.getResources().getColor(R.color.facebook));
		TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
		v.setTextColor(Color.WHITE);
		toast.show();
	}
	
	public final static void createToast(Context context, String text){
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.getView().setBackgroundColor(context.getResources().getColor(R.color.facebook));
		TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
		v.setTextColor(Color.WHITE);
		toast.show();
	}
}
