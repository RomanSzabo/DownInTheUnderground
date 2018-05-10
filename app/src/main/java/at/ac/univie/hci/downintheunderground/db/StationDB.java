package at.ac.univie.hci.downintheunderground.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.telecom.Call;

import java.util.concurrent.Executors;

@Database(entities = {Station.class, Exit.class, Street.class, StationStreetRelation.class}, version = 4)
public abstract class StationDB extends RoomDatabase{

    private static StationDB INSTANCE;
    private static final String DB_NAME = "station.db";
    public abstract StationDao getStationDao();
    public abstract ExitDao getExitDao();
    public abstract StreetDao getStreetDao();
    public abstract StationStreetRelationDao getStationStreetDao();

    public static StationDB getInstance (Context context) {
        if (INSTANCE == null) {
            INSTANCE = create(context);
        }
        return INSTANCE;
    }



    private static StationDB create (final Context context) {
         INSTANCE = Room.databaseBuilder(context, StationDB.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        DatabaseInitializer.populateAsync(INSTANCE);
                    }
                })
                .build();
        return INSTANCE;
    }



}
