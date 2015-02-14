package com.anthony.fernandez.nightly.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
			holder.textReceive = (TextView) row.findViewById(R.id.textReceive);
			holder.textSend = (TextView) row.findViewById(R.id.textSend);
			holder.dateReceive = (TextView) row.findViewById(R.id.dateReceive);
			holder.dateSend = (TextView) row.findViewById(R.id.dateSend);
			holder.imageContact = (CircleImageView) row.findViewById(R.id.imageContact);
			holder.container = (LinearLayout) row.findViewById(R.id.containerConversation);
			holder.menu = (Spinner) row.findViewById(R.id.conversationMenu);
			
			row.setTag(holder);
		} else {
			holder = (RowHolder) row.getTag();
		}

		Conversation = listConversations.get(position);

		int totalMessageReceive = Conversation.getListMessages().size()-1;
		int totalMessageSend = Conversation.getListMessagesSent().size()-1;
		
		holder.textReceive.setText(Conversation.getListMessages().get(totalMessageReceive).getMessage());
		holder.textSend.setText(Conversation.getListMessagesSent().get(totalMessageSend).getMessage());
		holder.container.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				holder.menu.performClick();
				return false;
			}
		});
		
		@SuppressWarnings("unused")
		String timeLaspeReceive = ""; //new Date().getTime(),Conversation.getListMessages().get(totalMessageReceive).getDateSend()

		return row;
	}

	static class RowHolder {
		LinearLayout container;
		Spinner menu;
		TextView textReceive;
		TextView textSend;
		TextView dateReceive;
		TextView dateSend;
		CircleImageView imageContact;
	}
}