package ma.ensa.mobile.studentmanagement.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "students",
    indices = {
        @Index(value = "apogee_number", unique = true),
        @Index(value = "cne", unique = true),
        @Index(value = "email", unique = true)
    }
)
public class Student {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "student_id")
    private int studentId;

    @ColumnInfo(name = "apogee_number")
    @NonNull
    private String apogeeNumber;

    @ColumnInfo(name = "cne")
    @NonNull
    private String cne;

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

    @ColumnInfo(name = "date_of_birth")
    private Long dateOfBirth; // Timestamp

    @ColumnInfo(name = "gender")
    private String gender; // M/F

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "city")
    private String city;

    @ColumnInfo(name = "status")
    @NonNull
    private String status; // ACTIVE, ARCHIVED, GRADUATED

    @ColumnInfo(name = "enrollment_date")
    private Long enrollmentDate; // Timestamp

    @ColumnInfo(name = "graduation_date")
    private Long graduationDate; // Timestamp (nullable)

    @ColumnInfo(name = "is_archived")
    private boolean isArchived;

    @ColumnInfo(name = "archive_date")
    private Long archiveDate; // Timestamp (nullable)

    @ColumnInfo(name = "created_at")
    private Long createdAt; // Timestamp

    @ColumnInfo(name = "updated_at")
    private Long updatedAt; // Timestamp

    // Constructors
    public Student() {
        this.status = "ACTIVE";
        this.isArchived = false;
    }

    // Getters and Setters
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    @NonNull
    public String getApogeeNumber() {
        return apogeeNumber;
    }

    public void setApogeeNumber(@NonNull String apogeeNumber) {
        this.apogeeNumber = apogeeNumber;
    }

    @NonNull
    public String getCne() {
        return cne;
    }

    public void setCne(@NonNull String cne) {
        this.cne = cne;
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

    public Long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @NonNull
    public String getStatus() {
        return status;
    }

    public void setStatus(@NonNull String status) {
        this.status = status;
    }

    public Long getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(Long enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public Long getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(Long graduationDate) {
        this.graduationDate = graduationDate;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public Long getArchiveDate() {
        return archiveDate;
    }

    public void setArchiveDate(Long archiveDate) {
        this.archiveDate = archiveDate;
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

    // Helper methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", apogeeNumber='" + apogeeNumber + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
