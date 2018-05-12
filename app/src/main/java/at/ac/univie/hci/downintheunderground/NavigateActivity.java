package at.ac.univie.hci.downintheunderground;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import at.ac.univie.hci.downintheunderground.db.StationDB;

import static at.ac.univie.hci.downintheunderground.QRStartScreen.STATION;

public class NavigateActivity extends AppCompatActivity {
    //Layout
    private TextView from;
    private TextView to;
    private TextView richtung;
    private TextView steps;
    private ImageView elevatorImg;
    private Button next;

    //DB
    private StationDB stationDB;
    ExecutorService executors = Executors.newSingleThreadExecutor();

    //Intent receive
    String f;
    String t;
    String e;
    String street;

    //Exit info
    boolean elevator = true;
    int level = -11;
    boolean isKarlsplatz;

    //Get names from DB
    int frSt;
    int toID;
    String toSt;
    String dir;
    String desc;
    String trainSide;

    //Set values from Thread
    private void setFrom(int s) {
        frSt = s;
    }

    private void setToID(int i) {
        toID = i;
    }

    private void setTo(String s) {
        toSt = s;
    }

    private void setElevator(boolean b) {
        elevator = b;
    }

    private void setLevel(int i) {
        level = i;
    }

    private void setTrain(String s) {
        trainSide = s;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);

        //init DB
        stationDB = StationDB.getInstance(NavigateActivity.this);

        //init Layout vars.
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        richtung = findViewById(R.id.exit);
        steps = findViewById(R.id.level);
        elevatorImg = findViewById(R.id.elevator);
        next = findViewById(R.id.next);

        //get Intent
        Intent intent = getIntent();
        f = intent.getStringExtra(STATION);
        t = intent.getStringExtra("dest");
        e = intent.getStringExtra("exit");
        street = intent.getStringExtra("street");

       // e = intent.getIntExtra("destExit", 1);


        //Get info from DB
        executors.execute(new Runnable() {
            @Override
            public void run() {
                int arr = stationDB.getStreetDao().getExitIDByName(street);
                int i = stationDB.getStationDao().findIDByName(f);
                String b  = stationDB.getStationStreetDao().getStationForStreet(stationDB.getStreetDao().getStreetId(t)).name;
                int j = stationDB.getStationDao().findIDByName(b);
                String arrSide = stationDB.getExitDao().getTrain(arr, j);
                boolean el = stationDB.getExitDao().getElevatorInfo(stationDB.getExitDao().getExitID(i, e), i);
                int lvl = stationDB.getExitDao().getLevel(stationDB.getExitDao().getExitID(i, e),i);
                setFrom(i);
                setTo(b);
                setElevator(el);
                setLevel(lvl);
                setTrain(arrSide);
            }
        });
        //wait to thread
        try {
            executors.awaitTermination(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        //set from - to
        from.setText(f);
        to.setText(toSt);

        //find direction
        if(frSt < toID) {
            dir = "in Direction Karlsplatz";
            isKarlsplatz = true;
        }
        else if (frSt > toID) {
            dir = "in Direction Seestadt";
            isKarlsplatz = false;
        }
        richtung.setText(dir);

        //create steps
        //level & elevator info
        desc = "Go down to station level -" + level + ". You can use stairs";
        if(elevator) {
            desc += " or elevator.";
        }
        else desc += ".";

        //train step-in hint
        if(isKarlsplatz) {
            if(trainSide.equals("b")) {
                desc += " Use back of the Train.";
            }
            else if(trainSide.equals("f")) {
                desc += " Use front of the Train.";
            }
            //if no side given - display nothing extra
            else desc += "";
        }
        else {
            if(trainSide.equals("b")) {
                desc += " Use front of the Train";
            }
            else if (trainSide.equals("f")) {
                desc += " Use back of the Train";
            }
            //if no side given - display nothing extra
            else desc += "";
        }

        steps.setText(desc);

        //put picture
        if(elevator) {
            elevatorImg.setImageDrawable(getResources().getDrawable(R.drawable.elevator));
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavigateActivity.this, EndActivity.class);
                intent.putExtra("street", street);
                intent.putExtra(STATION, f);
                intent.putExtra("dest", toSt);
                intent.putExtra("richtung", isKarlsplatz);
                startActivity(intent);
            }
        });




    }
}
