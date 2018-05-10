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
    int a;
    int b;
    private StationDB stationDB;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_screen);

        stationDB = StationDB.getInstance(DestinationScreen.this);

        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        confirmButton = findViewById(R.id.confirmButton);

        Intent intent = getIntent();
        a = intent.getIntExtra(STATION, 1);
        origin = stationDB.getStationDao().findStationById(a);
        from.setText(origin);


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dest =  to.getText().toString();
                if (stationDB.getStreetDao().getStreetByName(dest).isEmpty()) {
                    Toast.makeText(DestinationScreen.this, "No such Street!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    b = stationDB.getStationStreetDao().getStationForStreet(stationDB.getStreetDao().getStreetId(dest)).id;
                    Intent intent = new Intent(DestinationScreen.this, NavigateActivity.class);
                    intent.putExtra(STATION, a);
                    intent.putExtra("destST", b);
                    intent.putExtra("destExit", stationDB.getStreetDao().getExitIDByName(dest));
                    startActivity(intent);
                }
            }
        });
    }
}
