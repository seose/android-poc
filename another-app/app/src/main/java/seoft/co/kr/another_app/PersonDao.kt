package seoft.co.kr.another_app

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Single

@Dao
interface PersonDao{

    @Query("SELECT * FROM PERSON")
    fun findPersons() : Single<List<Person>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPerson(chat : Person) : Long

}