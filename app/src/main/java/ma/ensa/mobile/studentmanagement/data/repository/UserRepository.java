package ma.ensa.mobile.studentmanagement.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import org.mindrot.jbcrypt.BCrypt;

import ma.ensa.mobile.studentmanagement.data.local.dao.RoleDao;
import ma.ensa.mobile.studentmanagement.data.local.dao.StudentDao;
import ma.ensa.mobile.studentmanagement.data.local.dao.UserDao;
import ma.ensa.mobile.studentmanagement.data.local.database.AppDatabase;
import ma.ensa.mobile.studentmanagement.data.local.entity.Role;
import ma.ensa.mobile.studentmanagement.data.local.entity.Student;
import ma.ensa.mobile.studentmanagement.data.local.entity.User;

public class UserRepository {

    private static final String TAG = "UserRepository";
    private static final int MAX_FAILED_ATTEMPTS = 5;

    private UserDao userDao;
    private RoleDao roleDao;
    private StudentDao studentDao;

    public UserRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        userDao = database.userDao();
        roleDao = database.roleDao();
        studentDao = database.studentDao();
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

    /**
     * Enregistre un nouvel utilisateur
     * @return User ID si succès, -1 si username existe, -2 si email existe, -3 si erreur
     */
    public long registerUser(User user) {
        try {
            // Vérifier si le username existe déjà
            if (userDao.usernameExists(user.getUsername())) {
                Log.w(TAG, "Username already exists: " + user.getUsername());
                return -1;
            }

            // Vérifier si l'email existe déjà
            if (userDao.emailExists(user.getEmail())) {
                Log.w(TAG, "Email already exists: " + user.getEmail());
                return -2;
            }

            // Hasher le mot de passe avec BCrypt
            String hashedPassword = BCrypt.hashpw(user.getPasswordHash(), BCrypt.gensalt());
            user.setPasswordHash(hashedPassword);

            // Définir les valeurs par défaut si non définies
            if (user.getRoleId() == 0) {
                user.setRoleId(5); // Role par défaut: Étudiant (role_id = 5)
            }
            user.setActive(true);
            user.setLocked(false);
            user.setFailedLoginAttempts(0);

            long currentTime = System.currentTimeMillis() / 1000;
            user.setCreatedAt(currentTime);
            user.setUpdatedAt(currentTime);

            // Insérer l'utilisateur
            long userId = userDao.insertUser(user);
            Log.i(TAG, "User registered successfully: " + user.getUsername() + " (ID: " + userId + ")");

            // Si c'est un étudiant, créer aussi le profil Student
            if (user.getRoleId() == 5 && userId > 0) {
                createStudentProfile(user, currentTime);
            }

            return userId;

        } catch (Exception e) {
            Log.e(TAG, "Error during user registration", e);
            return -3;
        }
    }

    /**
     * Crée un profil étudiant lors de l'inscription
     */
    private void createStudentProfile(User user, long timestamp) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                Student student = new Student();

                // Auto-générer Apogée et CNE basés sur timestamp
                String timeId = String.valueOf(timestamp).substring(5); // Derniers chiffres du timestamp
                student.setApogeeNumber("APG" + timeId);
                student.setCne("R" + timeId);

                // Extraire prénom et nom du fullName (format: "Prénom Nom")
                String fullName = user.getFullName();
                String[] nameParts = fullName.trim().split("\\s+", 2);
                student.setFirstName(nameParts[0]);
                student.setLastName(nameParts.length > 1 ? nameParts[1] : nameParts[0]);

                student.setEmail(user.getEmail());
                student.setPhone(user.getPhone());
                student.setStatus("ACTIVE");
                student.setArchived(false);
                student.setEnrollmentDate(timestamp);
                student.setCreatedAt(timestamp);
                student.setUpdatedAt(timestamp);

                long studentId = studentDao.insertStudent(student);
                Log.i(TAG, "Student profile created: " + student.getApogeeNumber() + " (ID: " + studentId + ")");

            } catch (Exception e) {
                Log.e(TAG, "Error creating student profile", e);
            }
        });
    }

    /**
     * Vérifie si un nom d'utilisateur existe
     */
    public boolean isUsernameAvailable(String username) {
        return !userDao.usernameExists(username);
    }

    /**
     * Vérifie si un email existe
     */
    public boolean isEmailAvailable(String email) {
        return !userDao.emailExists(email);
    }
}