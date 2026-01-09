package ma.ensa.mobile.studentmanagement;

import android.app.Application;

import ma.ensa.mobile.studentmanagement.data.local.database.AppDatabase;

public class StudentManagementApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize database (this will trigger the DatabaseCallback.onCreate)
        AppDatabase.getInstance(this);
    }
}