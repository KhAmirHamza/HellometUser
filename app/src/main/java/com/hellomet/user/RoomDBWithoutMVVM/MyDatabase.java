package com.hellomet.user.RoomDBWithoutMVVM;

import androidx.room.RoomDatabase;

public abstract class MyDatabase extends RoomDatabase {
    public abstract DAO dao();
}
