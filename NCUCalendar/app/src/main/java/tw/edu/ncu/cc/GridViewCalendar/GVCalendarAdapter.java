package tw.edu.ncu.cc.GridViewCalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import tw.edu.ncu.cc.R;

public class GVCalendarAdapter extends BaseAdapter {
    List<GVCalendarItem> items;
    Context context;
    public GVCalendarAdapter(Context context, List<GVCalendarItem> items){
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.calendar_item, null);
            convertView.setFocusable(false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder.dateTextView.setText(items.get(position).getDateString());
        viewHolder.linearLayout.setBackgroundColor(0xffdddddd);
        viewHolder.dateTextView.setBackgroundDrawable(null);

        if(items.get(position).isLastOrNextMonth()){		//color of last or next month
            viewHolder.dateTextView.setTextColor(0xff666666);
            viewHolder.linearLayout.setBackgroundColor(0xffcccccc);
        }
        else{
            if(items.get(position).isHasPlan()){
            }
            if(items.get(position).getDayOfWeek() == Calendar.SUNDAY
                    || items.get(position).getDayOfWeek() == Calendar.SATURDAY){
                viewHolder.dateTextView.setTextColor(0xffe33125);
            }
            if(items.get(position).isToday()){
                viewHolder.dateTextView.setTextColor(0xff000000);
                viewHolder.dateTextView.setBackgroundColor(0xfffffd87);
            }
        }
        return convertView;
    }

    class ViewHolder{
        public ViewHolder(View view){
            this.linearLayout = (LinearLayout)view.findViewById(R.id.ci_ll_item);
            this.dateTextView = (TextView)view.findViewById(R.id.ci_tv_day);
        }
        LinearLayout linearLayout;
        TextView dateTextView;
    }
}
