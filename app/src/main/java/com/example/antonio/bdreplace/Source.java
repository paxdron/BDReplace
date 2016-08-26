package com.example.antonio.bdreplace;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Antonio on 25/06/2016.
 * Clase que se encarga de manejar las inserciones, actualizaciones, eliminaciones de los campos en la BD
 */
public class Source {
    public static final String TABLA_DEVICES = "devices";
    public static final String TABLA_BOMBAS = "bombas";
    public static final String TABLA_EVENTOS = "eventos";
    public static final String TABLA_EVENTOS_OBS = "eventosOBS";

    public static class Devices {
        public static final String ID_DEV = "id";
        public static final String NAME_DEV = "nombre";
        public static final String MAC_DEV = "mac";
        public static final String LAST_UPDATE = "ultimaAct";
        public static final String NUMERO_BOMBAS = "numeroBombas";
    }

    public static class Bombas {
        public static final String ID_BOM = "id";
        public static final String ID_DEV_BOMB = "idDev";
        public static final String NUMERO_BOMBA = "numeroBomba";
        public static final String NOMBRE_BOMBA = "nombreBomba";
        public static final String GASTO = "gasto";
        public static final String POTENCIA = "potencia";
        public static final String HABITANTES = "habitantes";
        public static final String CONSUMO = "consumo";
        public static final String TOPERACION = "tiempoOperancion";
    }

    public static class Eventos {
        public static final String ID_EVENTS = "id";
        public static final String NUM_BOM = "numeroBomba";
        public static final String INICIO = "inicio";
        public static final String FIN = "fin";
        public static final String QUERY = "query";
        public static final String FUGA = "fuga";
        public static final String ID_DEV_EVENTS = "idDev";
    }

    public static final String sqlCreateDEVS = "CREATE TABLE " + TABLA_DEVICES + " ( " +
            Devices.ID_DEV + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            Devices.MAC_DEV + " TEXT UNIQUE NOT NULL, " +
            Devices.NAME_DEV + " TEXT NOT NULL, " +
            Devices.LAST_UPDATE + " DATETIME, " +
            Devices.NUMERO_BOMBAS + " INTEGER " +
            " )";


    public static final String sqlCreateBOMBS = "CREATE TABLE " + TABLA_BOMBAS + " ( " +
            Bombas.ID_BOM + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            Bombas.ID_DEV_BOMB + " INTEGER NOT NULL, " +
            Bombas.NUMERO_BOMBA + " INTEGER NOT NULL, " +
            Bombas.NOMBRE_BOMBA + " TEXT, " +
            Bombas.GASTO + " REAL, " +
            Bombas.POTENCIA + " REAL, " +
            Bombas.HABITANTES + " INTEGER, " +
            Bombas.CONSUMO + " INTEGER, " +
            Bombas.TOPERACION + " INTEGER " +
            " )";


    public static final String sqlCreateEvents = "CREATE TABLE " + TABLA_EVENTOS + " ( " +
            Eventos.ID_EVENTS + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            Eventos.QUERY + " TEXT UNIQUE NOT NULL, " +
            Eventos.NUM_BOM + " INTEGER NOT NULL, " +
            Eventos.INICIO + " DATETIME NOT NULL, " +
            Eventos.FIN + " DATETIME NOT NULL, " +
            Eventos.FUGA + " INTEGER NOT NULL, " +
            Eventos.ID_DEV_EVENTS + " INTEGER NOT NULL " +
            " )";
    public static final String sqlCreateEventsv2 = "CREATE TABLE " + TABLA_EVENTOS + " ( " +
            Eventos.ID_EVENTS + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            Eventos.QUERY + " TEXT NOT NULL, " +
            Eventos.NUM_BOM + " INTEGER NOT NULL, " +
            Eventos.INICIO + " DATETIME NOT NULL, " +
            Eventos.FIN + " DATETIME NOT NULL, " +
            Eventos.FUGA + " INTEGER NOT NULL, " +
            Eventos.ID_DEV_EVENTS + " INTEGER NOT NULL " +
            " )";

    public static final String INSERT_DEVICE_SCRIPT =
            "insert into " + TABLA_DEVICES + " values( " +
                    " null," + "\"deafult\"," + "\"00:00:00:00:00\")";
    private BD openHelper;
    private SQLiteDatabase database;

    public static final String DATABASE_ALTER_1 = "ALTER TABLE "
            + TABLA_DEVICES + " ADD COLUMN " + Devices.NUMERO_BOMBAS + " INTEGER;";

    public static final String DATABASE_ALTER_2 = "ALTER TABLE "+ TABLA_EVENTOS +" RENAME TO " + TABLA_EVENTOS_OBS;
    public static final String COPY_TABLE_EVENTS ="INSERT INTO "+TABLA_EVENTOS+" SELECT * FROM "+TABLA_EVENTOS_OBS;


    public static final String FORMAT_DB="yyyy-MM-dd HH:mm";
    public static SimpleDateFormat date_format_db = new SimpleDateFormat(FORMAT_DB);

    public Source(Context context) {
        //Creando una instancia hacia la base de datos
        openHelper = new BD(context);
        database = openHelper.getWritableDatabase();
    }

    public void AddNewEvent(String Query, int IDdev){
        //Nuestro contenedor de valores

        if(!existeEvento(Query,IDdev)) {
            ContentValues values = new ContentValues();
            //Seteando body y author
            values.put(Eventos.ID_DEV_EVENTS, IDdev);
            values.put(Eventos.QUERY, Query);
            values.put(Eventos.NUM_BOM, 1);
            values.put(Eventos.INICIO, date_format_db.format(new Date()));
            values.put(Eventos.FIN, date_format_db.format(new Date()));
            values.put(Eventos.FUGA, false);


            //Insertando en la base de datos
            database.insert(TABLA_EVENTOS, null, values);
        }
    }


    private boolean existeEvento(String Query, int IDdev){
        String QUERY="SELECT "+Eventos.ID_EVENTS+" FROM "+TABLA_EVENTOS+" WHERE "+Eventos.QUERY+" ='"+Query+"' AND "+Eventos.ID_DEV_EVENTS+" = "+IDdev;
        return database.rawQuery(QUERY,null).moveToFirst();
    }
    /**
     * Obtiene todos los eventos relacionados al dispositivo indicado
     * @return La lista con los eventos
     */
    public Cursor GetAllEvents(){
        String Query="SELECT * FROM "+TABLA_EVENTOS;
        return database.rawQuery(Query,null);
    }

}
