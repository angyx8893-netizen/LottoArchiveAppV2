package com.example.lottoarchive.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DrawDao {
    @Query("SELECT * FROM draws ORDER BY date DESC, wheel ASC")
    fun all(): Flow<List<Draw>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(draws: List<Draw>)

    @Query("DELETE FROM draws")
    suspend fun clear()

    @Query("""
        SELECT * FROM draws
        WHERE (:wheel IS NULL OR wheel = :wheel)
        AND (:number IS NULL OR n1 = :number OR n2 = :number OR n3 = :number OR n4 = :number OR n5 = :number)
        ORDER BY date DESC, wheel ASC
    """)
    fun search(wheel: String?, number: Int?): Flow<List<Draw>>

    @Query("""
        SELECT COUNT(*) FROM draws
        WHERE (:wheel IS NULL OR wheel = :wheel)
        AND (n1 = :number OR n2 = :number OR n3 = :number OR n4 = :number OR n5 = :number)
    """)
    suspend fun countOccurrences(number: Int, wheel: String?): Int
}


    @Query("SELECT COUNT(*) FROM draws WHERE (:wheel IS NULL OR wheel = :wheel) AND (n1=:number OR n2=:number OR n3=:number OR n4=:number OR n5=:number)")
    suspend fun countNumber(number: Int, wheel: String?): Int

    @Query("SELECT MAX(date) FROM draws WHERE (:wheel IS NULL OR wheel = :wheel) AND (n1=:number OR n2=:number OR n3=:number OR n4=:number OR n5=:number)")
    suspend fun lastDateForNumber(number: Int, wheel: String?): String?

    @Query("SELECT MAX(date) FROM draws")
    suspend fun maxDate(): String?
