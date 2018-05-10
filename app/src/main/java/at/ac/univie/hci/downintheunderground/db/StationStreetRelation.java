package at.ac.univie.hci.downintheunderground.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

@Entity(tableName = "station_street_relation", primaryKeys = {"stationID", "streetID"}, foreignKeys = {
        @ForeignKey(entity = Station.class, parentColumns = "id", childColumns = "stationID"),
        @ForeignKey(entity = Street.class, parentColumns = "id", childColumns = "streetID")})
public class StationStreetRelation {

    public final int stationID;
    public final int streetID;

    public StationStreetRelation(final int stationID, final int streetID) {
        this.stationID = stationID;
        this.streetID = streetID;
    }


}
