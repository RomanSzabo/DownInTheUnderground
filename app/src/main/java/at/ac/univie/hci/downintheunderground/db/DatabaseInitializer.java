package at.ac.univie.hci.downintheunderground.db;

import android.os.AsyncTask;

import io.reactivex.annotations.NonNull;

public class DatabaseInitializer {

    public static void populateAsync(@NonNull final StationDB db) {
        PopulateDBAsync task = new PopulateDBAsync(db);
        task.execute();
    }

    private static void populateWithData (StationDB db) {
        Exit e[] = new Exit[] {new Exit(1, 1, "Venediger Au", true, -1)};
        Station s[] = new Station[] { new Station(1, "Praterstern"), new Station(2, "Taborstrasse"),
                                      new Station(3, "Schottenring"), new Station(4, "Schottentor"),
                                      new Station(5, "Rathaus")};
        Street st = new Street ("UniRing", 1);
        StationStreetRelation ssr = new StationStreetRelation(5, 1);
        db.getExitDao().insert(e);
        db.getStationStreetDao().insert(ssr);
        db.getStreetDao().insert(st);
        db.getStationDao().insert(s);
    }


    private static class PopulateDBAsync extends AsyncTask<Void, Void, Void> {
        private final StationDB popdb;

        PopulateDBAsync(StationDB db) {
            popdb = db;
        }

        @Override
        protected Void doInBackground(final Void... voids) {
            populateWithData(popdb);
            return null;
        }
    }

}
