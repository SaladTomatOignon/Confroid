package fr.uge.confroid.storage;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ConfroidPackageDao {
    @Insert
    void create(ConfroidPackage confroidPackage);

    @Update
    void update(ConfroidPackage confroidPackage);

    @Delete
    void delete(ConfroidPackage confroidPackage);

    @Query("UPDATE packages SET tag = NULL WHERE name LIKE :name AND tag LIKE :tag")
    void removeTag(String name, String tag);

    @Query("SELECT * FROM packages WHERE name LIKE :name AND tag LIKE :tag LIMIT 1")
    ConfroidPackage findByTag(String name, String tag);

    @Query("SELECT * FROM packages WHERE name LIKE :name AND version LIKE :version LIMIT 1")
    ConfroidPackage findByVersion(String name, int version);

    @Query("SELECT * FROM packages WHERE name LIKE :name ORDER BY version DESC LIMIT 1")
    ConfroidPackage findLastVersion(String name);

    @Query("SELECT * FROM packages WHERE name LIKE :name ORDER BY version")
    List<ConfroidPackage> findAllVersions(String name);

    @Query("SELECT DISTINCT name FROM packages")
    List<String> findAllNames();

    @Query("SELECT * FROM packages WHERE name LIKE :name ORDER BY version DESC LIMIT 1")
    LiveData<ConfroidPackage> findLastVersionChanges(String name);
}
