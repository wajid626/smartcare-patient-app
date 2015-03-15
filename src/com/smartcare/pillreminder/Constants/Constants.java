package com.smartcare.pillreminder.Constants;

import java.util.ArrayList;

import com.smartcare.pillreminder.Helpers.PrescriptionsClass;

/**
 * Class contains my constants
 * 
 * @author peteryasi
 * 
 */
public class Constants {

	// List of prescriptions
	public static ArrayList<String> PRESCRIPTIONS = new ArrayList<String>();
	public static ArrayList<PrescriptionsClass> PRESCRIPTIONS2 = new ArrayList<PrescriptionsClass>();
	public static PrescriptionsClass[] myPrescriptions;

	// Ints used for alarm manager
	public static int chosenHour;
	public static int chosenMinute;
}
