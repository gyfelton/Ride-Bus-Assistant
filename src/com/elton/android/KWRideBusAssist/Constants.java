package com.elton.android.KWRideBusAssist;

public class Constants {
	
	public final static String DATABASE_NAME = "KWBusStopNumberAndDescription.db";
	public final static String TABLE_NAME = "BusStopNumberAndDescriptionAndOppositeNumber";
	
	//id here refers to bus stop number
	public final static String TABLE_ID = "_id";
	public final static String TABLE_DETAIL = "busStopDescription";
	public final static String TABLE_DETAIL2 = "oppositeToThisBusStop";
	//record count of request sent for this bus stop
	public final static String TABLE_DETAIL3 = "hitCount";
	
	//sender' number, will be modified later
	public static String SENDER_NUM = "57555";
	
	public static boolean ACTIVE = false;
}
