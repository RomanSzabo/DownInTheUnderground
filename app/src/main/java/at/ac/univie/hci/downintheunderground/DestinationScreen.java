package at.ac.univie.hci.downintheunderground;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import at.ac.univie.hci.downintheunderground.db.DatabaseInitializer;
import at.ac.univie.hci.downintheunderground.db.Exit;
import at.ac.univie.hci.downintheunderground.db.ExitDao;
import at.ac.univie.hci.downintheunderground.db.Station;
import at.ac.univie.hci.downintheunderground.db.StationDB;

import static at.ac.univie.hci.downintheunderground.QRStartScreen.STATION;

public class DestinationScreen extends AppCompatActivity {

    //Layout vars.
    TextView from;
    EditText to;
    Button confirmButton;
    //db info
    String dest;
    String origin;
    String originExit;
    String street;
    int a;
    int b;
    //DB & Executors
    private StationDB stationDB;
    ExecutorService executors = Executors.newSingleThreadExecutor();
    int exit;

    //setters for db recieved
    private void set(String s) {
        this.street = s;
    }

    private void setA(int i) {
        this.a = i;
    }

    private void setB(int i) {
        this.b = i;
    }

    private void setExit(int e) {
        this.exit = e;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_screen);
        //db init.
        stationDB = StationDB.getInstance(DestinationScreen.this);
        //init. layout
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        confirmButton = findViewById(R.id.confirmButton);

        //get data from intent
        Intent intent = getIntent();
        origin = intent.getStringExtra(STATION);
        originExit = intent.getStringExtra("exit");

        //set from
        from.setText(origin);


        //button activity
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dest =  to.getText().toString();
                executors.execute(new Runnable() {
                    @Override
                    public void run() {
                       String s =  stationDB.getStreetDao().getStreetByName(dest);
                       //don't go further if street not in db, let err. cond. handle it
                       if(s != null) {
                           int i = stationDB.getStationStreetDao().getStationForStreet(stationDB.getStreetDao().getStreetId(s)).id;
                           set(s);
                           setA(i);
                       }
                    }
                });
                //wait for thread
                try {
                    executors.awaitTermination(100, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //check if street is in db
                if (street != null) {
                    //if in db, go to next activity
                    Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            int b = stationDB.getStationStreetDao().getStationForStreet(stationDB.getStreetDao().getStreetId(dest)).id;
                            int exit = stationDB.getStreetDao().getExitIDByName(dest);
                            setB(b);
                            setExit(exit);

                        }
                    });
                }
                else {
                    //if not in db, display error msg.
                    Toast.makeText(DestinationScreen.this, "No such Street!", Toast.LENGTH_SHORT).show();
                    return;
                }
                    //create intent and put data
                    Intent intent = new Intent(DestinationScreen.this, NavigateActivity.class);
                    intent.putExtra(STATION, origin);
                    intent.putExtra("dest", dest);
                    intent.putExtra("exit", originExit);
                    intent.putExtra("street", street);
                    startActivity(intent);

            }
        });
    }
}
