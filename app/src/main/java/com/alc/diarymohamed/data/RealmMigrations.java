package com.alc.diarymohamed.data;

import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by mohamed on 17/02/18.
 */

public class RealmMigrations implements RealmMigration {


    @Override
    public void migrate(DynamicRealm dynamicRealm, long oldVersion, long newVersion) {
        final RealmSchema schema = dynamicRealm.getSchema();

        if (oldVersion == 3) {

            Log.i("RealmMigrations","oldversion==3");
        }

    }
}