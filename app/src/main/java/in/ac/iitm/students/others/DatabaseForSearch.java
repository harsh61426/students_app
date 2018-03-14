package in.ac.iitm.students.others;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

/**
 * Created by dell on 03-03-2018.
 */

public class DatabaseForSearch {
    private static final String TAG = "CoursesDatabase";

    //The columns we'll include in the dictionary table
    public static final String COL_COURSE_NO = "courseNumberKey";
    public static final String COL_COURSE_NAME = "courseNameKey";

    private static final String DATABASE_NAME = "coursesDatabaseDatabseName";
    private static final String FTS_VIRTUAL_TABLE = "FTS";
    private static final int DATABASE_VERSION = 1;

    private final DatabaseOpenHelper mDatabaseOpenHelper;

    public DatabaseForSearch(Context context) {
        mDatabaseOpenHelper = new DatabaseOpenHelper(context);
    }

    private static class DatabaseOpenHelper extends SQLiteOpenHelper {

        private final Context mHelperContext;
        private SQLiteDatabase mDatabase;

        private static final String FTS_TABLE_CREATE =
                "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
                        " USING fts3 (" +
                        COL_COURSE_NO + ", " +
                        COL_COURSE_NAME + ")";

        DatabaseOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mHelperContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            mDatabase = db;
            mDatabase.execSQL(FTS_TABLE_CREATE);
            addCourse("AM1100", "Engineering Mechanics");
            addCourse("AM5110", "Biomechanics theory and Lab");
            addCourse("AS2100", "Introduction to Aerospace lab");
            addCourse("AS5220", "Structural Design Aerospace Engineers");
            addCourse("BT1010", "Life Sciences in Biotechnology");
            addCourse("CE2080", "Surveying in Chemical Engineering");
            addCourse("CE2310", "Mechanics of Material");
            addCourse("CH2030", "Momentum Transfer in Chemistry");
            addCourse("CH3520", "Heat and Mass Transfer Lab");
            addCourse("CS1100", "Computational Engineering");
            addCourse("CS2800", "Data structures and Algorithm");
            addCourse("ED1100", "Functional and Conceptual Design");
            addCourse("ED1120", "Engineering Drawing basic course");
            addCourse("ED1300", "Graphic Art for Engineering Design");
            addCourse("EE1100", "Basic Electrical Engineering");
            addCourse("EE2003", "Computer Organization");
            addCourse("EE2004", "Digital Signal Processing");
            addCourse("EE2005", "Electrical Machines and components");
            addCourse("EE2015", "Electrical Circuits and Analysis");
            addCourse("ME1100", "Thermodynamics for Mechanical Engineers");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            onCreate(db);
        }

        public long addCourse(String code, String name) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(COL_COURSE_NO, code);
            initialValues.put(COL_COURSE_NAME, name);

            return mDatabase.insert(FTS_VIRTUAL_TABLE, null, initialValues);
        }
    }

    public Cursor getCourseMatches(String query, String[] columns, int sortCode) {
        String selection = COL_COURSE_NO + " MATCH ?";
        String[] selectionArgs = new String[]{query + "*"};

        return query(selection, selectionArgs, columns, sortCode);
    }

    private Cursor query(String selection, String[] selectionArgs, String[] columns, int sortCode) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(FTS_VIRTUAL_TABLE);
        Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, " " + COL_COURSE_NO + " ASC");

        if (sortCode == 1)
            cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
                    columns, selection, selectionArgs, null, null, " " + COL_COURSE_NAME + " ASC");


        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }
}
