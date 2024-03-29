package com.anthony.fernandez.nightly;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.anthony.fernandez.nightly.adapter.InitPagerAdapter;
import com.anthony.fernandez.nightly.fragment.RegisterPartOne;
import com.anthony.fernandez.nightly.fragment.RegisterPartTwo;
import com.anthony.fernandez.nightly.task.listener.OnAccountCreated;
import com.anthony.fernandez.nightly.util.Utils;
import com.anthony.fernandez.nightly.view.ScrollViewPager;
import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.soundcloud.android.crop.Crop;
import com.soundcloud.android.crop.CropUtil;

public class RegisterActivity extends SherlockFragmentActivity implements CalendarDatePickerDialog.OnDateSetListener, OnAccountCreated {

	private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";
	private static final int CAMERA_REQUEST = 1888; 

	private PagerAdapter pagerAdapter;
	public static ScrollViewPager pager;
	private List<Object> fragments;
	
	private int day = 0;
	private int month = 0;
	private int year = 0;
	
	private int photoMode = 0; //if 1 = pic else 2= take
	private Uri imageFilePath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
		setContentView(R.layout.activity_register_container);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true); 
		} 

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setTintColor(getResources().getColor(R.color.blue_crepuscule));
		
		fragments = new Vector<Object>();

		fragments.add(Fragment.instantiate(this,RegisterPartOne.class.getName()));
		fragments.add(Fragment.instantiate(this,RegisterPartTwo.class.getName()));

		this.pagerAdapter = new InitPagerAdapter(super.getSupportFragmentManager(), fragments);
		pager = (ScrollViewPager) findViewById(R.id.viewpager);
		pager.setPagingEnabled(false);
		pager.setAdapter(this.pagerAdapter);
	}
	
	public void pickBirthDay(View v){
		FragmentManager fm = getSupportFragmentManager();
		Calendar now = Calendar.getInstance();
		CalendarDatePickerDialog calendarDatePickerDialog = CalendarDatePickerDialog
				.newInstance(RegisterActivity.this, now.get(Calendar.YEAR)-10, now.get(Calendar.MONTH),
						now.get(Calendar.DAY_OF_MONTH));
		calendarDatePickerDialog.setYearRange(now.get(Calendar.YEAR)-90, now.get(Calendar.YEAR));
		calendarDatePickerDialog.show(fm, FRAG_TAG_DATE_PICKER);
	}
	
	public void choicePicture(View v){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setItems(getResources().getStringArray(R.array.picture), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void male(View v){
		if(((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).female.isSelected()){
			((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).female.setSelected(false);
		}
		((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).male.setSelected(true);
	}

	public void female(View v){
		if(((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).male.isSelected()){
			((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).male.setSelected(false);
		}
		((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).female.setSelected(true);
	}
	
	public void pickPhoto(View v){
		Crop.pickImage(this);
	}
	
	
	public void takePhoto(View v){
		PackageManager packageManager = this.getPackageManager();
	    if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false){
	    	Utils.createToast(RegisterActivity.this, R.string.no_camera_detected);
	    } else {
	    	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
			intent.putExtra("android.intent.extras.CAMERA_FACING", 1);  
	        ContentValues values = new ContentValues(3);  
	        values.put(MediaStore.Images.Media.DISPLAY_NAME, "testing");  
	        values.put(MediaStore.Images.Media.DESCRIPTION, "this is description");  
	        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");  
	        imageFilePath = RegisterActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);  
	        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFilePath); 

	        startActivityForResult(intent, CAMERA_REQUEST); 
	    }
	}
	
	public boolean hasImageCaptureBug() {
	    // list of known devices that have the bug
	    ArrayList<String> devices = new ArrayList<String>();
	    devices.add("android-devphone1/dream_devphone/dream");
	    devices.add("generic/sdk/generic");
	    devices.add("vodafone/vfpioneer/sapphire");
	    devices.add("tmobile/kila/dream");
	    devices.add("verizon/voles/sholes");
	    devices.add("google_ion/google_ion/sapphire");

	    return devices.contains(android.os.Build.BRAND + "/" + android.os.Build.PRODUCT + "/"
	            + android.os.Build.DEVICE);
	}

	
	public void definePhoto(View v){
		if(((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).actions.getVisibility() == View.GONE){
			((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).actions.setVisibility(View.GONE);
			Animation animation = new TranslateAnimation(0, 0, -((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).container.getHeight(), 0);
			animation.setStartOffset(0L);
			animation.setFillAfter(true);
			animation.setFillBefore(true);
            animation.setDuration(1000);
            animation.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animatiofillAftern) {
                	((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).actions.setVisibility(View.VISIBLE);
                }
            });

            ((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).actions.startAnimation(animation);
		}
	}

	
	public void quit(View v){
		if(((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).actions.getVisibility() == View.VISIBLE){
			((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).actions.setVisibility(View.VISIBLE);
			Animation animation=new TranslateAnimation(0, 0, 0, -((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).container.getHeight());
			animation.setStartOffset(0L);
            animation.setDuration(1000);
            animation.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animatiofillAftern) {
                	((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).actions.setVisibility(View.GONE);
                }
            });

            ((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).actions.startAnimation(animation);
		}
	}

	@SuppressLint("SimpleDateFormat")
	public void next(View v){
		if(((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).profilPicture.getTag().toString().equals(getResources().getString(R.string.content_description))){
			displayError(R.string.change_image_profil);
			return;
		}
		if(!((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).male.isSelected() && !((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).female.isSelected()){
			displayError(R.string.fill_all_fields);
			return;
		}
		if(0 == ((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).firstname.getText().toString().length()){
			displayError(R.string.fill_all_fields);
			return;
		}
		if(0 == ((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).lastname.getText().toString().length()){
			displayError(R.string.fill_all_fields);
			return;
		}
		if(0 == ((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).birthday.toString().length()){
			displayError(R.string.fill_all_fields);
			return;
		}
		if(day == 0 || month == 0 || year == 0){
			displayError(R.string.age_not_fill);
			return;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
		String dateInString = day+"-"+month+"-"+year;
		try {
			Date date = sdf.parse(dateInString);
			if(getAge(date) < 14){
				displayError(R.string.under_14);
				return;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		alllGood(R.string.all_good);
		pager.setCurrentItem(pager.getCurrentItem()+1, true);
	}

	
	public static int getAge(Date dateOfBirth) {
		Calendar today = Calendar.getInstance();
		Calendar birthDate = Calendar.getInstance();

		int age = 0;

		birthDate.setTime(dateOfBirth);
		if (birthDate.after(today)) {
			throw new IllegalArgumentException("Can't be born in the future");
		}

		age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

		// If birth date is greater than todays date (after 2 days adjustment of leap year) then decrement age one year   
		if ( (birthDate.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR) > 3) ||
				(birthDate.get(Calendar.MONTH) > today.get(Calendar.MONTH ))){
			age--;

			// If birth date and todays date are of same month and birth day of month is greater than todays day of month then decrement age
		}else if ((birthDate.get(Calendar.MONTH) == today.get(Calendar.MONTH )) &&
				(birthDate.get(Calendar.DAY_OF_MONTH) > today.get(Calendar.DAY_OF_MONTH ))){
			age--;
		}

		return age;
	}

	
	@Override
	public void onDateSet(CalendarDatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
		String birthday = dayOfMonth + " " + getMonth(monthOfYear+1) + " " + year;
		this.day = dayOfMonth;
		this.month = monthOfYear+1;
		this.year = year;
		Log.w("Nightly", birthday);
		((TextView)findViewById(R.id.birthday)).setText(birthday);
	}

	@Override
	protected void onResume() {
		super.onResume();
		CalendarDatePickerDialog calendarDatePickerDialog = (CalendarDatePickerDialog) getSupportFragmentManager()
				.findFragmentByTag(FRAG_TAG_DATE_PICKER);
		if (calendarDatePickerDialog != null) {
			calendarDatePickerDialog.setOnDateSetListener(this);
		}
	}

	@Override
	protected void onPause() {
		overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
		super.onPause();
	}

	@Override
	protected void onStop() {
		overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
		super.onDestroy();
	}

	@TargetApi(19)  
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else { 
			winParams.flags &= ~bits;
		} 
		win.setAttributes(winParams);
	} 

	public String getMonth(int month) {
		return new DateFormatSymbols().getMonths()[month-1];
	}
	
	public void displayError(int errorID){
		if(View.GONE == ((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).connectionState.getVisibility()){
			((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).connectionState.setVisibility(View.VISIBLE);
		}
		((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).connectionState.setBackgroundColor(getResources().getColor(R.color.not_connected));
		Log.w("Nightly", "Msg = " + getResources().getString(errorID));
		((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).connectionState.setText(getResources().getString(errorID));
	}
	
	public void alllGood(int errorID){
		if(View.GONE == ((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).connectionState.getVisibility()){
			((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).connectionState.setVisibility(View.VISIBLE);
		}
		((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).connectionState.setBackgroundColor(getResources().getColor(R.color.connected));
		Log.w("Nightly", "Msg = " + getResources().getString(errorID));
		((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).connectionState.setText(getResources().getString(errorID));
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
        	photoMode = 1;
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
        	photoMode = 2;
        	beginCrop(imageFilePath);
        } 
    }

    private void beginCrop(Uri source) {
        Uri outputUri = Uri.fromFile(new File(getCacheDir(), "cropped"));
        new Crop(source).output(outputUri).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
        	((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).profilPicture.setImageBitmap(getImageBitmap(Crop.getOutput(result).toString()));
        	((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).stateProfil.setImageResource(R.drawable.ic_action_accept);
        	((RegisterPartOne)((InitPagerAdapter) pagerAdapter).getItem(0)).profilPicture.setTag("done");
            quit(null);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private Bitmap rotateImage(Context context,Bitmap img, Uri selectedImage) {

        // Detect rotation
        int rotation= CropUtil.getExifRotation(CropUtil.getFromMediaUri(RegisterActivity.this, getContentResolver(), selectedImage));
        if(rotation!=0){
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
            img.recycle();
            return rotatedImg;        
        }else{
            return img;
        }
    }
    
    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            
            if(2 == photoMode){
            	bm = rotateImage(getApplicationContext(), bm, imageFilePath);
            }
            
            bis.close();
            is.close();
       } catch (IOException e) {
           Log.e("Nightly", "Error getting bitmap", e);
       }
       return bm;
    } 
    
    
    //PART TWO
    public void nextTwo(View v){
		if(((RegisterPartTwo)((InitPagerAdapter) pagerAdapter).getItem(1)).email.getText().toString().isEmpty()){
			displayErrorTwo(R.string.fill_all_fields);
			return;
		}
		if(!Utils.isValidEmail(((RegisterPartTwo)((InitPagerAdapter) pagerAdapter).getItem(1)).email.getText())){
			displayErrorTwo(R.string.email_invalid);
			return;
		}
		if(((RegisterPartTwo)((InitPagerAdapter) pagerAdapter).getItem(1)).password.getText().toString().isEmpty()){
			displayErrorTwo(R.string.fill_all_fields);
			return;
		}
		if(((RegisterPartTwo)((InitPagerAdapter) pagerAdapter).getItem(1)).repassword.getText().toString().isEmpty()){
			displayErrorTwo(R.string.fill_all_fields);
			return;
		}
		
		if(((RegisterPartTwo)((InitPagerAdapter) pagerAdapter).getItem(1)).password.getText().toString().length() < 6 || ((RegisterPartTwo)((InitPagerAdapter) pagerAdapter).getItem(1)).repassword.getText().toString().length() < 6){
			displayErrorTwo(R.string.password_to_short);
			return;
		}
		
		if(((RegisterPartTwo)((InitPagerAdapter) pagerAdapter).getItem(1)).password.getText().toString().length() < 6 && ((RegisterPartTwo)((InitPagerAdapter) pagerAdapter).getItem(1)).repassword.getText().toString().length() < 6){
			displayErrorTwo(R.string.password_to_short);
			return;
		}
		if(!((RegisterPartTwo)((InitPagerAdapter) pagerAdapter).getItem(1)).password.getText().toString().equals(((RegisterPartTwo)((InitPagerAdapter) pagerAdapter).getItem(1)).repassword.getText().toString())){
			displayErrorTwo(R.string.password_different);
			return;
		}
		if(!((RegisterPartTwo)((InitPagerAdapter) pagerAdapter).getItem(1)).checkCluf.isChecked()){
			displayErrorTwo(R.string.please_accept_cluf);
			return;
		}

		alllGoodTwo(R.string.all_good);
		Intent intent = new Intent(this, PhoneActivity.class);
		startActivity(intent);
		this.finish();
	}
    
    public void previousTwo(View v){
		pager.setCurrentItem(pager.getCurrentItem()-1, true);
	}
    
    public void displayErrorTwo(int errorID){
		if(View.GONE == ((RegisterPartTwo)((InitPagerAdapter) pagerAdapter).getItem(1)).connectionState.getVisibility()){
			((RegisterPartTwo)((InitPagerAdapter) pagerAdapter).getItem(1)).connectionState.setVisibility(View.VISIBLE);
		}
		((RegisterPartTwo)((InitPagerAdapter) pagerAdapter).getItem(1)).connectionState.setBackgroundColor(getResources().getColor(R.color.not_connected));
		Log.w("Nightly", "Msg = " + getResources().getString(errorID));
		((RegisterPartTwo)((InitPagerAdapter) pagerAdapter).getItem(1)).connectionState.setText(getResources().getString(errorID));
	}
    
    public void alllGoodTwo(int errorID){
		if(View.GONE == ((RegisterPartTwo)((InitPagerAdapter) pagerAdapter).getItem(1)).connectionState.getVisibility()){
			((RegisterPartTwo)((InitPagerAdapter) pagerAdapter).getItem(1)).connectionState.setVisibility(View.VISIBLE);
		}
		((RegisterPartTwo)((InitPagerAdapter) pagerAdapter).getItem(1)).connectionState.setBackgroundColor(getResources().getColor(R.color.connected));
		((RegisterPartTwo)((InitPagerAdapter) pagerAdapter).getItem(1)).connectionState.setText(getResources().getString(errorID));
	}

	@Override
	public void OnAccountCreatedSucced() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnAccountCreatedFailed(String reason) {
		// TODO Auto-generated method stub
		
	}
}
