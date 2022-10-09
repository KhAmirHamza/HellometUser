package com.hellomet.user.RoomDbMVVM;

import androidx.lifecycle.LiveData;
import java.util.List;

public interface NoteDao {
    void delete(Note note);

    void deleteAllNotes();

    LiveData<List<Note>> getAllNotes();

    void insert(Note note);

    void update(Note note);
}
