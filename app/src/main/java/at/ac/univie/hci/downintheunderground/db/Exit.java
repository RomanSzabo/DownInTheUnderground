package at.ac.univie.hci.downintheunderground.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import at.ac.univie.hci.downintheunderground.db.Station;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "exit", foreignKeys = @ForeignKey(entity = Station.class, parentColumns = "id", childColumns = "stationID", onDelete = CASCADE))
public class Exit {
    @PrimaryKey(autoGenerate = false)
    public final int exitID;

    @ColumnInfo(name = "stationID")
    public final int stationID;

    @ColumnInfo(name = "exitName")
    public final String exitName;

    @ColumnInfo(name = "elevator")
    public final boolean elevator;

    @ColumnInfo(name = "level")
    public final int level;

    @ColumnInfo(name = "train")
    public final String train;

    public Exit (int stationID, int exitID, String exitName, boolean elevator, int level, String train) {
        this.stationID = stationID;
        this.exitID = exitID;
        this.exitName = exitName;
        this.elevator = elevator;
        this.level = level;
        this.train = train;
    }


}
