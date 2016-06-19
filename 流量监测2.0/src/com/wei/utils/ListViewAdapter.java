package com.wei.utils;

import java.util.List;

import com.wei.checkliuliang.R;
import com.wei.javabean.ContentLiuliang;

import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {

	private List<ContentLiuliang> contacts;//存放的所有APP的流量信息
	LayoutInflater inflater;//布局填充器
	private int listview;
	private Context context;

	public ListViewAdapter(Context context, List<ContentLiuliang> contacts,
			int listview) {
		this.context=context;
		this.contacts = contacts;
		this.listview = listview;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);// 取得填充布局
	}

	public int getCount() {
		return contacts.size();
	}

	@Override
	public Object getItem(int position) {
		return contacts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// 显示view
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textViewname = null;
		TextView textViewsent = null;
		TextView textViewreceive = null;
		if (convertView == null) {
			convertView = inflater.inflate(listview, null);
			textViewname = (TextView) convertView.findViewById(R.id.tx_name);
			textViewsent = (TextView) convertView.findViewById(R.id.tx_sent);
			textViewreceive = (TextView) convertView
					.findViewById(R.id.tx_receive);
			convertView.setTag(new Itemshow(textViewname,textViewsent,textViewreceive));
		} 
		else // 把3个textview存进Itemshow
		{
			Itemshow itemshow = (Itemshow) convertView.getTag();
			textViewname = itemshow.textViewname;
			textViewsent = itemshow.textViewsent;
			textViewreceive = itemshow.textViewreceive;
		}
		ContentLiuliang contact = contacts.get(position);
		textViewname.setText(contact.getAppname());
		textViewsent.setText(String.valueOf(Formatter.formatFileSize(context,contact.getSentbyte())));
		textViewreceive.setText(String.valueOf(Formatter.formatFileSize(context,contact.getReceivebyte())));

		return convertView;// 别忘了是返回值的
	}

	public static void main(String[] args) {

	}

	/**
	 * 节省资源保存的3个textview
	 * 
	 * @author Corsair
	 * 
	 */
	public final class Itemshow {
		TextView textViewname;
		TextView textViewsent;
		TextView textViewreceive;

		public Itemshow(TextView textViewname, TextView textViewsent,
				TextView textViewreceive) {
			this.textViewname = textViewname;
			this.textViewsent = textViewsent;
			this.textViewreceive = textViewreceive;
		}
	}

}
