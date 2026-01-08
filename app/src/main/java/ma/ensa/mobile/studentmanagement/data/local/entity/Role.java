package ma.ensa.mobile.studentmanagement.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "roles")
public class Role {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "role_id")
    private int roleId;

    @ColumnInfo(name = "role_code")
    private String roleCode;

    @ColumnInfo(name = "role_name")
    private String roleName;

    @ColumnInfo(name = "role_name_en")
    private String roleNameEn;

    @ColumnInfo(name = "role_name_fr")
    private String roleNameFr;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "priority_level")
    private int priorityLevel;

    @ColumnInfo(name = "is_active")
    private boolean isActive;

    @ColumnInfo(name = "created_at")
    private long createdAt;

    @ColumnInfo(name = "updated_at")
    private long updatedAt;

    // Constructeur
    public Role() {
        this.isActive = true;
        this.createdAt = System.currentTimeMillis() / 1000;
        this.updatedAt = System.currentTimeMillis() / 1000;
    }

    // Getters et Setters
    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleNameEn() {
        return roleNameEn;
    }

    public void setRoleNameEn(String roleNameEn) {
        this.roleNameEn = roleNameEn;
    }

    public String getRoleNameFr() {
        return roleNameFr;
    }

    public void setRoleNameFr(String roleNameFr) {
        this.roleNameFr = roleNameFr;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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