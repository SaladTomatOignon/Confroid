package fr.uge.confroid.storage;

import java.util.Date;

import androidx.room.TypeConverter;
import fr.uge.confroid.configuration.Configuration;

public class ConfroidConverters {
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Date dateFromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Configuration configFromString(String value) {
        return value == null ? null : Configuration.fromJson(value);
    }

    @TypeConverter
    public static String configToString(Configuration configuration) {
        return configuration == null ? null : configuration.toJson();
    }
}
