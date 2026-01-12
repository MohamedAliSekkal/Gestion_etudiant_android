package ma.ensa.mobile.studentmanagement.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Professor Entity
 * Links a User (with role=TEACHER) to professor-specific information
 */
@Entity(
    tableName = "professors",
    foreignKeys = {
        @ForeignKey(
            entity = User.class,
            parentColumns = "user_id",
            childColumns = "user_id",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {
        @Index(value = "user_id", unique = true),
        @Index(value = "employee_number", unique = true)
    }
)
public class Professor {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "professor_id")
    private int professorId;

    @ColumnInfo(name = "user_id")
    private int userId; // Links to users table

    @ColumnInfo(name = "employee_number")
    @NonNull
    private String employeeNumber; // e.g., "PROF001"

    @ColumnInfo(name = "first_name")
    @NonNull
    private String firstName;

    @ColumnInfo(name = "last_name")
    @NonNull
    private String lastName;

    @ColumnInfo(name = "email")
    @NonNull
    private String email;

    @ColumnInfo(name = "phone")
    private String phone;

    @ColumnInfo(name = "department")
    private String department; // e.g., "Informatique", "Mathématiques"

    @ColumnInfo(name = "specialization")
    private String specialization; // e.g., "Machine Learning", "Réseaux"

    @ColumnInfo(name = "office")
    private String office; // Office location

    @ColumnInfo(name = "is_active")
    private boolean isActive;

    @ColumnInfo(name = "hire_date")
    private Long hireDate; // Timestamp

    @ColumnInfo(name = "created_at")
    private Long createdAt;

    @ColumnInfo(name = "updated_at")
    private Long updatedAt;

    // Constructors
    public Professor() {
        this.isActive = true;
        this.createdAt = System.currentTimeMillis() / 1000;
        this.updatedAt = System.currentTimeMillis() / 1000;
    }

    // Getters and Setters
    public int getProfessorId() {
        return professorId;
    }

    public void setProfessorId(int professorId) {
        this.professorId = professorId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @NonNull
    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(@NonNull String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    @NonNull
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NonNull String firstName) {
        this.firstName = firstName;
    }

    @NonNull
    public String getLastName() {
        return lastName;
    }

    public void setLastName(@NonNull String lastName) {
        this.lastName = lastName;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Long getHireDate() {
        return hireDate;
    }

    public void setHireDate(Long hireDate) {
        this.hireDate = hireDate;
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
     * Get full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
