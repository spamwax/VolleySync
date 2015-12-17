package cc.hamid.volleysync;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private RequestQueue mRequestQueue;
    private RequestFuture<JSONArray> future;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.mText);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Fake url, my actual url is working and provides correct response
        String url = "http://weather_api?recent=30";

        future = RequestFuture.newFuture();
        JsonArrayRequest mRequest = new JsonArrayRequest(Request.Method.GET, url, new JSONArray(),
                future, future) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
                params.put("Authorization", "Token " + "tokenString");
                return params;
            }
        };

        future.setRequest(mRequest);
        mRequest.setTag(this.getApplicationContext());

        mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueue.add(mRequest);


        Log.i("MainActivity", "--> In the onStart");
        try {
            JSONArray response = future.get(4, TimeUnit.SECONDS); // this will block for 4 seconds and then timeout :(
            Log.i("response", response.toString());
            mTextView.setText(response.toString());
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        } catch (ExecutionException e) {
            System.out.println("Execution");
        } catch (TimeoutException e) {
            Log.w("response", "--> Timed out!!!");
        }
    }
}
