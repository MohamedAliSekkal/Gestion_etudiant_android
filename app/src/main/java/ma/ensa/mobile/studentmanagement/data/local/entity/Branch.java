package ma.ensa.mobile.studentmanagement.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entity representing an academic branch (filière)
 * Examples: GI, GSTR, ML, GM, GC, GCSE, BDAI
 */
@Entity(
    tableName = "branches",
    indices = {
        @Index(value = "branch_code", unique = true)
    }
)
public class Branch {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "branch_id")
    private int branchId;

    @ColumnInfo(name = "branch_code")
    @NonNull
    private String branchCode; // Ex: GI, GSTR

    @ColumnInfo(name = "branch_name")
    @NonNull
    private String branchName; // Ex: Génie Informatique

    @ColumnInfo(name = "branch_name_en")
    private String branchNameEn; // Ex: Computer Engineering

    @ColumnInfo(name = "cycle")
    @NonNull
    private String cycle; // PREPARATORY or ENGINEERING

    @ColumnInfo(name = "capacity")
    private int capacity; // Max students

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "is_active")
    private boolean isActive;

    @ColumnInfo(name = "created_at")
    private Long createdAt;

    @ColumnInfo(name = "updated_at")
    private Long updatedAt;

    // Constructors
    public Branch() {
        this.isActive = true;
    }

    // Getters and Setters
    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    @NonNull
    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(@NonNull String branchCode) {
        this.branchCode = branchCode;
    }

    @NonNull
    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(@NonNull String branchName) {
        this.branchName = branchName;
    }

    public String getBranchNameEn() {
        return branchNameEn;
    }

    public void setBranchNameEn(String branchNameEn) {
        this.branchNameEn = branchNameEn;
    }

    @NonNull
    public String getCycle() {
        return cycle;
    }

    public void setCycle(@NonNull String cycle) {
        this.cycle = cycle;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
