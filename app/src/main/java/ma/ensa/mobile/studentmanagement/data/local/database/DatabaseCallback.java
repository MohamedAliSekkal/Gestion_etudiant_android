package ma.ensa.mobile.studentmanagement.data.local.database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.mindrot.jbcrypt.BCrypt;

import ma.ensa.mobile.studentmanagement.data.local.entity.Role;
import ma.ensa.mobile.studentmanagement.data.local.entity.User;

public class DatabaseCallback extends RoomDatabase.Callback {

    private static final String TAG = "DatabaseCallback";

    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
        super.onCreate(db);
        Log.d(TAG, "Database created, populating initial data...");

        // Exécuter l'initialisation dans un thread séparé
        AppDatabase.databaseWriteExecutor.execute(() -> {
            // Obtenir l'instance de la base de données n'est pas possible ici
            // On utilise donc des requêtes SQL directes

            // Insérer les rôles par défaut
            db.execSQL("INSERT INTO roles (role_code, role_name, role_name_en, role_name_fr, description, priority_level, is_active, created_at, updated_at) VALUES " +
                    "('ADMIN', 'Administrateur', 'Administrator', 'Administrateur', 'Administration système complète', 1, 1, strftime('%s', 'now'), strftime('%s', 'now'))");

            db.execSQL("INSERT INTO roles (role_code, role_name, role_name_en, role_name_fr, description, priority_level, is_active, created_at, updated_at) VALUES " +
                    "('SCOLARITE', 'Service Scolarité', 'Registry Service', 'Service Scolarité', 'Gestion administrative', 2, 1, strftime('%s', 'now'), strftime('%s', 'now'))");

            db.execSQL("INSERT INTO roles (role_code, role_name, role_name_en, role_name_fr, description, priority_level, is_active, created_at, updated_at) VALUES " +
                    "('APOGEE', 'Service APOGÉE', 'APOGÉE Service', 'Service APOGÉE', 'Gestion académique', 2, 1, strftime('%s', 'now'), strftime('%s', 'now'))");

            db.execSQL("INSERT INTO roles (role_code, role_name, role_name_en, role_name_fr, description, priority_level, is_active, created_at, updated_at) VALUES " +
                    "('TEACHER', 'Enseignant', 'Teacher', 'Enseignant', 'Gestion absences', 3, 1, strftime('%s', 'now'), strftime('%s', 'now'))");

            db.execSQL("INSERT INTO roles (role_code, role_name, role_name_en, role_name_fr, description, priority_level, is_active, created_at, updated_at) VALUES " +
                    "('STUDENT', 'Étudiant', 'Student', 'Étudiant', 'Consultation profil', 4, 1, strftime('%s', 'now'), strftime('%s', 'now'))");

            // Créer un utilisateur admin par défaut
            // Mot de passe : admin123
            String hashedPassword = BCrypt.hashpw("admin123", BCrypt.gensalt());

            db.execSQL("INSERT INTO users (username, email, password_hash, role_id, full_name, phone, is_active, is_locked, failed_login_attempts, created_at, updated_at) VALUES " +
                    "('admin', 'admin@ensa.ac.ma', '" + hashedPassword + "', 1, 'Administrateur Système', '+212600000000', 1, 0, 0, strftime('%s', 'now'), strftime('%s', 'now'))");

            // Insérer les filières (Branches) - 7 filières de l'ENSA
            db.execSQL("INSERT INTO branches (branch_code, branch_name, branch_name_en, cycle, capacity, is_active, created_at, updated_at) VALUES " +
                    "('GI', 'Génie Informatique', 'Computer Engineering', 'ENGINEERING', 120, 1, strftime('%s', 'now'), strftime('%s', 'now'))");

            db.execSQL("INSERT INTO branches (branch_code, branch_name, branch_name_en, cycle, capacity, is_active, created_at, updated_at) VALUES " +
                    "('GSTR', 'Génie des Systèmes de Télécommunications et Réseaux', 'Telecommunication Systems and Networks Engineering', 'ENGINEERING', 60, 1, strftime('%s', 'now'), strftime('%s', 'now'))");

            db.execSQL("INSERT INTO branches (branch_code, branch_name, branch_name_en, cycle, capacity, is_active, created_at, updated_at) VALUES " +
                    "('ML', 'Management et Logistique', 'Management and Logistics', 'ENGINEERING', 60, 1, strftime('%s', 'now'), strftime('%s', 'now'))");

            db.execSQL("INSERT INTO branches (branch_code, branch_name, branch_name_en, cycle, capacity, is_active, created_at, updated_at) VALUES " +
                    "('GM', 'Génie Mécanique', 'Mechanical Engineering', 'ENGINEERING', 60, 1, strftime('%s', 'now'), strftime('%s', 'now'))");

            db.execSQL("INSERT INTO branches (branch_code, branch_name, branch_name_en, cycle, capacity, is_active, created_at, updated_at) VALUES " +
                    "('GC', 'Génie Civil', 'Civil Engineering', 'ENGINEERING', 60, 1, strftime('%s', 'now'), strftime('%s', 'now'))");

            db.execSQL("INSERT INTO branches (branch_code, branch_name, branch_name_en, cycle, capacity, is_active, created_at, updated_at) VALUES " +
                    "('GCSE', 'Génie Climatique et Solutions Énergétiques', 'Climate and Energy Solutions Engineering', 'ENGINEERING', 40, 1, strftime('%s', 'now'), strftime('%s', 'now'))");

            db.execSQL("INSERT INTO branches (branch_code, branch_name, branch_name_en, cycle, capacity, is_active, created_at, updated_at) VALUES " +
                    "('BDAI', 'Big Data et Intelligence Artificielle', 'Big Data and Artificial Intelligence', 'ENGINEERING', 40, 1, strftime('%s', 'now'), strftime('%s', 'now'))");

            // Insérer les niveaux (Levels) - 5 niveaux
            db.execSQL("INSERT INTO levels (level_code, level_name, level_name_en, year_number, cycle, is_active, created_at, updated_at) VALUES " +
                    "('CP1', 'Cycle Préparatoire 1ère année', 'Preparatory Cycle 1st Year', 1, 'PREPARATORY', 1, strftime('%s', 'now'), strftime('%s', 'now'))");

            db.execSQL("INSERT INTO levels (level_code, level_name, level_name_en, year_number, cycle, is_active, created_at, updated_at) VALUES " +
                    "('CP2', 'Cycle Préparatoire 2ème année', 'Preparatory Cycle 2nd Year', 2, 'PREPARATORY', 1, strftime('%s', 'now'), strftime('%s', 'now'))");

            db.execSQL("INSERT INTO levels (level_code, level_name, level_name_en, year_number, cycle, is_active, created_at, updated_at) VALUES " +
                    "('CI1', 'Cycle Ingénieur 1ère année', 'Engineering Cycle 1st Year', 3, 'ENGINEERING', 1, strftime('%s', 'now'), strftime('%s', 'now'))");

            db.execSQL("INSERT INTO levels (level_code, level_name, level_name_en, year_number, cycle, is_active, created_at, updated_at) VALUES " +
                    "('CI2', 'Cycle Ingénieur 2ème année', 'Engineering Cycle 2nd Year', 4, 'ENGINEERING', 1, strftime('%s', 'now'), strftime('%s', 'now'))");

            db.execSQL("INSERT INTO levels (level_code, level_name, level_name_en, year_number, cycle, is_active, created_at, updated_at) VALUES " +
                    "('CI3', 'Cycle Ingénieur 3ème année', 'Engineering Cycle 3rd Year', 5, 'ENGINEERING', 1, strftime('%s', 'now'), strftime('%s', 'now'))");

            Log.d(TAG, "Initial data populated successfully (Roles, Users, Branches, Levels)");
        });
    }

    @Override
    public void onOpen(@NonNull SupportSQLiteDatabase db) {
        super.onOpen(db);
        Log.d(TAG, "Database opened");
    }
}