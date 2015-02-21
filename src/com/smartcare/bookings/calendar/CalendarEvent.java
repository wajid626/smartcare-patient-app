package com.smartcare.bookings.calendar;

import java.util.Date;

public class CalendarEvent {
	public String title;
	public Date begin;
	public Date end;
	public Boolean allDay;
	
	public CalendarEvent(String title, Date begin, Date end, Boolean allDay) {
		this(title, begin, end);
		this.allDay = allDay;
	}
	
	public CalendarEvent(String title, Date begin, Date end) {
		this.title = title;
		this.begin = begin;
		this.end = end;
	}
}
