package ma.ensa.mobile.studentmanagement.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import ma.ensa.mobile.studentmanagement.data.local.dao.StudentDao;
import ma.ensa.mobile.studentmanagement.data.local.database.AppDatabase;
import ma.ensa.mobile.studentmanagement.data.local.entity.Student;

public class StudentRepository {

    private static final String TAG = "StudentRepository";

    private StudentDao studentDao;
    private LiveData<List<Student>> allActiveStudents;

    public StudentRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        studentDao = database.studentDao();
        allActiveStudents = studentDao.getAllActiveStudents();
    }

    /**
     * Insert a new student
     * @return Student ID if success, -1 if apogee exists, -2 if cne exists, -3 if email exists, -4 if error
     */
    public long insert(Student student) {
        try {
            // Check if apogee number exists
            if (studentDao.apogeeNumberExists(student.getApogeeNumber())) {
                Log.w(TAG, "Apogee number already exists: " + student.getApogeeNumber());
                return -1;
            }

            // Check if CNE exists
            if (studentDao.cneExists(student.getCne())) {
                Log.w(TAG, "CNE already exists: " + student.getCne());
                return -2;
            }

            // Check if email exists
            if (studentDao.emailExists(student.getEmail())) {
                Log.w(TAG, "Email already exists: " + student.getEmail());
                return -3;
            }

            // Set timestamps
            long currentTime = System.currentTimeMillis() / 1000;
            student.setCreatedAt(currentTime);
            student.setUpdatedAt(currentTime);

            // Insert student
            long studentId = studentDao.insertStudent(student);
            Log.i(TAG, "Student inserted successfully: " + student.getFullName() + " (ID: " + studentId + ")");
            return studentId;

        } catch (Exception e) {
            Log.e(TAG, "Error inserting student", e);
            return -4;
        }
    }

    /**
     * Update an existing student
     */
    public void update(Student student) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                student.setUpdatedAt(System.currentTimeMillis() / 1000);
                studentDao.updateStudent(student);
                Log.i(TAG, "Student updated successfully: " + student.getFullName());
            } catch (Exception e) {
                Log.e(TAG, "Error updating student", e);
            }
        });
    }

    /**
     * Delete a student
     */
    public void delete(Student student) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                studentDao.deleteStudent(student);
                Log.i(TAG, "Student deleted successfully: " + student.getFullName());
            } catch (Exception e) {
                Log.e(TAG, "Error deleting student", e);
            }
        });
    }

    /**
     * Archive a student (soft delete)
     */
    public void archive(int studentId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                long archiveDate = System.currentTimeMillis() / 1000;
                studentDao.archiveStudent(studentId, archiveDate);
                Log.i(TAG, "Student archived successfully (ID: " + studentId + ")");
            } catch (Exception e) {
                Log.e(TAG, "Error archiving student", e);
            }
        });
    }

    /**
     * Restore an archived student
     */
    public void restore(int studentId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                studentDao.restoreStudent(studentId);
                Log.i(TAG, "Student restored successfully (ID: " + studentId + ")");
            } catch (Exception e) {
                Log.e(TAG, "Error restoring student", e);
            }
        });
    }

    /**
     * Get all active students
     */
    public LiveData<List<Student>> getAllActiveStudents() {
        return allActiveStudents;
    }

    /**
     * Get all archived students
     */
    public LiveData<List<Student>> getAllArchivedStudents() {
        return studentDao.getAllArchivedStudents();
    }

    /**
     * Get all students (active and archived)
     */
    public LiveData<List<Student>> getAllStudents() {
        return studentDao.getAllStudents();
    }

    /**
     * Get a student by ID
     */
    public LiveData<Student> getStudentById(int studentId) {
        return studentDao.getStudentById(studentId);
    }

    /**
     * Search by Apogee number (BF4)
     */
    public LiveData<Student> searchByApogee(String apogeeNumber) {
        return studentDao.getStudentByApogee(apogeeNumber);
    }

    /**
     * Search students by query (name or apogee)
     */
    public LiveData<List<Student>> searchStudents(String query) {
        return studentDao.searchStudents(query);
    }

    /**
     * Get students by status
     */
    public LiveData<List<Student>> getStudentsByStatus(String status) {
        return studentDao.getStudentsByStatus(status);
    }

    /**
     * Get student count
     */
    public int getActiveStudentCount() {
        return studentDao.getActiveStudentCount();
    }

    /**
     * Get total student count
     */
    public int getTotalStudentCount() {
        return studentDao.getTotalStudentCount();
    }
}
