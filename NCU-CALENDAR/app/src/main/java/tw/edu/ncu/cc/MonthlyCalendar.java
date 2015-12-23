package tw.edu.ncu.cc;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

import tw.edu.ncu.cc.GridViewCalendar.GVCalendar;
import tw.edu.ncu.cc.GridViewCalendar.GVCalendarItem;

public class MonthlyCalendar extends Fragment {
    public static MainActivity mainRefrence;
    private GVCalendar gvCalendar;
    private Button btnNext;
    private Button btnPre;
    private TextView tvDate;

    public MonthlyCalendar(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.monthly_calendar, container, false);
        gvCalendar = (GVCalendar)rootView.findViewById(R.id.gvCalendar);
        gvCalendar.initCalendar();
        btnPre = (Button)rootView.findViewById(R.id.btn_pre);
        btnNext = (Button)rootView.findViewById(R.id.btn_next);
        tvDate = (TextView)rootView.findViewById(R.id.tv_date);
        tvDate.setText(gvCalendar.getTitle());

        btnPre.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gvCalendar.PreMonth();
                tvDate.setText(gvCalendar.getTitle());
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gvCalendar.NextMonth();
                tvDate.setText(gvCalendar.getTitle());
            }
        });

        gvCalendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GVCalendarItem item = (GVCalendarItem) parent.getItemAtPosition(position);
                item.setHasPlan(!item.isHasPlan());
                Calendar gotoDate = Calendar.getInstance();

                gotoDate.set(Calendar.YEAR, Integer.parseInt(item.getYearString()));
                gotoDate.set(Calendar.MONTH, Integer.parseInt(item.getMonthString())-1);
                gotoDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(item.getDateString()));

                DayCalendar.AssignedDay = gotoDate;
                DayCalendar.Assign = true;
                mainRefrence.displayView(0);
            }
        });
        return rootView;
    }
}
