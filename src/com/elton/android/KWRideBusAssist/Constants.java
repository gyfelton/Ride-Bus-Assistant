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
	public static String SENDER_NUM = null;
	
	//available cities
	public static String TORONTO_NAME = "Toronto";
	public static String TORONTO_NUMBER = "898882";
	
	public static String WATERLOO_NAME = "Waterloo";
	public static String WATERLOO_NUMBER = "57555";
	
	public static String VANCOUVER_NAME = "Vancouver";
	public static String VANCOUVER_NUMBER = "33333";
	
	public static boolean SMS_INTERCEPTOR_IS_ACTIVE = false;
}
