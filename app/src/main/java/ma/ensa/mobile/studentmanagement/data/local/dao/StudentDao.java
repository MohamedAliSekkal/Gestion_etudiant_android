package ma.ensa.mobile.studentmanagement.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ma.ensa.mobile.studentmanagement.data.local.entity.Student;

@Dao
public interface StudentDao {

    /**
     * Insert a new student
     */
    @Insert
    long insertStudent(Student student);

    /**
     * Update an existing student
     */
    @Update
    void updateStudent(Student student);

    /**
     * Delete a student
     */
    @Delete
    void deleteStudent(Student student);

    /**
     * Get a student by ID
     */
    @Query("SELECT * FROM students WHERE student_id = :id")
    LiveData<Student> getStudentById(int id);

    /**
     * Get a student by ID (synchronous)
     */
    @Query("SELECT * FROM students WHERE student_id = :id")
    Student getStudentByIdSync(int id);

    /**
     * Get a student by Apogee number (BF4 - Search)
     */
    @Query("SELECT * FROM students WHERE apogee_number = :apogeeNumber")
    LiveData<Student> getStudentByApogee(String apogeeNumber);

    /**
     * Get a student by CNE
     */
    @Query("SELECT * FROM students WHERE cne = :cne")
    LiveData<Student> getStudentByCNE(String cne);

    /**
     * Get all active students (not archived)
     */
    @Query("SELECT * FROM students WHERE is_archived = 0 ORDER BY last_name ASC, first_name ASC")
    LiveData<List<Student>> getAllActiveStudents();

    /**
     * Get all archived students
     */
    @Query("SELECT * FROM students WHERE is_archived = 1 ORDER BY archive_date DESC")
    LiveData<List<Student>> getAllArchivedStudents();

    /**
     * Get all students (active and archived)
     */
    @Query("SELECT * FROM students ORDER BY last_name ASC, first_name ASC")
    LiveData<List<Student>> getAllStudents();

    /**
     * Search students by name or apogee number
     */
    @Query("SELECT * FROM students WHERE " +
           "(first_name LIKE '%' || :query || '%' OR " +
           "last_name LIKE '%' || :query || '%' OR " +
           "apogee_number LIKE '%' || :query || '%') " +
           "AND is_archived = 0 " +
           "ORDER BY last_name ASC, first_name ASC")
    LiveData<List<Student>> searchStudents(String query);

    /**
     * Archive a student (soft delete)
     */
    @Query("UPDATE students SET is_archived = 1, archive_date = :archiveDate, status = 'ARCHIVED' WHERE student_id = :studentId")
    void archiveStudent(int studentId, long archiveDate);

    /**
     * Restore an archived student
     */
    @Query("UPDATE students SET is_archived = 0, archive_date = NULL, status = 'ACTIVE' WHERE student_id = :studentId")
    void restoreStudent(int studentId);

    /**
     * Check if apogee number exists
     */
    @Query("SELECT COUNT(*) > 0 FROM students WHERE apogee_number = :apogeeNumber")
    boolean apogeeNumberExists(String apogeeNumber);

    /**
     * Check if CNE exists
     */
    @Query("SELECT COUNT(*) > 0 FROM students WHERE cne = :cne")
    boolean cneExists(String cne);

    /**
     * Check if email exists
     */
    @Query("SELECT COUNT(*) > 0 FROM students WHERE email = :email")
    boolean emailExists(String email);

    /**
     * Get count of active students
     */
    @Query("SELECT COUNT(*) FROM students WHERE is_archived = 0")
    int getActiveStudentCount();

    /**
     * Get count of all students
     */
    @Query("SELECT COUNT(*) FROM students")
    int getTotalStudentCount();

    /**
     * Get students by status
     */
    @Query("SELECT * FROM students WHERE status = :status ORDER BY last_name ASC, first_name ASC")
    LiveData<List<Student>> getStudentsByStatus(String status);

    /**
     * Get count of active students (LiveData for dashboard)
     */
    @Query("SELECT COUNT(*) FROM students WHERE is_archived = 0")
    LiveData<Integer> getActiveStudentCountLive();

    /**
     * Get count of archived students
     */
    @Query("SELECT COUNT(*) FROM students WHERE is_archived = 1")
    LiveData<Integer> getArchivedStudentCountLive();

    /**
     * Get count of students by status
     */
    @Query("SELECT COUNT(*) FROM students WHERE status = :status AND is_archived = 0")
    LiveData<Integer> getStudentCountByStatus(String status);

    /**
     * Advanced search: Search with status filter
     * @param query Search query (name or apogee, use "" for all)
     * @param includeArchived Include archived students (0 = active only, 1 = all)
     */
    @Query("SELECT * FROM students WHERE " +
           "(:query = '' OR first_name LIKE '%' || :query || '%' OR " +
           "last_name LIKE '%' || :query || '%' OR " +
           "apogee_number LIKE '%' || :query || '%') " +
           "AND (:includeArchived = 1 OR is_archived = 0) " +
           "ORDER BY last_name ASC, first_name ASC")
    LiveData<List<Student>> searchWithStatusFilter(String query, int includeArchived);

    /**
     * Get all students (including archived) for filtering
     */
    @Query("SELECT * FROM students ORDER BY is_archived ASC, last_name ASC, first_name ASC")
    LiveData<List<Student>> getAllStudentsIncludingArchived();
}
