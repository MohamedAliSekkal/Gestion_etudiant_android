package ma.ensa.mobile.studentmanagement.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * B2: Grade Entity
 * Represents student academic grades/notes
 */
@Entity(
    tableName = "grades",
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
        @Index(value = "student_id"),
        @Index(value = "module_id"),
        @Index(value = "semester"),
        @Index(value = "academic_year")
    }
)
public class Grade {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "grade_id")
    private int gradeId;

    @ColumnInfo(name = "student_id")
    private int studentId;

    @ColumnInfo(name = "module_id")
    private int moduleId; // Foreign key to modules table

    @ColumnInfo(name = "exam_type")
    private String examType; // "DS" (Devoir Surveillé), "EXAM" (Examen Final), "TP", "PROJECT"

    @ColumnInfo(name = "grade_value")
    private Double gradeValue; // Note (0-20)

    @ColumnInfo(name = "coefficient")
    private Double coefficient; // Coefficient

    @ColumnInfo(name = "semester")
    private String semester; // e.g., "S1", "S2", "S3"

    @ColumnInfo(name = "academic_year")
    private String academicYear; // e.g., "2024-2025"

    @ColumnInfo(name = "exam_date")
    private Long examDate; // Timestamp in seconds

    @ColumnInfo(name = "is_rattrapage")
    private boolean isRattrapage; // Is this a resit exam?

    @ColumnInfo(name = "credits")
    private Integer credits; // ECTS credits

    @ColumnInfo(name = "status")
    private String status; // "PASSED", "FAILED", "PENDING"

    @ColumnInfo(name = "created_at")
    private Long createdAt;

    @ColumnInfo(name = "updated_at")
    private Long updatedAt;

    // Constructors
    public Grade() {
    }

    // Getters and Setters
    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
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

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public Double getGradeValue() {
        return gradeValue;
    }

    public void setGradeValue(Double gradeValue) {
        this.gradeValue = gradeValue;
    }

    public Double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
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

    public Long getExamDate() {
        return examDate;
    }

    public void setExamDate(Long examDate) {
        this.examDate = examDate;
    }

    public boolean isRattrapage() {
        return isRattrapage;
    }

    public void setRattrapage(boolean rattrapage) {
        isRattrapage = rattrapage;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
     * Calculate weighted grade (grade * coefficient)
     */
    public Double getWeightedGrade() {
        if (gradeValue != null && coefficient != null) {
            return gradeValue * coefficient;
        }
        return 0.0;
    }
}
