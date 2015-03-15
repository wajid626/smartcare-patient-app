package com.smartcare.pillreminder.Helpers;

import java.sql.Date;

import android.text.format.Time;

/**
 * This is a class the creates a prescription object. The prescriptions
 * all have a set of attributes. This class allows the building
 * and accessing of the prescriptions and their attributes
 * @author peteryasi
 *
 */
public class PrescriptionsClass {
	
	//Attributes of the prescriptions
	private String prescriptionName;
	private int prescriptionId;
	private int hour;
	private int minute;
	private boolean[] weekdays;
	
	/**
	 * Constructor for the prescriptions. This is the method that is used
	 * to create a new prescription object
	 * 
	 * @param name
	 * @param id
	 * @param hour
	 * @param minute
	 * @param weekdays
	 */
	 public PrescriptionsClass(String name, int id, int hour, int minute, boolean[] weekdays){
		 this.prescriptionName=name;
		 this.prescriptionId=id;
		 this.hour=hour;
		 this.minute=minute;
		 this.weekdays=weekdays;
	    }
	
	 /**
	  * Sets the prescription's name
	  * @param name
	  */
	public void setPrescriptionName(String name){
		this.prescriptionName=name;
	}
	/**
	 * Gets the prescription's name
	 * @return
	 */
	public String getPrescriptionName(){
		return this.prescriptionName;
	}
	
	/**
	 * Sets the prescription's id
	 * @param id
	 */
	public void setPrescriptionId(int id){
		this.prescriptionId=id;
	}
	/**
	 * Gets the prescription's id
	 * @return
	 */
	public int getPrescriptionId(){
		return this.prescriptionId;
	}
	/**
	 * Sets the prescriptions hour
	 * @param hour
	 */
	public void setHour(int hour){
		this.hour=hour;
	}
	/**
	 * Gets the prescription's hour
	 * @return
	 */
	public int getHour(){
		return this.hour;
	}
	/**
	 * sets the prescription's minute
	 * @param minute
	 */
	public void setMinute(int minute){
		this.minute=minute;
	}
	/**
	 * Gets the prescription's minute
	 * @return
	 */
	public int getMinute(){
		return this.minute;
	}
	/**
	 * Sets the array of weekdays selected
	 * @param weekdays
	 */
	public void setWeekdays(boolean[] weekdays){
		this.weekdays=weekdays;
		
	}
	/**
	 * Gets the array of weekdays selected
	 * @return
	 */
	public boolean[] getWeekdays(){
		return this.weekdays;
	}
}
