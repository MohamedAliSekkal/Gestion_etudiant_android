package ma.ensa.mobile.studentmanagement.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * StudentModule Entity
 * Join table for many-to-many relationship between Students and Modules
 * Represents enrollment of a student in a module
 */
@Entity(
    tableName = "student_modules",
    foreignKeys = {
        @ForeignKey(
            entity = Student.class,
            parentColumns = "student_id",
            childColumns = "student_id",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = Module.class,
            parentColumns = "module_id",
            childColumns = "module_id",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {
        @Index(value = {"student_id", "module_id"}, unique = true), // Unique constraint
        @Index(value = "student_id"),
        @Index(value = "module_id")
    }
)
public class StudentModule {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "enrollment_id")
    private int enrollmentId;

    @ColumnInfo(name = "student_id")
    private int studentId;

    @ColumnInfo(name = "module_id")
    private int moduleId;

    @ColumnInfo(name = "enrollment_date")
    private Long enrollmentDate; // When the student enrolled

    @ColumnInfo(name = "is_active")
    private boolean isActive; // Still enrolled or dropped

    @ColumnInfo(name = "created_at")
    private Long createdAt;

    @ColumnInfo(name = "updated_at")
    private Long updatedAt;

    // Constructors
    public StudentModule() {
        this.isActive = true;
        this.enrollmentDate = System.currentTimeMillis() / 1000;
        this.createdAt = System.currentTimeMillis() / 1000;
        this.updatedAt = System.currentTimeMillis() / 1000;
    }

    @androidx.room.Ignore
    public StudentModule(int studentId, int moduleId) {
        this();
        this.studentId = studentId;
        this.moduleId = moduleId;
    }

    // Getters and Setters
    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public Long getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(Long enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
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
