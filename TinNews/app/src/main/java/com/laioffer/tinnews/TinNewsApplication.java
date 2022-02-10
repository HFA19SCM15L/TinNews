package com.laioffer.tinnews;

import android.app.Application;

import androidx.room.Room;

import com.ashokvarma.gander.Gander;
import com.ashokvarma.gander.imdb.GanderIMDB;
import com.facebook.stetho.Stetho;
import com.laioffer.tinnews.database.AppDatabase;

// AndroidManifest.xml: android:name=".TinNewsApplication"
public class TinNewsApplication extends Application {
    // singleton
    private static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        // for debug
        Gander.setGanderStorage(GanderIMDB.getInstance());
        Stetho.initializeWithDefaults(this);
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tin_db").build();
    }

    public static AppDatabase getDatabase() {
        return database;
    }
}