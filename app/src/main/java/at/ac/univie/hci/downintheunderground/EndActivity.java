package at.ac.univie.hci.downintheunderground;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import at.ac.univie.hci.downintheunderground.db.StationDB;

import static at.ac.univie.hci.downintheunderground.QRStartScreen.STATION;

public class EndActivity extends AppCompatActivity {

    //Layout
    private TextView from;
    private TextView to;
    private TextView exit;
    private TextView level;
    private ImageView elevator;

    //intent vars.
    String street;
    String f;
    String t;

    //DB & Executors
    StationDB stationDB;
    ExecutorService executors = Executors.newSingleThreadExecutor();

    //DB vars and setters
    String e;
    private void setE(String s){
        e=s;
    }
    int lvl;
    private void setL(int l) {
        lvl=l;
    }
    boolean lift;
    private void setLift(boolean b){
        lift = b;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        //Layout init.
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        exit = findViewById(R.id.exit);
        level = findViewById(R.id.level);
        elevator = findViewById(R.id.elevator);
        //DB init
        stationDB = StationDB.getInstance(EndActivity.this);

        //Intent get
        Intent intent = getIntent();
        street = intent.getStringExtra("street");
        f = intent.getStringExtra(STATION);
        t = intent.getStringExtra("dest");

        //set from & to txt View
        from.setText(f);
        to.setText(t);

        //get info from DB
        executors.execute(new Runnable() {
            @Override
            public void run() {
                int exitID = stationDB.getStreetDao().getExitIDByName(street);
                int stationID = stationDB.getStationDao().findIDByName(t);
                String exitName = stationDB.getExitDao().getExitByID(exitID, stationID);
                setE(exitName);
                int l = stationDB.getExitDao().getLevel(exitID, stationID);
                setL(l);
                boolean b = stationDB.getExitDao().getElevatorInfo(exitID,stationID);
                setLift(b);
            }
        });
        //wait for thread
        try {
            executors.awaitTermination(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        //set exit & desc.
        exit.setText(e);

        String desc = "Go up to the ground level. Current level: -" + lvl + ". You can use stairs";
        if (lift) {
            desc += " or elevator.";
        }
        else desc += ".";
        level.setText(desc);

        //put picture
        if(lift) {
            elevator.setImageDrawable(getResources().getDrawable(R.drawable.elevator));
        }


    }
}
