package at.ac.univie.hci.downintheunderground.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface StreetDao {

    @Insert
    void insert(Street... streets);
    @Delete
    void delete(Street... streets);
    @Update
    void update(Street... streets);

    @Query("SELECT street FROM street WHERE street=:street")
    String getStreetByName(final String street);

    @Query("SELECT id FROM street WHERE street=:street")
    int getStreetId(final String street);

    @Query("SELECT exitID FROM street WHERE street=:street")
    int getExitIDByName(final String street);

    @Query("SELECT exitID FROM street WHERE id=:id")
    int getExitIDByID(final int id);

    @Query("SELECT * FROM street")
    List<Street> getAllStreets();

}
