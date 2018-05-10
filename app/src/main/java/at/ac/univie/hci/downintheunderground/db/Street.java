package at.ac.univie.hci.downintheunderground.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "street")
public class Street {

    @PrimaryKey(autoGenerate = false)
    private int id;

    @ColumnInfo(name = "streetName")
    private String street;
    @ColumnInfo(name = "exitID")
    private int exitID;

    public Street(int id, String street, int exitID) {
        this.id = id;
        this.street = street;
        this.exitID = exitID;
    }

    public int getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public int getExitID() {
        return exitID;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setExitID(int exitID) {
        this.exitID = exitID;
    }

}
