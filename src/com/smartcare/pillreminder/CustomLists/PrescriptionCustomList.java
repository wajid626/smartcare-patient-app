package com.smartcare.pillreminder.CustomLists;

import java.util.List;

import com.smartcare.bookings.R;
import com.smartcare.bookings.R.id;
import com.smartcare.bookings.R.layout;
import com.smartcare.bookings.R.menu;
import com.smartcare.pillreminder.Helpers.PrescriptionsClass;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PrescriptionCustomList extends ArrayAdapter<PrescriptionsClass>{

	private final Activity context;
	private final String[] name;
	private final Integer[] imageId;
	private int[] hour;
	private int[] minute;

	public PrescriptionCustomList(Activity context, String[] name, Integer[] imageId, int[] hour, int[] minute) {
		super(context, R.layout.prescription_list);
		this.context=context;
		this.name=name;
		this.imageId=imageId;
		this.hour=hour;
		this.minute=minute;

		// TODO Auto-generated constructor stub
	}
	//
	@Override
	public View getView(int position, View view, ViewGroup parent) {

		LayoutInflater inflater = context.getLayoutInflater();
		View rowView= inflater.inflate(R.layout.prescription_list, null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
		txtTitle.setText(name[position]);
		imageView.setImageResource(imageId[position]);
		return rowView;

	}


}
