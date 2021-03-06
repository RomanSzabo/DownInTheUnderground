package at.ac.univie.hci.downintheunderground.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ExitDao {
    @Insert
    void insert (Exit... exits);
    @Update
    void update (Exit... exits);
    @Delete
    void delete (Exit... exits);

    @Query("SELECT exitName FROM exit WHERE exitID=:exitID AND stationID=:stationID")
    String getExitByID(final int exitID, final int stationID);

    @Query("SELECT exitID FROM exit WHERE stationID=:stationID AND exitName=:exitName")
    int getExitID(final int stationID, final String exitName);

    @Query("SELECT elevator FROM exit WHERE exitID=:ID AND stationID=:stationID")
    boolean getElevatorInfo(final int ID, final int stationID);

    @Query("SELECT level FROM exit WHERE exitID=:ID AND stationID=:stationID")
    int getLevel(final int ID, final int stationID);

    @Query("SELECT train FROM exit WHERE exitID=:ID AND stationID=:stationID")
    String getTrain(int ID, int stationID);

    @Query("SELECT * FROM exit")
    List<Exit> getExits();
}
