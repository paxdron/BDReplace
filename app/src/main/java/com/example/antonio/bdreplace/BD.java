package com.example.antonio.bdreplace;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Antonio on 24/06/2016.
 * Clase que genera y modifica la base de datos
 */
public class BD extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Quotes.db";
    public static final int DATABASE_VERSION = 3;

    public BD(Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Source.sqlCreateBOMBS);
        db.execSQL(Source.sqlCreateDEVS);
        db.execSQL(Source.sqlCreateEvents);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL(Source.DATABASE_ALTER_1);
        }
        if(oldVersion<3){
            db.execSQL(Source.DATABASE_ALTER_2);
            db.execSQL(Source.sqlCreateEventsv2);
            db.execSQL(Source.COPY_TABLE_EVENTS);
        }

    }
}
