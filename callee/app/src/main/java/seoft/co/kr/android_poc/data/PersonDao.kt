package seoft.co.kr.android_poc.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import android.database.Cursor

@Dao
interface PersonDao{

    @Query("SELECT COUNT(*) FROM " + Person.TABLE_NAME)
    fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(person: Person): Long

    @Insert
    fun insertAll(persons: Array<Person>): LongArray

    @Query("SELECT * FROM " + Person.TABLE_NAME)
    fun selectAll(): Cursor

    @Query("SELECT * FROM " + Person.TABLE_NAME + " WHERE " + Person.COLUMN_ID + " = :id")
    fun selectById(id: Long): Cursor

    @Query("DELETE FROM " + Person.TABLE_NAME + " WHERE " + Person.COLUMN_ID + " = :id")
    fun deleteById(id: Long): Int

    @Update
    fun update(person: Person): Int

}