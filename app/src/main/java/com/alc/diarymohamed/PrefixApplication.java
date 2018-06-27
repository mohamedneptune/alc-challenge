package com.alc.diarymohamed;

import android.app.Application;
import android.content.Context;

import com.alc.diarymohamed.data.RealmMigrations;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class PrefixApplication extends Application {


    private static Context GLOBAL_APP_CONTEXT;
    private final static int SCHEMA_VERSION = 1;

    public static Context getGlobalAppContext() {
        return GLOBAL_APP_CONTEXT;
    }

    public static void setGlobalAppContext(Context globalAppContext) {
        GLOBAL_APP_CONTEXT = globalAppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        final RealmConfiguration configuration = new RealmConfiguration.Builder(this).name("default2").schemaVersion(SCHEMA_VERSION).migration(new RealmMigrations()).build();
        Realm.setDefaultConfiguration(configuration);
        Realm.getInstance(configuration);
    }

}
