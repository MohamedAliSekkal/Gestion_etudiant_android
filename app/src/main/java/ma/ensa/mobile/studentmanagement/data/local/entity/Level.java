package ma.ensa.mobile.studentmanagement.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entity representing an academic level (niveau)
 * Examples: CP1, CP2, CI1, CI2, CI3
 */
@Entity(
    tableName = "levels",
    indices = {
        @Index(value = "level_code", unique = true)
    }
)
public class Level {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "level_id")
    private int levelId;

    @ColumnInfo(name = "level_code")
    @NonNull
    private String levelCode; // Ex: CP1, CI2

    @ColumnInfo(name = "level_name")
    @NonNull
    private String levelName; // Ex: Cycle Préparatoire 1ère année

    @ColumnInfo(name = "level_name_en")
    private String levelNameEn; // Ex: Preparatory Cycle 1st Year

    @ColumnInfo(name = "year_number")
    private int yearNumber; // 1, 2, 3, 4, 5

    @ColumnInfo(name = "cycle")
    @NonNull
    private String cycle; // PREPARATORY or ENGINEERING

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "is_active")
    private boolean isActive;

    @ColumnInfo(name = "created_at")
    private Long createdAt;

    @ColumnInfo(name = "updated_at")
    private Long updatedAt;

    // Constructors
    public Level() {
        this.isActive = true;
    }

    // Getters and Setters
    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    @NonNull
    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(@NonNull String levelCode) {
        this.levelCode = levelCode;
    }

    @NonNull
    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(@NonNull String levelName) {
        this.levelName = levelName;
    }

    public String getLevelNameEn() {
        return levelNameEn;
    }

    public void setLevelNameEn(String levelNameEn) {
        this.levelNameEn = levelNameEn;
    }

    public int getYearNumber() {
        return yearNumber;
    }

    public void setYearNumber(int yearNumber) {
        this.yearNumber = yearNumber;
    }

    @NonNull
    public String getCycle() {
        return cycle;
    }

    public void setCycle(@NonNull String cycle) {
        this.cycle = cycle;
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
