package com.dorbrauner.nutshellfirebase.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dorbrauner.nutshellfirebase.database.model.NotificationMessage
import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract.Sources.PersistentSource.Companion.ROOM_TABLE_NOTIFICATION_MESSAGE
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface NotificationMessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(notificationMessage: NotificationMessage): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(notificationMessages: List<NotificationMessage>): Completable

    @Query("SELECT * FROM $ROOM_TABLE_NOTIFICATION_MESSAGE")
    fun getAll(): Single<List<NotificationMessage>>

    @Query("SELECT * FROM $ROOM_TABLE_NOTIFICATION_MESSAGE WHERE action_id == :id")
    fun getById(id: String): Single<NotificationMessage>

    @Query("DELETE FROM $ROOM_TABLE_NOTIFICATION_MESSAGE")
    fun deleteAll(): Completable

    @Query("DELETE FROM $ROOM_TABLE_NOTIFICATION_MESSAGE WHERE action_id == :id")
    fun deleteByActionId(id: String): Completable

    @Query("DELETE FROM $ROOM_TABLE_NOTIFICATION_MESSAGE WHERE action_id in (:list)")
    fun deleteGroup(list: List<String>): Completable
}