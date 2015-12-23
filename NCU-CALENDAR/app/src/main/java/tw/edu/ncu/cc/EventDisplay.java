package tw.edu.ncu.cc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.util.ArrayList;

import tw.edu.ncu.cc.NCUCalendar.Event;
import tw.edu.ncu.cc.NCUCalendar.NCUCalendar;
import tw.edu.ncu.cc.NCUCalendar.ResponseListener;

public class EventDisplay extends Activity {

    private TextView title_text, event_date,time_begin,time_end,location,description,link, category;
    private ArrayList<String> CategoryList = new ArrayList<String>();
    private ArrayAdapter<String> listAdapter;
    private Intent intent;
    private NCUCalendar NCUCalendar;
    public static MainActivity mainRefrence;
    private AlertDialog dialog;

    public EventDisplay(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_display);
        NCUCalendar = MainActivity.NCUCalendar;
        CategoryList = MainActivity.CategoryList;

        processViews();
    }

    private void processViews() {
        intent = this.getIntent();
        title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText(intent.getStringExtra("EventSummary"));

        String[] StartTime = intent.getStringExtra("EventStart").split("T");
        String[] StartTime_YYYYMMDD = StartTime[0].split("-");
        String[] StartTime_HHMM = StartTime[1].split(":");

        event_date = (TextView) findViewById(R.id.event_date);
        event_date.setText(StartTime_YYYYMMDD[0] + "-" + StartTime_YYYYMMDD[1] + "-" + StartTime_YYYYMMDD[2]);

        time_begin = (TextView) findViewById(R.id.time_begin);
        time_begin.setText(StartTime_HHMM[0] + ":" + StartTime_HHMM[1]);

        String[] EndTime = intent.getStringExtra("EventEnd").split("T");
        String[] EndTime_HHMM = EndTime[1].split(":");

        time_end = (TextView) findViewById(R.id.time_end);
        time_end.setText(EndTime_HHMM[0] + ":" + EndTime_HHMM[1]);

        location = (TextView) findViewById(R.id.location);
        location.setText(intent.getStringExtra("EventLocation"));

        description = (TextView) findViewById(R.id.description);
        description.setText(intent.getStringExtra("EventDescription"));

        link = (TextView) findViewById(R.id.link);
        if (intent.getStringExtra("EventLink") == null) {
            link.setText("無活動網址。");
        } else {
            link.setText(intent.getStringExtra("EventLink"));
        }

        category = (TextView) findViewById(R.id.category);
        category.setText(intent.getStringExtra("EventCategory"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_display, menu);
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
                break;
            case R.id.update_item:
                Intent EventUpdateIntent = new Intent(this, EventUpdate.class);
                Bundle bundle = new Bundle();
                bundle.putString("EventId", intent.getStringExtra("EventId"));
                bundle.putString("EventCreated_at", intent.getStringExtra("EventCreated_at"));
                bundle.putString("EventUpdated_at", intent.getStringExtra("EventUpdated_at"));
                bundle.putString("EventSummary", intent.getStringExtra("EventSummary"));
                bundle.putString("EventDescription", intent.getStringExtra("EventDescription"));
                bundle.putString("EventLocation", intent.getStringExtra("EventLocation"));
                bundle.putString("EventCategory", intent.getStringExtra("EventCategory"));
                bundle.putString("EventStart", intent.getStringExtra("EventStart"));
                bundle.putString("EventEnd", intent.getStringExtra("EventEnd"));
                bundle.putString("EventLink", intent.getStringExtra("EventLink"));
                EventUpdateIntent.putExtras(bundle);
                startActivity(EventUpdateIntent);
                finish();
                break;
            case R.id.delete_item:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.delete);
                builder.setMessage(R.string.delete_check);
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DeleteEvent(intent.getStringExtra("EventId"));
                    }
                });
                dialog = builder.show();
                break;
            default:
                finish();
                break;
        }
    }

    private void DeleteEvent(String EventId) {
        NCUCalendar.deleteEvent(new ResponseListener<Event>() {
            public void onResponse(Event responses) {
                Toast.makeText(EventDisplay.this, "活動刪除成功！", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();
                mainRefrence.getAllEvents();
            }

            @Override
            public void onError(VolleyError error) {
                if(error == null){
                    Toast.makeText(EventDisplay.this, "活動刪除成功！", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    finish();
                    mainRefrence.getAllEvents();
                }
                Toast.makeText(EventDisplay.this, "活動刪除失敗！", Toast.LENGTH_SHORT).show();
            }
        }, EventId);
    }
}
