package be.ap.edu.studies_osm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by keithvella on 06/10/2017.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "test.db";
    private static final String TABLE_POINTERS = "pointers";
    private static final int DATABASE_VERSION = 5;

    public MySQLiteHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Android expects _id to be the primary key
        String CREATE_POINTERS_TABLE = "CREATE TABLE " + TABLE_POINTERS + "(_id INTEGER PRIMARY KEY, latitude REAL, longitude REAL, description TEXT)";
        db.execSQL(CREATE_POINTERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POINTERS);
        onCreate(db);
    }

    public void addPointer(GeoPoint geo, String descr) {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.delete(TABLE_POINTERS, null, null);

        ContentValues values = new ContentValues();
        values.put("latitude", geo.getLatitude());
        values.put("longitude", geo.getLongitude());
        values.put("description", descr);


        db.insert(TABLE_POINTERS, null, values);
        //db.close();
    }

    public void deletePointers(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM POINTERS"); //delete all rows in a table
        db.close();
    }

    public String getPointers(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        /**String tblName 		The table name to compile the query against.
         String[] columnNames 	A list of which table columns to return. Passing "null" will return all columns.
         String whereClause 		Where-clause, i.e. filter for the selection of data, null will select all data.
         String[] selectionArgs 	You may include ?s in the "whereClause"". These placeholders will get replaced by the values from the selectionArgs array.
         String[] groupBy 		A filter declaring how to group rows, null will cause the rows to not be grouped.
         String[] having 			Filter for the groups, null means no filter.
         String[] orderBy 		Table columns which will be used to order the data, null means no ordering.**/

        Cursor cursor = db.query(TABLE_POINTERS,
                new String[] { "_id", "latitude", "longitude", "description" },
                "_id=?",
                new String[] { String.valueOf(id) },
                null, null, null, null);

        if(cursor != null)
            cursor.moveToFirst();

        return cursor.getString(1) + " " + cursor.getString(2);
    }

    public String getTableAsString() {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("SQLITE", "getTableAsString called");
        String tableString = String.format("Table %s:\n", TABLE_POINTERS);
        Cursor allRows  = db.rawQuery("SELECT * FROM " + TABLE_POINTERS, null);
        System.out.println(allRows.getCount());
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }

        allRows.close();

        return tableString;
    }

    public List<GeoPoint> getLocations() {
        SQLiteDatabase db = this.getReadableDatabase();

        /**String tblName       The table name to compile the query against.
         String[] columnNames   A list of which table columns to return. Passing "null" will return all columns.
         String whereClause         Where-clause, i.e. filter for the selection of data, null will select all data.
         String[] selectionArgs     You may include ?s in the "whereClause"". These placeholders will get replaced by the values from the selectionArgs array.
         String[] groupBy       A filter declaring how to group rows, null will cause the rows to not be grouped.
         String[] having            Filter for the groups, null means no filter.
         String[] orderBy       Table columns which will be used to order the data, null means no ordering.**/

        Cursor cursor  = db.rawQuery("SELECT * FROM " + TABLE_POINTERS, null);

        List<GeoPoint> geoPointList = new ArrayList<>();

        System.out.println(cursor.getCount());

        while (cursor.moveToNext()) {
            geoPointList.add(new GeoPoint(cursor.getDouble(1), cursor.getDouble(2)));
            //System.out.println(cursor.getDouble(1));
            //System.out.println(cursor.getDouble(2));
        }

        cursor.close();

        System.out.println("AAAAAAAAAAAAAAAAAA"  + geoPointList);

        return geoPointList;

    }

}
