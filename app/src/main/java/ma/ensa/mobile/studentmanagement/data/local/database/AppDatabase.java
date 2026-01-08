package ma.ensa.mobile.studentmanagement.data.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ma.ensa.mobile.studentmanagement.data.local.dao.RoleDao;
import ma.ensa.mobile.studentmanagement.data.local.dao.UserDao;
import ma.ensa.mobile.studentmanagement.data.local.entity.Role;
import ma.ensa.mobile.studentmanagement.data.local.entity.User;

@Database(
        entities = {Role.class, User.class},
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    // DAOs abstraits
    public abstract RoleDao roleDao();
    public abstract UserDao userDao();

    // Instance Singleton
    private static volatile AppDatabase INSTANCE;

    // ExecutorService pour opérations asynchrones
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Méthode Singleton pour obtenir l'instance de la base de données
     */
    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "esms_database"
                            )
                            .addCallback(new DatabaseCallback())
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}