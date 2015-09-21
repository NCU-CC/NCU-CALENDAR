package tw.edu.ncu.cc.GridViewCalendar;

import java.util.Calendar;

public class GVCalendarItem {
    private String dayString;
    private String yearString;
    private String monthString;
    private String planString;
    private boolean hasMark;
    private boolean isLastOrNextMonth;
    private boolean isToday;
    private int dayOfWeek;
    private Calendar calendar;

    public GVCalendarItem(){
        this.isToday = false;
        this.isLastOrNextMonth = false;
        this.hasMark = false;
        this.planString = "";
    }

    public Calendar getCalendar() {
        return calendar;
    }
    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public String getDateString() {
        return dayString;
    }
    public void setDateString(String dateString) {
        this.dayString = dateString;
    }

    public String getMonthString() {
        return monthString;
    }
    public void setMonthString(String monthString) {
        this.monthString = monthString;
    }

    public String getYearString() {
        return yearString;
    }
    public void setYearString(String yearString) {
        this.yearString = yearString;
    }

    public int getDayOfWeek() {return dayOfWeek;}
    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public boolean isHasPlan() {
        return hasMark;
    }
    public void setHasPlan(boolean hasPlan) {
        this.hasMark = hasPlan;
    }

    public boolean isLastOrNextMonth() {
        return isLastOrNextMonth;
    }
    public void setLastOrNextMonth(boolean isLastOrNextMonth) {
        this.isLastOrNextMonth = isLastOrNextMonth;
    }

    public boolean isToday() {
        return isToday;
    }
    public void setToday(boolean isToday) {
        this.isToday = isToday;
    }

    public String getPlanString() {
        return planString;
    }
    public void setPlanString(String planString) {
        this.planString = planString;
    }
}
