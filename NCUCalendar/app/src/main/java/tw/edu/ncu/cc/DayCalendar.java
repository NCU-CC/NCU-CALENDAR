package tw.edu.ncu.cc;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tw.edu.ncu.cc.NCUCalendar.Event;
import tw.edu.ncu.cc.NCUCalendar.NCUCalendar;
import tw.edu.ncu.cc.NCUCalendar.ResponseListener;

public class DayCalendar extends Fragment implements WeekView.MonthChangeListener, WeekView.EventClickListener, WeekView.EventLongPressListener  {
    private NCUCalendar NCUCalendar;
    private View rootView;
    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_DAY_VIEW;
    private WeekView mWeekView;
    private List<WeekViewEvent> eventslist;
    private Calendar Today;
    public static Calendar AssignedDay;
    public static Boolean Assign = false;
    public DayCalendar(){
        eventslist = new ArrayList<WeekViewEvent>();
        eventslist = MainActivity.eventslist;
    }
    private boolean EventPrinted = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.day_calendar, container, false);
        NCUCalendar = MainActivity.NCUCalendar;
        Today = Calendar.getInstance();
        if(Assign){
            Today.set(Calendar.DAY_OF_MONTH , AssignedDay.DAY_OF_MONTH);
        }else{
            AssignedDay = Today;
        }

        mWeekView = (WeekView) rootView.findViewById(R.id.weekView);
        mWeekView.setOnEventClickListener((WeekView.EventClickListener) this);
        mWeekView.setMonthChangeListener((WeekView.MonthChangeListener) this);
        mWeekView.setEventLongPressListener((WeekView.EventLongPressListener) this);

        mWeekView.goToDate(AssignedDay);
        Assign = false;

        return rootView;
    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> a = new ArrayList<WeekViewEvent>();
        if(!EventPrinted) {
            if(eventslist != null){
                EventPrinted = true;
                return eventslist;
            }
        }
        return a;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        GetEventDetail(MainActivity.ComparedList.get((int) event.getId()));
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(DayCalendar.this.getActivity(), event.getName(), Toast.LENGTH_SHORT).show();
    }

    private void GetEventDetail(String EventId)
    {
        NCUCalendar.getEvent(new ResponseListener<Event>() {
            public void onResponse(Event responses) {
                Event event = responses;
                Intent EventDisplayIntent = new Intent(DayCalendar.this.getActivity(), EventDisplay.class);
                Bundle bundle = new Bundle();
                bundle.putString("EventId", event.getId());
                bundle.putString("EventCreated_at", event.getCreated_at());
                bundle.putString("EventUpdated_at", event.getUpdated_at());
                bundle.putString("EventSummary", event.getSummary());
                bundle.putString("EventDescription", event.getDescription());
                bundle.putString("EventLocation", event.getLocation());
                bundle.putString("EventCategory", event.getCategory());
                bundle.putString("EventStart", event.getStart());
                bundle.putString("EventEnd", event.getEnd());
                bundle.putString("EventLink", event.getLink());
                EventDisplayIntent.putExtras(bundle);
                startActivity(EventDisplayIntent);
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(DayCalendar.this.getActivity(), "活動資料載入失敗。", Toast.LENGTH_SHORT).show();
            }
        }, EventId);
    }
}