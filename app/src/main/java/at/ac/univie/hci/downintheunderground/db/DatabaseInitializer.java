package at.ac.univie.hci.downintheunderground.db;

import android.os.AsyncTask;

import io.reactivex.annotations.NonNull;

public class DatabaseInitializer {

    public static void populateAsync(@NonNull final StationDB db) {
        PopulateDBAsync task = new PopulateDBAsync(db);
        task.execute();
    }
    //Data
    private static void populateWithData (StationDB db) {
        Exit e[] = new Exit[] {new Exit(1, 1, "Venediger Au", true, 3),
                               new Exit(1, 2, "Praterstern",true, 2),
                               new Exit(2, 3, "Novaragasse",true, 4),
                               new Exit(2, 4, "Taborstrasse",true, 4),
                               new Exit(3, 5, "Herminengasse",true, 4),
                               new Exit(3, 6, "Schottenring",true, 4),
                               new Exit(4, 7, "Hohenstraufengasse",false, 2),
                               new Exit(4, 8, "Universitaetsring(Universitaet)",true, 1),
                               new Exit(5, 9, "Florianigasse",false, 1),
                               new Exit(5, 10,"Josefstaedter Strasse, Landesgerichtsstrasse",true, 1),
                               new Exit(5, 11,"Friedrich-Schmidt-Platz, Rathaus",false, 1),
                               new Exit(5, 12,"Josefstaedter Strasse, Stadiongasse",true, 1),
        };
        Station s[] = new Station[] { new Station(1, "Praterstern"), new Station(2, "Taborstrasse"),
                                      new Station(3, "Schottenring"), new Station(4, "Schottentor"),
                                      new Station(5, "Rathaus")};
        Street st[] = new Street[] { new Street(3,"Waehringer Strasse", 8), new Street (2,"Tupengasse", 9),
                                     new Street(1,"Josefstaedter Strasse", 10), new Street (4,"Maria-Theresien Strasse", 7),
                                     new Street(5,"Franz-Josfs-Kai", 6), new Street (6, "Nickelgasse", 5),
                                     new Street(7,"Taborstrasse", 4), new Street (8,"Glockengasse", 3),
                                     new Street(9,"Prater", 1), new Street (10,"Holzhausergasse", 2)};
        StationStreetRelation ssr[] = new StationStreetRelation[] { new StationStreetRelation(5, 2),
                                    new StationStreetRelation(1, 10),new StationStreetRelation(1, 9),
                                    new StationStreetRelation(2, 8),new StationStreetRelation(2, 7),
                                    new StationStreetRelation(3, 5),new StationStreetRelation(3, 6),
                                    new StationStreetRelation(4, 3),new StationStreetRelation(4, 4),
                                    new StationStreetRelation(5, 1)  };

        db.getStationDao().insert(s);
        db.getExitDao().insert(e);
        db.getStreetDao().insert(st);
        db.getStationStreetDao().insert(ssr);
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
