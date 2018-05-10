package at.ac.univie.hci.downintheunderground.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface StationStreetRelationDao {
    @Insert
    void insert(StationStreetRelation... stationStreetRelation);

    @Query("SELECT * FROM station INNER JOIN station_street_relation ON station.id=station_street_relation.stationID WHERE station_street_relation.streetID=:id ")
    Station getStationForStreet (final int id);

    @Query("SELECT * FROM street INNER JOIN station_street_relation ON street.id=station_street_relation.streetID WHERE station_street_relation.stationID=:id")
    List<Street> getStreetForStation (final  int id);

    @Query("SELECT exitID FROM street INNER JOIN station_street_relation ON street.id=station_street_relation.streetID WHERE station_street_relation.streetID=:id")
    int getExitID(final int id);
}
