package com.anthony.fernandez.nightly.adapter;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.anthony.fernandez.nightly.MessagingActivity;
import com.anthony.fernandez.nightly.R;
import com.anthony.fernandez.nightly.model.Conversation;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversationsAdapter extends ArrayAdapter<Conversation> {

	private ArrayList<Conversation> listConversations = null;
	private int layoutResourceId;
	private Context context;
	private Conversation Conversation;
	private RowHolder holder = null;
	
	public ConversationsAdapter(Context context, int layoutResourceId,
			ArrayList<Conversation> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.listConversations = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
	    
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new RowHolder();
			holder.containerConversation = (LinearLayout) row.findViewById(R.id.containerConversation);
			holder.container = (LinearLayout) row.findViewById(R.id.containerConversation);
			holder.containerInfos = (RelativeLayout) row.findViewById(R.id.containerInfo);
			holder.imageContact = (CircleImageView) row.findViewById(R.id.imageContact);
			holder.textReceive = (TextView) row.findViewById(R.id.textReceive);
			holder.dateReceive = (TextView) row.findViewById(R.id.dateReceive);
			holder.menu = (Spinner) row.findViewById(R.id.conversationMenu);
			holder.textSend = (TextView) row.findViewById(R.id.textSend);
			holder.dateSend = (TextView) row.findViewById(R.id.dateSend);
			holder.isRead = (View) row.findViewById(R.id.hasBeenRead);

			row.setTag(holder);
		} else {
			holder = (RowHolder) row.getTag();
		}

		Conversation = listConversations.get(position);

		int totalMessageReceive = Conversation.getListMessages().size()-1;
		int totalMessageSend = Conversation.getListMessagesSent().size()-1;

		if(Conversation.isRead()){
			holder.isRead.setVisibility(View.GONE);
		}else{
			holder.isRead.setVisibility(View.VISIBLE);
		}
		holder.textReceive.setText(Conversation.getListMessages().get(totalMessageReceive).getMessage());
		holder.textSend.setText(Conversation.getListMessagesSent().get(totalMessageSend).getMessage());
		//		holder.containerInfos.setOnLongClickListener(new OnLongClickListener() {
		//
		//			@Override
		//			public boolean onLongClick(View v) {
		//				holder.menu.performClick();
		//				return false;
		//			}
		//		});
		
		if(!Conversation.isRead()){
//			holder.containerInfos.setOnTouchListener(new OnTouchListener() {
//				int left = 0;
//				float initFingerPositionX = 0;
//				@SuppressLint("ClickableViewAccessibility")
//				@Override
//				public boolean onTouch(View v, MotionEvent event) {
//					switch (event.getAction()) {
//					case MotionEvent.ACTION_DOWN:
//						left = v.getLeft();
//						initFingerPositionX = event.getRawX();
//					case MotionEvent.ACTION_MOVE:
//						if((event.getRawX()-initFingerPositionX) >0){
//							v.setTranslationX(event.getRawX()-initFingerPositionX);
//						} else {
//							return false;
//						}
//					case MotionEvent.ACTION_UP:
//						animate(v).x(left).setDuration(400).start();
//					case MotionEvent.ACTION_CANCEL:
//						animate(v).x(left).setDuration(400).start();
//					}
//					return false;
//				}
//			});
		}

		@SuppressWarnings("unused")
		String timeLaspeReceive = ""; //new Date().getTime(),Conversation.getListMessages().get(totalMessageReceive).getDateSend()

		return row;
	}

	static class RowHolder {
		View isRead;
		LinearLayout container;
		Spinner menu;
		TextView textReceive;
		TextView textSend;
		TextView dateReceive;
		TextView dateSend;
		CircleImageView imageContact;
		RelativeLayout containerInfos;
		LinearLayout containerConversation;
	}
}