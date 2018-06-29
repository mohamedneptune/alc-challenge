package com.alc.diarymohamed;

import android.app.Application;
import android.content.Context;

import com.alc.diarymohamed.data.RealmMigrations;
import com.alc.diarymohamed.shared.Config;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by mohamed on 26/06/18.
 */


public class DiaryApplication extends Application {


    private static Context GLOBAL_APP_CONTEXT;

    public static Context getGlobalAppContext() {
        return GLOBAL_APP_CONTEXT;
    }

    public static void setGlobalAppContext(Context globalAppContext) {
        GLOBAL_APP_CONTEXT = globalAppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        //Stetho
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider
                                .builder(this).build())
                        .build());

        final RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("default2.realm").schemaVersion(Config.SCHEMA_VERSION)
                .migration(new RealmMigrations()).build();
        Realm.setDefaultConfiguration(configuration);
        Realm.getInstance(configuration);
    }

    @Override
    public void onTerminate() {
        Realm.getDefaultInstance().close();
        super.onTerminate();
    }

}
