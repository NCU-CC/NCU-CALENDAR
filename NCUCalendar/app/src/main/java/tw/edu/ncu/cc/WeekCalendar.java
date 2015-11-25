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

public class WeekCalendar extends Fragment implements WeekView.MonthChangeListener, WeekView.EventClickListener, WeekView.EventLongPressListener  {
    private WeekView mWeekView;
    private List<WeekViewEvent> eventslist;
    private NCUCalendar NCUCalendar;
    public WeekCalendar(){
        eventslist = new ArrayList<WeekViewEvent>();
        eventslist = MainActivity.eventslist;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.week_calendar, container, false);
        NCUCalendar = MainActivity.NCUCalendar;

        mWeekView = (WeekView) rootView.findViewById(R.id.weekView);
        mWeekView.setOnEventClickListener(this);
        mWeekView.setMonthChangeListener(this);
        mWeekView.setEventLongPressListener(this);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> a = new ArrayList<WeekViewEvent>();
        Calendar c = Calendar.getInstance();
        if(eventslist!=null){
            if(newMonth == c.get(Calendar.MONTH) + 1){
                return eventslist;
            }
        }
        return a;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        GetEventDetail(MainActivity.ComparedList.get((int) event.getId()));
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(WeekCalendar.this.getActivity(), event.getName(), Toast.LENGTH_SHORT).show();
    }

    private void GetEventDetail(String EventId){
        NCUCalendar.getEvent(new ResponseListener<Event>() {
            public void onResponse(Event responses) {
                Event event = responses;
                Intent EventDisplayIntent = new Intent(WeekCalendar.this.getActivity(), EventDisplay.class);
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
                Toast.makeText(WeekCalendar.this.getActivity(), "活動資料載入失敗。", Toast.LENGTH_SHORT).show();
            }
        }, EventId);
    }
}