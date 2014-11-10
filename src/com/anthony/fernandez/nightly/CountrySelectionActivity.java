package com.anthony.fernandez.nightly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.countrypicker.CountryPicker;
import com.countrypicker.CountryPickerListener;

public class CountrySelectionActivity extends SherlockFragmentActivity implements CountryPickerListener{

	private CountryPicker picker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
		setContentView(R.layout.activity_country_selection);

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		picker = new CountryPicker();
		picker.setListener(this);
		transaction.replace(R.id.countryListView, picker);
		transaction.commit();
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

	@Override
	public void onBackPressed() {
		this.finish();
		super.onBackPressed();
	}

	@Override
	public void onSelectCountry(String name, String code) {
		Intent data = new Intent();
		data.putExtra("code", code);
		if (getParent() == null) {
			setResult(Activity.RESULT_OK, data);
		} else {
			getParent().setResult(Activity.RESULT_OK, data);
		}
		finish();
	}
}
