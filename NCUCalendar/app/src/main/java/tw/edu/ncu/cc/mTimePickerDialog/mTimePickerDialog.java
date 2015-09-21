package tw.edu.ncu.cc.mTimePickerDialog;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

public class mTimePickerDialog extends TimePickerDialog {

    public static final int TIME_PICKER_INTERVAL=30;
    private boolean mIgnoreEvent=false;

    public mTimePickerDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute,
                             boolean is24HourView) {
        super(context, callBack, hourOfDay, minute, is24HourView);
    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
        super.onTimeChanged(timePicker, hourOfDay, minute);
        this.setTitle("2. Select Time");
        if (!mIgnoreEvent){
            minute = getRoundedMinute(minute);
            mIgnoreEvent=true;
            timePicker.setCurrentMinute(minute);
            mIgnoreEvent=false;
        }
    }

    public static int getRoundedMinute(int minute){
        if(minute % TIME_PICKER_INTERVAL != 0){
            int minuteFloor = minute - (minute % TIME_PICKER_INTERVAL);
            minute = minuteFloor + (minute == minuteFloor + 1 ? TIME_PICKER_INTERVAL : 0);
            if (minute == 60)  minute=0;
        }

        return minute;
    }
}
