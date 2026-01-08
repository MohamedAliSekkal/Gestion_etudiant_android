package ma.ensa.mobile.studentmanagement.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "users",
        foreignKeys = @ForeignKey(
                entity = Role.class,
                parentColumns = "role_id",
                childColumns = "role_id",
                onDelete = ForeignKey.RESTRICT
        ),
        indices = {
                @Index(value = "username", unique = true),
                @Index(value = "email", unique = true),
                @Index(value = "role_id")
        }
)
public class User {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "password_hash")
    private String passwordHash;

    @ColumnInfo(name = "role_id")
    private int roleId;

    @ColumnInfo(name = "full_name")
    private String fullName;

    @ColumnInfo(name = "phone")
    private String phone;

    @ColumnInfo(name = "is_active")
    private boolean isActive;

    @ColumnInfo(name = "is_locked")
    private boolean isLocked;

    @ColumnInfo(name = "failed_login_attempts")
    private int failedLoginAttempts;

    @ColumnInfo(name = "last_login")
    private Long lastLogin;

    @ColumnInfo(name = "auth_token")
    private String authToken;

    @ColumnInfo(name = "token_expiry")
    private Long tokenExpiry;

    @ColumnInfo(name = "created_at")
    private long createdAt;

    @ColumnInfo(name = "updated_at")
    private long updatedAt;

    // Constructeur
    public User() {
        this.isActive = true;
        this.isLocked = false;
        this.failedLoginAttempts = 0;
        this.createdAt = System.currentTimeMillis() / 1000;
        this.updatedAt = System.currentTimeMillis() / 1000;
    }

    // Getters et Setters (générez-les avec Alt+Insert → Getter and Setter)

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public Long getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Long getTokenExpiry() {
        return tokenExpiry;
    }

    public void setTokenExpiry(Long tokenExpiry) {
        this.tokenExpiry = tokenExpiry;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}