package tw.edu.ncu.cc.GridViewCalendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

import com.alamkanak.weekview.WeekViewEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tw.edu.ncu.cc.MainActivity;

public class GVCalendar extends GridView {
    Context context;

    private Calendar calToday;
    GVCalendarAdapter adapter;
    private List<GVCalendarItem> items = new ArrayList<GVCalendarItem>();

    public List<GVCalendarItem> getItems() {
        return items;
    }

    public void setItems(List<GVCalendarItem> items) {
        this.items = items;
    }

    private int year;
    private int month;
    private int day;

    public String getTitle(){
        return year+" 年 "+month+" 月";
    }

    private boolean isLoadDone;

    public boolean isLoadDone() {
        return isLoadDone;
    }

    private List<WeekViewEvent> eventslist;


    public GVCalendar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public GVCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public GVCalendar(Context context) {
        super(context);
        this.context = context;
    }

    public void initCalendar(){
        eventslist = new ArrayList<WeekViewEvent>();
        eventslist = MainActivity.eventslist;
        initCurrentTime();
        initCalendarItems(year, month, day);
        this.adapter = new GVCalendarAdapter(context, items);
        this.setAdapter(adapter);
    }

    public void refreshCalendar(){
        this.adapter.notifyDataSetChanged();
    }

    private void initCurrentTime() {
        calToday = Calendar.getInstance();
        year = calToday.get(Calendar.YEAR);
        month = calToday.get(Calendar.MONTH) + 1;
        day = calToday.get(Calendar.DAY_OF_MONTH);
    }

    private void initCalendarItems(int year, int month, int day) {
        this.items.clear();
        this.isLoadDone = false;

        int days = getDays(year, month);
        int predays;
        if(month == 1) {
            predays = getDays(year-1, 12);
        } else{
            predays = getDays(year, month-1);
        }
        int firstDayOfWeek = dayForWeek(year + "-" + month + "-01");

        if(firstDayOfWeek != 1)
        {
            for(int i = predays - firstDayOfWeek + 2; i <= predays; i++){
                GVCalendarItem cb = new GVCalendarItem();
                cb.setDateString(i+"");
                cb.setLastOrNextMonth(true);
                if(month==1){
                    cb.setMonthString(12 + "");
                }else{
                    cb.setMonthString((month - 1) % 12 + "");
                }
                if(month - 1 ==0){
                    cb.setYearString(year - 1 + "");
                }else{
                    cb.setYearString(year + "");
                }
                items.add(cb);
            }
        }

        for(int i = 1; i <= days; i++){
            GVCalendarItem cb = new GVCalendarItem();
            cb.setDateString(i+"");
            cb.setLastOrNextMonth(false);
            cb.setMonthString(month + "");
            cb.setYearString(year + "");
            if(i == day && calToday.get(Calendar.YEAR) == year && calToday.get(Calendar.MONTH) == month - 1){
                cb.setToday(true);
            }
            if(eventslist != null){
                for(WeekViewEvent eachevent : eventslist){
                    if(eachevent.getStartTime().get(Calendar.YEAR) == year &&
                            eachevent.getStartTime().get(Calendar.MONTH)+1 == month &&
                            eachevent.getStartTime().get(Calendar.DAY_OF_MONTH) == i){
                        cb.setHasPlan(true);
                    }
                }
            }
            cb.setDayOfWeek(dayForWeek(year + "-" + month + String.format("-%02d", i)));
            items.add(cb);
        }

        if((firstDayOfWeek - 1 + days) % 7 != 0){
            for(int i = 1; i<= 7 - (firstDayOfWeek - 1 + days) % 7; i++){
                GVCalendarItem cb = new GVCalendarItem();
                cb.setDateString(i + "");
                cb.setLastOrNextMonth(true);
                if(month==11){
                    cb.setMonthString((month+1) + "");
                }else{
                    cb.setMonthString((month + 1) % 12 + "");
                }
                if(month + 1 >12){
                    cb.setYearString(year+1 + "");
                }else{
                    cb.setYearString(year + "");
                }
                items.add(cb);
            }
        }

        if(adapter == null){
            adapter = new GVCalendarAdapter(context, items);
        }
        adapter.notifyDataSetChanged();
        this.isLoadDone = true;
    }

    private static int getDays(int year, int month) {
        int days = 0;
        boolean isLeapYear =  ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            case 2:
                days = isLeapYear ? 29 : 28;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
        }
        return days;
    }

    @SuppressLint("SimpleDateFormat")
    private static int dayForWeek(String pTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c.get(Calendar.DAY_OF_WEEK);
    }

    public void NextMonth() {
        if(month == 12){year+=1;month = 1;}
        else month = month + 1;
        initCalendarItems(year, month, day);
    }

    public void PreMonth() {
        if(month == 1){year-=1;month = 12;}
        else month = month - 1;
        initCalendarItems(year, month, day);
    }
}
