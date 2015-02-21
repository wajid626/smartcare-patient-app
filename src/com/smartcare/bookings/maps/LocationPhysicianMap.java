package com.smartcare.bookings.maps;

import java.util.EnumSet;
import java.util.HashMap;

import com.smartcare.bookings.enums.Physician;
import com.smartcare.bookings.enums.Location;

public class LocationPhysicianMap extends HashMap<Location, EnumSet<Physician>> {
	public LocationPhysicianMap() {
		put(Location.SanJose, EnumSet.of(
        		Physician.Dr_Frank, Physician.Dr_Lisa, Physician.Dr_Tom, Physician.Dr_Jeff));
        put(Location.Sunnyvale, EnumSet.of(
        		Physician.Dr_Tom, Physician.Dr_Lisa, Physician.Dr_Jeff, Physician.Dr_Frank));
        put(Location.SantaClara, EnumSet.of(
        		Physician.Dr_Frank, Physician.Dr_Lisa, Physician.Dr_Tom, Physician.Dr_Jeff));
        put(Location.SanFrancisco, EnumSet.of(
        		Physician.Dr_Frank, Physician.Dr_Lisa, Physician.Dr_Tom, Physician.Dr_Jeff));
	}
}
