package tw.edu.ncu.cc.NCUCalendar;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wuman.android.auth.OAuthManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NCUCalendar {
    private OAuthManager oauthManager;
    private RequestQueue queue;
    private String baseURL;
    private String language;
    private String token;

    public NCUCalendar(EventConfig config, OAuthManager oauthManager, Context context) {
        this.baseURL = config.getServerAddress();
        this.language = config.getLanguage();
        this.queue = Volley.newRequestQueue(context);
        this.oauthManager = oauthManager;
    }

    public void initAccessToken() {
        try {
            token = oauthManager.authorizeExplicitly( "user", null, null ).getResult().getAccessToken();
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    public void deleteAccessToken() {
        oauthManager.deleteCredential("user", null, null);
    }

    public void getAllEvents( ResponseListener< AllEvents > responseListener , String from , String to ) {
        get(
                "events?from=" + from + "&to=" + to + "&limit=50", responseListener, new TypeToken<AllEvents>() {}
        );
    }

    public void getCategories( ResponseListener< Categories > responseListener ) {
        get(
                "categories", responseListener, new TypeToken< Categories >(){}
        );
    }

    public void getEvent( ResponseListener< Event > responseListener , String EventId ) {
        get(
                "event?id=" + EventId, responseListener, new TypeToken<Event>() {}
        );
    }

    public void postEvent( ResponseListener< Event > responseListener , Event event) {
        post(
                "event", responseListener, new TypeToken<Event>() {}, event
        );
    }

    public void putEvent( ResponseListener< Event > responseListener , Event UpdateEvent) {
        put(
                "event", responseListener, new TypeToken<Event>() {}, UpdateEvent
        );
    }

    public void deleteEvent( ResponseListener< Event > responseListener , String EventId ) {
        delete(
                "event?id=" + EventId, responseListener, new TypeToken<Event>() {}
        );
    }

    private < T > void get( String path, final ResponseListener< T > responseListener, final TypeToken typeToken ) {
        queue.add( new StringRequest( Request.Method.GET, baseURL + path,
                new Response.Listener< String >() {
                    @Override
                    public void onResponse( String response ) {
                        T data = new Gson().fromJson( response, typeToken.getType() );
                        responseListener.onResponse(data);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse( VolleyError error ) {
                        responseListener.onError(error);
                    }
                }
        ) {
            public Map< String, String > getHeaders() throws AuthFailureError {
                Map< String, String > headers = new HashMap();
                headers.put( "Authorization", "Bearer " + token );
                return headers;
            }
        } );
    }

    private < T > void post( String path, final ResponseListener< T > responseListener, final TypeToken typeToken , final Event event ) {
        queue.add( new StringRequest( Request.Method.POST, baseURL + path,
                new Response.Listener< String >() {
                    @Override
                    public void onResponse( String response ) {
                        T data = new Gson().fromJson( response, typeToken.getType() );
                        responseListener.onResponse(data);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse( VolleyError error ) {
                        responseListener.onError(error);
                    }
                }
        ) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("summary", event.getSummary());
                params.put("description", event.getDescription());
                if(!event.getLink().isEmpty()){
                    params.put("link", event.getLink());}
                params.put("location",event.getLocation());
                params.put("category", event.getCategory());
                params.put("start", event.getStart());
                params.put("end", event.getEnd());
                return params;
            }

            public Map< String, String > getHeaders() throws AuthFailureError {
                Map< String, String > headers = new HashMap();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        } );
    }

    private < T > void put( String path, final ResponseListener< T > responseListener, final TypeToken typeToken, final Event UpdateEvent ) {
        queue.add( new StringRequest( Request.Method.PUT, baseURL + path,
                new Response.Listener< String >() {
                    @Override
                    public void onResponse( String response ) {
                        T data = new Gson().fromJson( response, typeToken.getType() );
                        responseListener.onResponse(data);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse( VolleyError error ) {
                        responseListener.onError(error);
                    }
                }
        ) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",UpdateEvent.getId());
                params.put("summary", UpdateEvent.getSummary());
                params.put("description", UpdateEvent.getDescription());
                if(UpdateEvent.getLink()!=null){
                    params.put("link", UpdateEvent.getLink());}
                params.put("location",UpdateEvent.getLocation());
                params.put("category", UpdateEvent.getCategory());
                params.put("start", UpdateEvent.getStart());
                params.put("end", UpdateEvent.getEnd());
                return params;
            }

            public Map< String, String > getHeaders() throws AuthFailureError {
                Map< String, String > headers = new HashMap();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        } );
    }

    private < T > void delete( String path, final ResponseListener< T > responseListener, final TypeToken typeToken) {
        queue.add( new StringRequest( Request.Method.DELETE, baseURL + path,
                new Response.Listener< String >() {
                    @Override
                    public void onResponse( String response ) {
                        T data = new Gson().fromJson( response, typeToken.getType() );
                        responseListener.onResponse(data);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse( VolleyError error ) {
                        responseListener.onError(error);
                    }
                }
        ) {
            public Map< String, String > getHeaders() throws AuthFailureError {
                Map< String, String > headers = new HashMap();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        } );
    }
}
