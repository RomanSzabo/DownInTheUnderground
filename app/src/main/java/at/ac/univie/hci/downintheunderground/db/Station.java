package at.ac.univie.hci.downintheunderground.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "station")
public class Station {

    @PrimaryKey(autoGenerate = false)
    public final int id;

    @ColumnInfo(name = "name")
    public final String name;

    public Station (int id, String name) {
        this.id = id;
        this.name = name;
    }


}
