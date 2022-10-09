package com.hellomet.user.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities =  {MedicineEntityRoom.Medicine.class} , version = 1 , exportSchema = false)
public abstract class MedicineDatabase extends RoomDatabase {
    private static MedicineDatabase instance;

    public abstract MedicineDao medicineDao();

    public static synchronized MedicineDatabase getInstance(Context context) {
        //MedicineDatabase medicineDatabase;
        //Class<MedicineDatabase> cls = MedicineDatabase.class;
        //synchronized (cls) {
            if (instance == null) {
                return instance = Room.databaseBuilder(context, MedicineDatabase.class, "cart_medicine_database").allowMainThreadQueries().build();
            }
            //medicineDatabase = instance;
        //}
        else {
                return instance;
            }

    }
}
