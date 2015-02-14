package com.anthony.fernandez.nightly.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.anthony.fernandez.nightly.R;
import com.anthony.fernandez.nightly.enums.MessageDirection;
import com.anthony.fernandez.nightly.model.Message;

public class MessageAdapter extends BaseAdapter {

	private List<Message> messages;
	private LayoutInflater layoutInflater;

	public MessageAdapter(Activity activity) {
		layoutInflater = activity.getLayoutInflater();
		messages = new ArrayList<Message>();
	}

	public void addMessage(Message message) {
		messages.add(message);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return messages.size();
	}

	@Override
	public Object getItem(int i) {
		return messages.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int i) {
		return messages.get(i).getDirection().status;
	}

	@Override
	public View getView(int i, View convertView, ViewGroup viewGroup) {
		int direction = getItemViewType(i);

		Message message = messages.get(i);

		//show message on left or right, depending on if
		//it's incoming or outgoing
		if (convertView == null) {
			int res = 0;
			if (direction == MessageDirection.OUTCOMMING.status) {
				if(message.isPending()){
					res = R.layout.message_right_pending;
				} else {
					res = R.layout.message_right;
				}
			} else if (direction == MessageDirection.INCOMMING.status) {
				res = R.layout.message_left;
			}
			convertView = layoutInflater.inflate(res, viewGroup, false);
		}

		TextView txtMessage = (TextView) convertView.findViewById(R.id.txtMessage);
		txtMessage.setText(message.getMessage());

		return convertView;
	}
}

