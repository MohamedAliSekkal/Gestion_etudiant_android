package ma.ensa.mobile.studentmanagement.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import ma.ensa.mobile.studentmanagement.data.local.entity.User;

@Dao
public interface UserDao {

    @Insert
    long insertUser(User user);

    @Update
    void updateUser(User user);

    @Query("SELECT * FROM users WHERE user_id = :userId")
    LiveData<User> getUserById(int userId);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User getUserByUsername(String username);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);

    @Query("SELECT * FROM users WHERE username = :username AND is_active = 1 LIMIT 1")
    User getUserForLogin(String username);

    @Query("UPDATE users SET failed_login_attempts = :attempts WHERE user_id = :userId")
    void updateFailedAttempts(int userId, int attempts);

    @Query("UPDATE users SET is_locked = :locked WHERE user_id = :userId")
    void updateLockStatus(int userId, boolean locked);

    @Query("UPDATE users SET last_login = :timestamp WHERE user_id = :userId")
    void updateLastLogin(int userId, long timestamp);

    @Query("SELECT COUNT(*) FROM users")
    int getUserCount();
}