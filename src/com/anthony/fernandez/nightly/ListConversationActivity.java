package com.anthony.fernandez.nightly;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.anthony.fernandez.nightly.adapter.ConversationsAdapter;
import com.anthony.fernandez.nightly.model.Conversation;
import com.anthony.fernandez.nightly.model.Message;
import com.anthony.fernandez.nightly.task.listener.OnListConversationsGet;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class ListConversationActivity extends SherlockActivity implements OnListConversationsGet, OnRefreshListener<ListView>{

	private ConversationsAdapter conversationAdapter;
	private ArrayList<Conversation> listConversations;
	private PullToRefreshListView listViewPullToRefresh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
		setContentView(R.layout.activity_list_conversation);

		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.action_bar_menu);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().getCustomView().findViewById(R.id.settings).setOnClickListener(new OnClickListener() {

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
		tintManager.setTintColor(getResources().getColor(R.color.facebook));

		listConversations = new ArrayList<Conversation>();
		conversationAdapter = new ConversationsAdapter(this, R.layout.row_conversation, listConversations);
		listViewPullToRefresh = (PullToRefreshListView)findViewById(R.id.listConversation);
		listViewPullToRefresh.setShowIndicator(false);
		listViewPullToRefresh.setOnRefreshListener(this);
		listViewPullToRefresh.setAdapter(conversationAdapter);

		generateConversations(3);

		Bundle bundle = getIntent().getExtras();
		if(bundle != null && bundle.containsKey("unread")){
			if(bundle.getBoolean("unread")){
				//do not display read conversations
				ArrayList<Conversation> listeBackup = (ArrayList<Conversation>)listConversations.clone();
				ArrayList<Conversation> liste = new ArrayList<Conversation>();
				int size = listConversations.size();
				for(int i=0 ; i< size; ++i){
					Conversation conv = listConversations.get(i);
					if(!conv.isRead()){
						liste.add(conv);
					}
				}
				listConversations.clear();
				listConversations.addAll(liste);
				conversationAdapter.notifyDataSetChanged();
			}
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

	private void settings(){
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

	@Override
	public void OnListConversationGet(ArrayList<Conversation> listConversation) {
		this.listConversations.clear();
		this.listConversations.addAll(listConversation);
		this.conversationAdapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
				DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		new RefreshConversation().execute();
	}

	public void openConversation(View v){
		Intent intent = new Intent(this, MessagingActivity.class);
		startActivity(intent);
	}

	private void generateConversations(int number) {
		int i;
		for(i=0 ; i<number ; ++i){
			Message mess = new Message("Comment vas-tu ?");
			Message mess2 = new Message("Bien et toi ?");
			Conversation conv = new Conversation(i);
			ArrayList<Message> listMess = new ArrayList<Message>();
			listMess.add(mess);

			ArrayList<Message> listMessSent = new ArrayList<Message>();
			listMessSent.add(mess2);

			conv.setListMessages(listMess);
			conv.setListMessagesSent(listMessSent);
			if((i & 1) == 0){
				conv.setRead(true);
			}

			listConversations.add(conv);
			conversationAdapter.notifyDataSetChanged();
		}
	}

	private class RefreshConversation extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(2000);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						generateConversations(1);
					}
				});
			} catch (InterruptedException e) {
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			listViewPullToRefresh.onRefreshComplete();
		}
	}
}
