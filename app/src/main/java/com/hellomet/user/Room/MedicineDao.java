package com.hellomet.user.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import java.util.List;

@Dao
public interface MedicineDao {
    @Query("Delete from Medicine where medicineId = :medicineId")
    void deleteMedicine(String medicineId);

    @Query("Select * from Medicine")
    List<MedicineEntityRoom.Medicine> getAllMedicine();

    @Query("Select * from Medicine")
    LiveData<List<MedicineEntityRoom.Medicine>> getAllMedicineLiveData();

    @Insert
    void insertMedicine(MedicineEntityRoom.Medicine medicine);
}
