/*
 * Copyright (c) 2012-2013 CVTeam.
 * Author: Mai Dinh
 * Date: 04/18/2013
 */

package com.cvteam.bkmanager.service;

import java.util.Calendar;
import java.util.HashSet;
import java.util.TimeZone;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class CalendarService {

	private static String eventUriString = "content://com.android.calendar/events";

	public static long NewEvent(Context context, String title, String description,
			String placeEvent, Calendar startTimeEvent, Calendar endTimeEvent,
			int minutesBeforeAlarm) {
		try {
			ContentValues eventValues = new ContentValues();
			eventValues.put("calendar_id", 1);
			eventValues.put("title", title);
			eventValues.put("description", description);
			eventValues.put("eventLocation", placeEvent);

			eventValues.put("dtstart", startTimeEvent.getTimeInMillis());
			eventValues.put("dtend", endTimeEvent.getTimeInMillis());
			// event.put("allDay", 1);

			eventValues.put("eventTimezone", TimeZone.getDefault().getID());
			eventValues.put("eventStatus", 1);
			eventValues.put("hasAlarm", 1);

			Uri eventUri = context.getApplicationContext().getContentResolver()
					.insert(Uri.parse(eventUriString), eventValues);
			long eventID = Long.parseLong(eventUri.getLastPathSegment());

			String reminderUriString = "content://com.android.calendar/reminders";
			ContentValues reminderValues = new ContentValues();

			reminderValues.put("event_id", eventID);
			reminderValues.put("minutes", minutesBeforeAlarm);
			reminderValues.put("method", 1);

			context.getApplicationContext().getContentResolver()
					.insert(Uri.parse(reminderUriString), reminderValues);

			return eventID;
		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		}
	}

	public static boolean DeleteEvent(Context context, long idEvent) {
		try {
			Uri deleteEventUri = Uri.withAppendedPath(Uri.parse(eventUriString),
					String.valueOf(idEvent));
			context.getContentResolver().delete(deleteEventUri, null, null);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public static void readCalendar(Context context) {
		ContentResolver contentResolver = context.getContentResolver();
		// Fetch a list of all calendars synced with the device, their display
		// names and whether the
		// user has them selected for display.
		final Cursor cursor = contentResolver.query(Uri.parse("content://calendar/calendars"),
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
	}
}
