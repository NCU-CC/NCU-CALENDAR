package tw.edu.ncu.cc;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class EventCreate extends Activity {

    private EditText title_text, event_date,time_begin,time_end,location,description,link;
    private String category;
    private Spinner spinner;
    private ArrayList<String> CategoryList = new ArrayList<String>();
    private ArrayAdapter<String> listAdapter;
    private NCUCalendar NCUCalendar;
    private String EventVarified = "您的資料不完全：\n";
    private Boolean DataCompleted = true;
    private Event newevent = new Event();
    private Intent intent;
    public static MainActivity mainRefrence;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private static final int TIME_PICKER_INTERVAL=15;
    private boolean mIgnoreEvent=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        intent = getIntent();

        NCUCalendar = MainActivity.NCUCalendar;
        CategoryList = MainActivity.CategoryList;

        spinner = (Spinner)findViewById(R.id.category);

        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CategoryList);
        spinner.setAdapter(listAdapter);

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

        processViews();
    }

    private void CreateEvent(Event event){
        NCUCalendar.postEvent(new ResponseListener<Event>() {
            public void onResponse(Event responses) {
                Toast.makeText(EventCreate.this, "活動新增成功！", Toast.LENGTH_SHORT).show();
                mainRefrence.getAllEvents();
            }

            @Override
            public void onError(VolleyError error) {
                if(error == null){
                    Toast.makeText(EventCreate.this, "活動新增成功！", Toast.LENGTH_SHORT).show();
                    mainRefrence.getAllEvents();
                }else{
                    Toast.makeText(EventCreate.this, "活動新增失敗！", Toast.LENGTH_SHORT).show();
                }

            }
        }, event);
    }

    private void processViews() {
        title_text = (EditText) findViewById(R.id.title_text);
        event_date = (EditText) findViewById(R.id.event_date);
        event_date.setOnClickListener(new EditText.OnClickListener() {
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        time_begin = (EditText) findViewById(R.id.time_begin);
        time_begin.setOnClickListener(new EditText.OnClickListener() {
            public void onClick(View v) {
                showTimePickerDialog("begin");
            }
        });
        time_end = (EditText) findViewById(R.id.time_end);
        time_end.setOnClickListener(new EditText.OnClickListener() {
            public void onClick(View v) {
                showTimePickerDialog("end");
            }
        });
        location = (EditText) findViewById(R.id.location);
        description = (EditText) findViewById(R.id.description);
        link = (EditText) findViewById(R.id.link);
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
        mHour = c.get(Calendar.HOUR_OF_DAY);
        if(c.get(Calendar.MINUTE)>29){
            mMinute = 30;
        }else{
            mMinute = 0;
        }

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
                }, mHour, mMinute, true);
        tpd.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_create, menu);
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
                String eventLink = link.getText().toString();
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
                    newevent.setSummary(titleText);
                    newevent.setStart(eventDate + "T" + timeBegin + "+08:00");
                    newevent.setEnd(eventDate + "T" + timeEnd + "+08:00");
                    newevent.setLocation(eventLocation);
                    newevent.setLink(eventLink);
                    newevent.setDescription(eventDescription);
                    newevent.setCategory(category);
                    CreateEvent(newevent);
                }
                else{
                    Toast.makeText(EventCreate.this, EventVarified , Toast.LENGTH_LONG).show();
                }
                break;
            default:
                finish();
                break;
        }
        finish();
    }
}
