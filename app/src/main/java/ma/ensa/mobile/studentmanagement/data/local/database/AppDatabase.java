package ma.ensa.mobile.studentmanagement.data.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ma.ensa.mobile.studentmanagement.data.local.dao.BranchDao;
import ma.ensa.mobile.studentmanagement.data.local.dao.LevelDao;
import ma.ensa.mobile.studentmanagement.data.local.dao.RoleDao;
import ma.ensa.mobile.studentmanagement.data.local.dao.StudentDao;
import ma.ensa.mobile.studentmanagement.data.local.dao.UserDao;
import ma.ensa.mobile.studentmanagement.data.local.entity.Branch;
import ma.ensa.mobile.studentmanagement.data.local.entity.Level;
import ma.ensa.mobile.studentmanagement.data.local.entity.Role;
import ma.ensa.mobile.studentmanagement.data.local.entity.Student;
import ma.ensa.mobile.studentmanagement.data.local.entity.User;

@Database(
        entities = {Role.class, User.class, Student.class, Branch.class, Level.class},
        version = 3,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    // DAOs abstraits
    public abstract RoleDao roleDao();
    public abstract UserDao userDao();
    public abstract StudentDao studentDao();
    public abstract BranchDao branchDao();
    public abstract LevelDao levelDao();

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
                            .fallbackToDestructiveMigration() // Pour le développement
                            .addCallback(new DatabaseCallback())
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}