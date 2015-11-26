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
import java.util.List;

import tw.edu.ncu.cc.NCUCalendar.Event;
import tw.edu.ncu.cc.NCUCalendar.NCUCalendar;
import tw.edu.ncu.cc.NCUCalendar.ResponseListener;

public class WeekCalendar extends Fragment implements WeekView.MonthChangeListener, WeekView.EventClickListener, WeekView.EventLongPressListener  {
    private WeekView mWeekView;
    private List<WeekViewEvent> eventslist;
    private NCUCalendar NCUCalendar;
    private List<WeekViewEvent> eventslist_Jan;
    private List<WeekViewEvent> eventslist_Feb;
    private List<WeekViewEvent> eventslist_Mar;
    private List<WeekViewEvent> eventslist_Apr;
    private List<WeekViewEvent> eventslist_May;
    private List<WeekViewEvent> eventslist_Jun;
    private List<WeekViewEvent> eventslist_Jul;
    private List<WeekViewEvent> eventslist_Aug;
    private List<WeekViewEvent> eventslist_Sep;
    private List<WeekViewEvent> eventslist_Oct;
    private List<WeekViewEvent> eventslist_Nov;
    private List<WeekViewEvent> eventslist_Dec;
    public WeekCalendar(){
        eventslist = new ArrayList<WeekViewEvent>();
        eventslist = MainActivity.eventslist;
        eventslist_Jan = new ArrayList<WeekViewEvent>();
        eventslist_Jan = MainActivity.eventslist_Jan;
        eventslist_Feb = new ArrayList<WeekViewEvent>();
        eventslist_Feb = MainActivity.eventslist_Feb;
        eventslist_Mar = new ArrayList<WeekViewEvent>();
        eventslist_Mar = MainActivity.eventslist_Mar;
        eventslist_Apr = new ArrayList<WeekViewEvent>();
        eventslist_Apr = MainActivity.eventslist_Apr;
        eventslist_May = new ArrayList<WeekViewEvent>();
        eventslist_May = MainActivity.eventslist_May;
        eventslist_Jun = new ArrayList<WeekViewEvent>();
        eventslist_Jun = MainActivity.eventslist_Jun;
        eventslist_Jul = new ArrayList<WeekViewEvent>();
        eventslist_Jul = MainActivity.eventslist_Jul;
        eventslist_Aug = new ArrayList<WeekViewEvent>();
        eventslist_Aug = MainActivity.eventslist_Aug;
        eventslist_Sep = new ArrayList<WeekViewEvent>();
        eventslist_Sep = MainActivity.eventslist_Sep;
        eventslist_Oct = new ArrayList<WeekViewEvent>();
        eventslist_Oct = MainActivity.eventslist_Oct;
        eventslist_Nov = new ArrayList<WeekViewEvent>();
        eventslist_Nov = MainActivity.eventslist_Nov;
        eventslist_Dec = new ArrayList<WeekViewEvent>();
        eventslist_Dec = MainActivity.eventslist_Dec;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        switch (newMonth) {
            case 1:
                return eventslist_Jan;
            case 2:
                return eventslist_Feb;
            case 3:
                return eventslist_Mar;
            case 4:
                return eventslist_Apr;
            case 5:
                return eventslist_May;
            case 6:
                return eventslist_Jun;
            case 7:
                return eventslist_Jul;
            case 8:
                return eventslist_Aug;
            case 9:
                return eventslist_Sep;
            case 10:
                return eventslist_Oct;
            case 11:
                return eventslist_Nov;
            case 12:
                return eventslist_Dec;
            default:
                return eventslist;
        }
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