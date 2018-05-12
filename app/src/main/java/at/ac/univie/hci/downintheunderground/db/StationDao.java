package at.ac.univie.hci.downintheunderground.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface StationDao {
    @Insert
    void insert (Station... stations);
    @Update
    void update (Station... stations);
    @Delete
    void delete (Station... stations);

    @Query("SELECT name FROM station WHERE name=:name")
    String findStationByName(final String name);

    @Query("SELECT name FROM station WHERE id=:id")
    String findStationById(final int id);

    @Query("SELECT id FROM station WHERE name=:name")
    int findIDByName(final String name);

    @Query("SELECT * FROM station")
    List<Station> getAllStations();
}
