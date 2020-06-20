package com.hooply;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MyDao
{
    @Insert
    public void addUser(User user);

    @Delete
    public void deleteUser(User user);

    @Query("SELECT * FROM user WHERE userid IN (:id)")
    List<User> userIdExists(int id);

}