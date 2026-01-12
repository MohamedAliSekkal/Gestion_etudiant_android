package ma.ensa.mobile.studentmanagement.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Module Entity
 * Represents a course/subject taught by a professor
 */
@Entity(
    tableName = "modules",
    foreignKeys = {
        @ForeignKey(
            entity = Professor.class,
            parentColumns = "professor_id",
            childColumns = "professor_id",
            onDelete = ForeignKey.SET_NULL
        ),
        @ForeignKey(
            entity = Branch.class,
            parentColumns = "branch_id",
            childColumns = "branch_id",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = Level.class,
            parentColumns = "level_id",
            childColumns = "level_id",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {
        @Index(value = "module_code", unique = true),
        @Index(value = "professor_id"),
        @Index(value = "branch_id"),
        @Index(value = "level_id")
    }
)
public class Module {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "module_id")
    private int moduleId;

    @ColumnInfo(name = "module_code")
    @NonNull
    private String moduleCode; // e.g., "INF101"

    @ColumnInfo(name = "module_name")
    @NonNull
    private String moduleName; // e.g., "Programmation Java"

    @ColumnInfo(name = "professor_id")
    private Integer professorId; // Nullable - can be unassigned

    @ColumnInfo(name = "branch_id")
    private Integer branchId; // Nullable - some modules are common

    @ColumnInfo(name = "level_id")
    private Integer levelId; // Nullable - some modules span levels

    @ColumnInfo(name = "semester")
    @NonNull
    private String semester; // "S1", "S2", etc.

    @ColumnInfo(name = "credits")
    private Integer credits; // ECTS credits

    @ColumnInfo(name = "coefficient")
    private Double coefficient;

    @ColumnInfo(name = "hours_course")
    private Integer hoursCourse; // Theory hours

    @ColumnInfo(name = "hours_td")
    private Integer hoursTd; // TD hours

    @ColumnInfo(name = "hours_tp")
    private Integer hoursTp; // TP hours

    @ColumnInfo(name = "academic_year")
    @NonNull
    private String academicYear; // "2024-2025"

    @ColumnInfo(name = "is_active")
    private boolean isActive;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "created_at")
    private Long createdAt;

    @ColumnInfo(name = "updated_at")
    private Long updatedAt;

    // Constructors
    public Module() {
        this.isActive = true;
        this.createdAt = System.currentTimeMillis() / 1000;
        this.updatedAt = System.currentTimeMillis() / 1000;
    }

    // Getters and Setters
    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    @NonNull
    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(@NonNull String moduleCode) {
        this.moduleCode = moduleCode;
    }

    @NonNull
    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(@NonNull String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Integer professorId) {
        this.professorId = professorId;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    @NonNull
    public String getSemester() {
        return semester;
    }

    public void setSemester(@NonNull String semester) {
        this.semester = semester;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public Double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

    public Integer getHoursCourse() {
        return hoursCourse;
    }

    public void setHoursCourse(Integer hoursCourse) {
        this.hoursCourse = hoursCourse;
    }

    public Integer getHoursTd() {
        return hoursTd;
    }

    public void setHoursTd(Integer hoursTd) {
        this.hoursTd = hoursTd;
    }

    public Integer getHoursTp() {
        return hoursTp;
    }

    public void setHoursTp(Integer hoursTp) {
        this.hoursTp = hoursTp;
    }

    @NonNull
    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(@NonNull String academicYear) {
        this.academicYear = academicYear;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    /**
     * Get total hours for the module
     */
    public int getTotalHours() {
        int total = 0;
        if (hoursCourse != null) total += hoursCourse;
        if (hoursTd != null) total += hoursTd;
        if (hoursTp != null) total += hoursTp;
        return total;
    }
}
