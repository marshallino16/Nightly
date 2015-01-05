package com.anthony.fernandez.nightly;

import java.util.Date;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.anthony.fernandez.nightly.adapter.MessageAdapter;
import com.anthony.fernandez.nightly.enums.MessageDirection;
import com.anthony.fernandez.nightly.model.Message;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class MessagingActivity extends SherlockActivity {

	private EditText messageBodyField;
	private MessageAdapter messageAdapter;
	private ListView messagesList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
		setContentView(R.layout.messaging);

		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.action_bar_menu);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().getCustomView().findViewById(R.id.settings)
		.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				settings();
			}
		});

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setTintColor(getResources().getColor(
				R.color.blue_aciton_bar));

		messagesList = (ListView) findViewById(R.id.listMessages);
		messageAdapter = new MessageAdapter(this);
		messagesList.setAdapter(messageAdapter);

		messageBodyField = (EditText) findViewById(R.id.messageBodyField);

		findViewById(R.id.sendButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						sendMessage();
					}
				});
		
		stubPopulate();
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

	private void sendMessage() {
		String messageBody = messageBodyField.getText().toString();
		if (messageBody.isEmpty()) {
			Toast.makeText(this, "Please enter a message", Toast.LENGTH_LONG)
			.show();
			return;
		}
		Message message = new Message(messageBody);
		message.setDateSend(new Date().getTime());
		message.setDirection(MessageDirection.OUTCOMMING);
		messageAdapter.addMessage(message);
		// send message
		messageBodyField.setText("");
	}
	
	private void stubPopulate(){
		Message mess = new Message();
		mess.setMessage("Hello comment vas-tu ?");
		mess.setDirection(MessageDirection.INCOMMING);
		
		Message mess2 = new Message();
		mess2.setMessage("Bien et toi ?");
		mess2.setDirection(MessageDirection.OUTCOMMING);
		
		Message mess3 = new Message();
		mess3.setMessage("Bien merci, tu aimes les fraises ?");
		mess3.setDirection(MessageDirection.INCOMMING);
		
		Message mess4 = new Message();
		mess4.setMessage("A l'aise !");
		mess4.setDirection(MessageDirection.OUTCOMMING);
		
		messageAdapter.addMessage(mess);
		messageAdapter.addMessage(mess2);
		messageAdapter.addMessage(mess3);
		messageAdapter.addMessage(mess4);
	}

	private void settings() {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
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
}
