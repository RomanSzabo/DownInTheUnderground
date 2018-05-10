package at.ac.univie.hci.downintheunderground;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.concurrent.Executors;

import at.ac.univie.hci.downintheunderground.db.DatabaseInitializer;
import at.ac.univie.hci.downintheunderground.db.Exit;
import at.ac.univie.hci.downintheunderground.db.ExitDao;
import at.ac.univie.hci.downintheunderground.db.StationDB;

public class QRStartScreen extends AppCompatActivity {

    SurfaceView camera;
    TextView result;
    BarcodeDetector qrcode;
    CameraSource cameraSource;
    Button confirmButton;
    String qrResult;
    String st;
    String exit;
    final int RequestCameraPermissionID  = 1001;
    public static final String STATION = "Station";
    private StationDB stationDB;
    int stid;
    int eeid;

    private void set(String s, String e) {
        st = s;
        exit = e;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case RequestCameraPermissionID:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(camera.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrstart_screen);

        camera = (SurfaceView)findViewById(R.id.QRCamera);
        result = (TextView)findViewById(R.id.QRResult);
        confirmButton = (Button)findViewById(R.id.ConfirmQR);
        stationDB =  StationDB.getInstance(QRStartScreen.this);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exit == null) {
                    Toast.makeText(QRStartScreen.this, "No such Station!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    Intent intent = new Intent(QRStartScreen.this, DestinationScreen.class);
                    intent.putExtra(STATION, stid);
                    startActivity(intent);
                }
            }
        });
        qrcode = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        cameraSource = new CameraSource
                .Builder(this, qrcode)
                .setRequestedPreviewSize(640,480)
                .setAutoFocusEnabled(true)
                .build();

        //Event
        camera.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //Request Camera
                    ActivityCompat.requestPermissions(QRStartScreen.this, new String[] {Manifest.permission.CAMERA}, RequestCameraPermissionID);
                    return;
                }

                try {
                    cameraSource.start(camera.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        //Event
        qrcode.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> codes = detections.getDetectedItems();
                if (codes.size() != 0) {
                    result.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            qrResult = codes.valueAt(0).displayValue;
                            try {
                                String[] id = qrResult.split(",");
                                final int sid = Integer.parseInt(id[0]);
                                stid = sid;
                                final int eid = Integer.parseInt(id[1]);
                                eeid = eid;
                            }
                            catch (Exception e) {
                                Toast.makeText(QRStartScreen.this, "Can't find Station / Invalid QR Code", Toast.LENGTH_SHORT).show();
                                Log.e("QR_CODE_ERROR", e.getMessage());
                            }
                            Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    String e = (stationDB.getExitDao().getExitByID(eeid, stid));
                                    String s = (stationDB.getStationDao().findStationById(stid));
                                    set(s, e);
                                }
                            });
                            String res;
                            if (st == null || exit == null) {
                                res = "";
                            }
                            else {
                                res = "Station: " + st + " - " + exit;
                            }
                            result.setText(res);
                        }
                    });
                }
            }
        });
    }
}
