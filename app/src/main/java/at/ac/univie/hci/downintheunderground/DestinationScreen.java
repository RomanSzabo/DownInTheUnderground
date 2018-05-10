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
import java.util.concurrent.Executors;

import at.ac.univie.hci.downintheunderground.db.DatabaseInitializer;
import at.ac.univie.hci.downintheunderground.db.Exit;
import at.ac.univie.hci.downintheunderground.db.ExitDao;
import at.ac.univie.hci.downintheunderground.db.Station;
import at.ac.univie.hci.downintheunderground.db.StationDB;

import static at.ac.univie.hci.downintheunderground.QRStartScreen.STATION;

public class DestinationScreen extends AppCompatActivity {

    TextView from;
    EditText to;
    Button confirmButton;
    String dest;
    String origin;
    String street;
    int a;
    int b;
    private StationDB stationDB;
    int exit;


    private void set(String s) {
        this.street = s;
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

        stationDB = StationDB.getInstance(DestinationScreen.this);

        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        confirmButton = findViewById(R.id.confirmButton);

        Intent intent = getIntent();
        origin = intent.getStringExtra(STATION);
        from.setText(origin);


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dest =  to.getText().toString();
                Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                       String s =  stationDB.getStreetDao().getStreetByName(dest);
                       set(s);
                    }
                });
                if (street == null) {
                    Toast.makeText(DestinationScreen.this, "No such Street!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            int b = stationDB.getStationStreetDao().getStationForStreet(stationDB.getStreetDao().getStreetId(dest)).id;
                            int exit  = stationDB.getStreetDao().getExitIDByName(dest);
                            setB(b);
                            setExit(exit);
                        }
                    });
                    Intent intent = new Intent(DestinationScreen.this, NavigateActivity.class);
                    intent.putExtra(STATION, a);
                    intent.putExtra("destST", b);
                    intent.putExtra("destExit", exit);
                    startActivity(intent);
                }
            }
        });
    }
}
