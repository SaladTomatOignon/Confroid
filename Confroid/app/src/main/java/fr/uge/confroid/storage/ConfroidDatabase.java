package fr.uge.confroid.storage;

import android.content.Context;

import java.util.function.Consumer;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = { ConfroidPackage.class }, version = 1)
@TypeConverters({ ConfroidConverters.class })
public abstract class ConfroidDatabase extends RoomDatabase {
    public abstract ConfroidPackageDao packageDao();

    public static void exec(Context context, Consumer<ConfroidPackageDao> consumer) {
        ConfroidDatabase db = Room.databaseBuilder(
            context, ConfroidDatabase.class, "confroid.db"
        ).build();

        try {
            consumer.accept(db.packageDao());
        } finally {
            db.close();
        }
    }
}
