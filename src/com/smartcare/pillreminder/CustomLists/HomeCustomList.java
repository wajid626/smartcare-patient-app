package com.smartcare.pillreminder.CustomLists;

import com.smartcare.bookings.R;
import com.smartcare.bookings.R.id;
import com.smartcare.bookings.R.layout;
import com.smartcare.bookings.R.menu;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Class to build the customListView for the homescreen
 * 
 * @author peteryasi
 * 
 */
public class HomeCustomList extends ArrayAdapter<String> {

	// Global variables
	private final Activity context;
	private final String[] items;
	private final Integer[] imageId;

	/**
	 * contructor of the customListView
	 * 
	 * @param context
	 * @param items
	 * @param imageId
	 */
	public HomeCustomList(Activity context, String[] items, Integer[] imageId) {
		super(context, R.layout.home_list, items);
		this.context = context;
		this.items = items;
		this.imageId = imageId;
	}

	/**
	 * Inflates the layout and displays the listview
	 */
	@Override
	public View getView(int position, View view, ViewGroup parent) {

		// inflate the layout
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.home_list, null, true);

		// Set the textview
		TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);

		// set the image
		ImageView imageView = (ImageView) rowView.findViewById(R.id.img);

		// Set the position
		txtTitle.setText(items[position]);
		imageView.setImageResource(imageId[position]);
		return rowView;

	}
}
