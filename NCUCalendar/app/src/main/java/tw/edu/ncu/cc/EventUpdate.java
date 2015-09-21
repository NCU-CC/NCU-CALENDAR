package tw.edu.ncu.cc;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import tw.edu.ncu.cc.mTimePickerDialog.mTimePickerDialog;
import tw.edu.ncu.cc.NCUCalendar.Event;
import tw.edu.ncu.cc.NCUCalendar.NCUCalendar;
import tw.edu.ncu.cc.NCUCalendar.ResponseListener;

public class EventUpdate extends Activity{

    private EditText title_text, event_date,time_begin,time_end,location,description,link;
    private String category;
    private Spinner spinner;
    private ArrayList<String> CategoryList = new ArrayList<String>();
    private ArrayAdapter<String> listAdapter;
    private int EventCategoryPoint;
    private Intent intent;
    private String EventVarified = "您的資料不完全：\n";
    private Boolean DataCompleted = true;
    private Event UpdateEvent = new Event();
    private NCUCalendar NCUCalendar;
    public static MainActivity mainRefrence;
    private int mYear, mMonth, mDay, mHour, mMinute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_update);
        NCUCalendar = MainActivity.NCUCalendar;
        CategoryList = MainActivity.CategoryList;

        processViews();
    }

    private void processViews() {
        intent = this.getIntent();
        title_text = (EditText) findViewById(R.id.title_text);
        title_text.setText(intent.getStringExtra("EventSummary"));

        String[] StartTime = intent.getStringExtra("EventStart").split("T");
        String[] StartTime_YYYYMMDD = StartTime[0].split("-");
        String[] StartTime_HHMM = StartTime[1].split(":");

        event_date = (EditText) findViewById(R.id.event_date);
        event_date.setText(StartTime_YYYYMMDD[0] + "-" + StartTime_YYYYMMDD[1] + "-" + StartTime_YYYYMMDD[2]);
        event_date.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (v.getId() == event_date.getId()){
                    showDatePickerDialog();
                }
            }
        });

        time_begin = (EditText) findViewById(R.id.time_begin);
        time_begin.setText(StartTime_HHMM[0] + ":" + StartTime_HHMM[1]);
        time_begin.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        showTimePickerDialog("begin");
                    }
                });

        String[] EndTime = intent.getStringExtra("EventEnd").split("T");
        String[] EndTime_HHMM = EndTime[1].split(":");

        time_end = (EditText) findViewById(R.id.time_end);
        time_end.setText(EndTime_HHMM[0] + ":" + EndTime_HHMM[1]);
        time_end.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        showTimePickerDialog("end");
                    }
                });

        location = (EditText) findViewById(R.id.location);
        location.setText(intent.getStringExtra("EventLocation"));

        description = (EditText) findViewById(R.id.description);
        description.setText(intent.getStringExtra("EventDescription"));

        link = (EditText) findViewById(R.id.link);
        if (intent.getStringExtra("EventLink") == null) {
            link.setText("");
        } else {
            link.setText(intent.getStringExtra("EventLink"));
        }

        spinner = (Spinner)findViewById(R.id.category);

        for (int i = 0; i < CategoryList.size(); i++){
            if(intent.getStringExtra("EventCategory").equals(CategoryList.get(i))){
                EventCategoryPoint = i;
            }
        }

        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CategoryList);
        spinner.setAdapter(listAdapter);
        spinner.setSelection(EventCategoryPoint);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                category = CategoryList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar mCalendar = Calendar.getInstance();
                        mCalendar.set(Calendar.YEAR, year);
                        mCalendar.set(Calendar.MONTH, monthOfYear);
                        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "yyyy-MM-dd";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.TAIWAN);
                        event_date.setText(sdf.format(mCalendar.getTime()));
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

    public void showTimePickerDialog(final String BorE) {
        final Calendar c = Calendar.getInstance();
        if(c.get(Calendar.MINUTE)>29){
            mMinute = 30;
        }else{
            mMinute = 0;
        }
        mMinute = c.get(Calendar.MINUTE);

        mTimePickerDialog tpd = new mTimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar mCalendar = Calendar.getInstance();
                        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        mCalendar.set(Calendar.MINUTE, minute);
                        String myFormat = "HH:mm";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.TAIWAN);
                        if(BorE == "begin"){
                            time_begin.setText(sdf.format(mCalendar.getTime()));
                        }
                        else if(BorE == "end"){
                            time_end.setText(sdf.format(mCalendar.getTime()));
                        }
                    }
                }, mHour, mMinute, false);
        tpd.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSubmit(View view) {
        switch (view.getId()) {
            case R.id.ok_item:
                String titleText = title_text.getText().toString();
                if (titleText.isEmpty()) {
                    EventVarified += "缺少活動名稱！\n";
                    DataCompleted = false;
                }
                String eventDate = event_date.getText().toString();
                if (eventDate.isEmpty()) {
                    EventVarified += "缺少活動日期！\n";
                    DataCompleted = false;
                }
                String timeBegin = time_begin.getText().toString();
                if (timeBegin.isEmpty()) {
                    EventVarified += "缺少活動開始時間！\n";
                    DataCompleted = false;
                }
                String timeEnd = time_end.getText().toString();
                if (timeEnd.isEmpty()) {
                    EventVarified += "缺少活動結束時間！\n";
                    DataCompleted = false;
                }
                String eventLocation = location.getText().toString();
                if (eventLocation.isEmpty()) {
                    EventVarified += "缺少活動地點！\n";
                    DataCompleted = false;
                }
                String eventLink;
                if (intent.getStringExtra("EventLink") == null) {
                    eventLink = intent.getStringExtra("EventLink");
                }else{
                    eventLink = link.getText().toString();
                }
                String eventDescription = description.getText().toString();
                if (eventDescription.isEmpty()) {
                    EventVarified += "請填寫活動介紹！\n";
                    DataCompleted = false;
                }
                if(category == "請選擇活動類別") {
                    EventVarified += "請選擇活動類別！\n";
                    DataCompleted = false;
                }

                if(DataCompleted) {
                    UpdateEvent.setId(intent.getStringExtra("EventId"));
                    UpdateEvent.setSummary(titleText);
                    UpdateEvent.setStart(eventDate + "T" + timeBegin + "+08:00");
                    UpdateEvent.setEnd(eventDate + "T" + timeEnd + "+08:00");
                    UpdateEvent.setLocation(eventLocation);
                    UpdateEvent.setLink(eventLink);
                    UpdateEvent.setDescription(eventDescription);
                    UpdateEvent.setCategory(category);
                    UpdateEvent(UpdateEvent);
                }
                else{
                    Toast.makeText(EventUpdate.this, EventVarified , Toast.LENGTH_LONG).show();
                }
                break;
            default:
                finish();
                break;
        }
    }

    private void UpdateEvent(Event event){
        NCUCalendar.putEvent(new ResponseListener<Event>() {
            public void onResponse(Event responses) {
                Toast.makeText(EventUpdate.this, "活動修改成功！", Toast.LENGTH_SHORT).show();
                finish();
                mainRefrence.getAllEvents();
            }

            @Override
            public void onError(VolleyError error) {
                if(error == null){
                    Toast.makeText(EventUpdate.this, "活動修改成功！", Toast.LENGTH_SHORT).show();
                    finish();
                    mainRefrence.getAllEvents();
                }
                Toast.makeText(EventUpdate.this, "活動修改失敗！", Toast.LENGTH_SHORT).show();
            }
        }, event);
    }
}
