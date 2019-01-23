package com.anime.notivikasi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

public class Notivikasi extends AppCompatActivity implements View.OnClickListener {
    Button set_jadwal,cancel;
    FirebaseJobDispatcher mDispacher;
    private String DISPATCHER_TAG = "mydispatcher";
    private String CITY = "jakarta";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notivikasi);

        set_jadwal= findViewById(R.id.set_jadwal);
        cancel = findViewById(R.id.cancel);

        set_jadwal.setOnClickListener(this);
        cancel.setOnClickListener( this);
        mDispacher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
    }

    @Override
    public void onClick(View v) {

            if(v.getId()== R.id.set_jadwal){
                startDispatcher();
                Toast.makeText(this,"jadwal berhasil di buat",Toast.LENGTH_SHORT).show();
            }

            if ((v.getId())== R.id.cancel){
                cancelDispacher();
                Toast.makeText(this,"jadwal berhasil di cancel",Toast.LENGTH_SHORT).show();
            }
        }



    public void startDispatcher(){


        Bundle myExtrasBundle = new Bundle();
        myExtrasBundle.putString(MyJobService.EXTRA_CITY,CITY);


        Job myjob = mDispacher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag(DISPATCHER_TAG)
                .setRecurring(true)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setTrigger(Trigger.executionWindow(0,60))
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setConstraints(Constraint.ON_UNMETERED_NETWORK,Constraint.DEVICE_CHARGING,Constraint.DEVICE_IDLE


                )
                .setExtras(myExtrasBundle)
                .build();
        mDispacher.mustSchedule(myjob);

    }

    public void cancelDispacher(){
        mDispacher.cancel(DISPATCHER_TAG);
    }

}
