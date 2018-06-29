package com.alc.diarymohamed.data;

import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by mohamed on 25/06/18.
 */

public class RealmMigrations implements RealmMigration {


    @Override
    public void migrate(DynamicRealm dynamicRealm, long oldVersion, long newVersion) {
        final RealmSchema schema = dynamicRealm.getSchema();

        if (oldVersion == 1) {

            Log.i("RealmMigrations","oldversion==1");
        }

    }
}
