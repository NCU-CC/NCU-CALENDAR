package tw.edu.ncu.cc.NCUCalendar;

import com.android.volley.VolleyError;

public interface ResponseListener<T> {
    public void onResponse( T responses );
    public void onError( VolleyError error );
}
