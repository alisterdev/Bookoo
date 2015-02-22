package com.bookoo.business;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named("report")
@SessionScoped
/**
 * Sets up a time frame for manager reports. 
 * Reports are displayed based on a start and end date. 
 * @author Yo
 *
 */
public class Report implements Serializable {

	private static final long serialVersionUID = 5892052395907635554L;
	private Calendar checkin;
	private Calendar checkout;
	private Date calendarStart;
	private Date calendarEnd;

	public Report() throws ParseException {
		super();
		checkin = new GregorianCalendar(2013, 01, 01);
		checkout = new GregorianCalendar(2014, 9, 01);

		calendarStart = checkin.getTime();
		calendarEnd = checkout.getTime();
	}

	public Date getCalendarStart() {
		return calendarStart;
	}

	public void setCalendarStart(Date calendarStart) {
		this.calendarStart = calendarStart;
	}

	public Date getCalendarEnd() {
		return calendarEnd;
	}

	public void setCalendarEnd(Date calendarEnd) {
		this.calendarEnd = calendarEnd;
	}

	public Calendar getCheckin() {
		checkin.setTime(calendarStart);
		return checkin;
	}

	public void setCheckin(Calendar checkin) {
		this.checkin = checkin;
	}

	public Calendar getCheckout() {
		checkout.setTime(calendarEnd);
		return checkout;
	}

	public void setCheckout(Calendar checkout) {
		this.checkout = checkout;
	}

	public TimeZone getTimeZone() {
		return TimeZone.getDefault();
	}

}
