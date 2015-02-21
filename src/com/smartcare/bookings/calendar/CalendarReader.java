package com.smartcare.bookings.calendar;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.format.DateUtils;

public class CalendarReader {
	
	public static List<CalendarEvent> readCalendar(Context context) {
		
		ContentResolver contentResolver = context.getContentResolver();

		// Fetch a list of all calendars synced with the device, their display names and whether the
		// user has them selected for display.
		
		final Cursor cursor = contentResolver.query(Uri.parse("content://com.android.calendar/calendars"),
				(new String[] { "_id", "displayName", "selected" }), null, null, null);
		// For a full list of available columns see http://tinyurl.com/yfbg76w

		HashSet<String> calendarIds = new HashSet<String>();
		
		while (cursor.moveToNext()) {

			final String _id = cursor.getString(0);
			final String displayName = cursor.getString(1);
			final Boolean selected = !cursor.getString(2).equals("0");
			
			System.out.println("Id: " + _id + " Display Name: " + displayName + " Selected: " + selected);
			calendarIds.add(_id);
		}
		
		List<CalendarEvent> calendarEvents = new LinkedList<CalendarEvent>();
		
		// For each calendar, display all the events from the previous week to the end of next week.		
		for (String id : calendarIds) {
			String calendarUri = "content://com.android.calendar/instances/when";
			String calendarUri2 = "content://calendar/instances/when";
			
			Uri.Builder builder = Uri.parse(calendarUri).buildUpon();
			
			long now = new Date().getTime();
			ContentUris.appendId(builder, now - DateUtils.WEEK_IN_MILLIS);
			ContentUris.appendId(builder, now + DateUtils.WEEK_IN_MILLIS);

			Cursor eventCursor = contentResolver.query(builder.build(),
					new String[] { "title", "begin", "end", "allDay"}, "Calendars._id=" + id,
					null, "startDay ASC, startMinute ASC"); 
			// For a full list of available columns see http://tinyurl.com/yfbg76w

			
			
			while (eventCursor.moveToNext()) {
				final String title = eventCursor.getString(0);
				final Date begin = new Date(eventCursor.getLong(1));
				final Date end = new Date(eventCursor.getLong(2));
				final Boolean allDay = !eventCursor.getString(3).equals("0");
				
				CalendarEvent calendarEvent = new CalendarEvent(title, begin, end, allDay);
				calendarEvents.add(calendarEvent);
//				System.out.println("Title: " + title + " Begin: " + begin + " End: " + end +
//						" All Day: " + allDay);
			}
			
		}
		
		return calendarEvents;
	}
}
