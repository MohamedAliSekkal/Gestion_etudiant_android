package ma.ensa.mobile.studentmanagement.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * B1: Absence Entity
 * Represents student absence records
 */
@Entity(
    tableName = "absences",
    foreignKeys = {
        @ForeignKey(
            entity = Student.class,
            parentColumns = "student_id",
            childColumns = "student_id",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {
        @Index(value = "student_id"),
        @Index(value = "absence_date")
    }
)
public class Absence {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "absence_id")
    private int absenceId;

    @ColumnInfo(name = "student_id")
    private int studentId;

    @ColumnInfo(name = "absence_date")
    private Long absenceDate; // Timestamp in seconds

    @ColumnInfo(name = "module_code")
    private String moduleCode; // e.g., "INF101"

    @ColumnInfo(name = "module_name")
    private String moduleName; // e.g., "Programmation Java"

    @ColumnInfo(name = "absence_type")
    private String absenceType; // "JUSTIFIED" or "UNJUSTIFIED"

    @ColumnInfo(name = "justification")
    private String justification; // Justification text if any

    @ColumnInfo(name = "is_justified")
    private boolean isJustified;

    @ColumnInfo(name = "hours")
    private int hours; // Number of hours absent (typically 2 or 4)

    @ColumnInfo(name = "semester")
    private String semester; // e.g., "S1", "S2", "S3"

    @ColumnInfo(name = "academic_year")
    private String academicYear; // e.g., "2024-2025"

    @ColumnInfo(name = "created_at")
    private Long createdAt;

    @ColumnInfo(name = "updated_at")
    private Long updatedAt;

    // Constructors
    public Absence() {
    }

    // Getters and Setters
    public int getAbsenceId() {
        return absenceId;
    }

    public void setAbsenceId(int absenceId) {
        this.absenceId = absenceId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public Long getAbsenceDate() {
        return absenceDate;
    }

    public void setAbsenceDate(Long absenceDate) {
        this.absenceDate = absenceDate;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getAbsenceType() {
        return absenceType;
    }

    public void setAbsenceType(String absenceType) {
        this.absenceType = absenceType;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public boolean isJustified() {
        return isJustified;
    }

    public void setJustified(boolean justified) {
        isJustified = justified;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
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
