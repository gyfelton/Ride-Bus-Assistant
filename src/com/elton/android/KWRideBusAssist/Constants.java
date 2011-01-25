package com.elton.android.KWRideBusAssist;

public class Constants {
	
	public final static String DATABASE_NAME = "KWBusStopNumberAndDescription.db";
	public final static String TABLE_NAME = "BusStopNumberAndDescriptionAndOppositeNumber";
	
	//id here refers to bus stop number
	public final static String TABLE_ID = "_id";
	public final static String TABLE_DESCRIPTION = "busStopDescription";
	public final static String TABLE_DIRECTION = "busDirection";
	public final static String TABLE_OPPBUSSTOP = "oppositeToThisBusStop";
	//record count of request sent for this bus stop
	public final static String TABLE_HITCOUNT = "hitCount";
	
	//sender' number, will be modified later
	public static String SENDER_NUM = "57555";
	
	public static boolean SMS_INTERCEPTOR_IS_ACTIVE = false;
}
