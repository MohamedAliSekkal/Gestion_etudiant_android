package ma.ensa.mobile.studentmanagement.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import org.mindrot.jbcrypt.BCrypt;

import ma.ensa.mobile.studentmanagement.data.local.dao.RoleDao;
import ma.ensa.mobile.studentmanagement.data.local.dao.UserDao;
import ma.ensa.mobile.studentmanagement.data.local.database.AppDatabase;
import ma.ensa.mobile.studentmanagement.data.local.entity.Role;
import ma.ensa.mobile.studentmanagement.data.local.entity.User;

public class UserRepository {

    private static final String TAG = "UserRepository";
    private static final int MAX_FAILED_ATTEMPTS = 5;

    private UserDao userDao;
    private RoleDao roleDao;

    public UserRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        userDao = database.userDao();
        roleDao = database.roleDao();
    }

    /**
     * Authentifie un utilisateur
     * @return User si authentification réussie, null sinon
     */
    public User authenticate(String username, String password) {
        try {
            User user = userDao.getUserForLogin(username);

            if (user == null) {
                Log.w(TAG, "User not found: " + username);
                return null;
            }

            if (user.isLocked()) {
                Log.w(TAG, "User account is locked: " + username);
                return null;
            }

            // Vérifier le mot de passe avec BCrypt
            boolean passwordMatch = BCrypt.checkpw(password, user.getPasswordHash());

            if (passwordMatch) {
                // Réinitialiser les tentatives échouées
                updateFailedAttempts(user.getUserId(), 0);
                updateLastLogin(user.getUserId());
                Log.i(TAG, "Authentication successful for: " + username);
                return user;
            } else {
                // Incrémenter les tentatives échouées
                int attempts = user.getFailedLoginAttempts() + 1;
                updateFailedAttempts(user.getUserId(), attempts);

                if (attempts >= MAX_FAILED_ATTEMPTS) {
                    lockUser(user.getUserId());
                    Log.w(TAG, "User locked after " + attempts + " failed attempts: " + username);
                }

                Log.w(TAG, "Authentication failed for: " + username + " (attempt " + attempts + ")");
                return null;
            }

        } catch (Exception e) {
            Log.e(TAG, "Error during authentication", e);
            return null;
        }
    }

    /**
     * Récupère le rôle d'un utilisateur
     */
    public Role getUserRole(int roleId) {
        return roleDao.getRoleById(roleId).getValue();
    }

    /**
     * Met à jour le nombre de tentatives échouées
     */
    private void updateFailedAttempts(int userId, int attempts) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userDao.updateFailedAttempts(userId, attempts);
        });
    }

    /**
     * Verrouille un compte utilisateur
     */
    private void lockUser(int userId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userDao.updateLockStatus(userId, true);
        });
    }

    /**
     * Met à jour la date de dernière connexion
     */
    private void updateLastLogin(int userId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            long timestamp = System.currentTimeMillis() / 1000;
            userDao.updateLastLogin(userId, timestamp);
        });
    }

    /**
     * Récupère un utilisateur par ID
     */
    public LiveData<User> getUserById(int userId) {
        return userDao.getUserById(userId);
    }
}