package ma.ensa.mobile.studentmanagement.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ma.ensa.mobile.studentmanagement.data.local.entity.Grade;

/**
 * B2: Grade DAO
 * Database access for grade records
 */
@Dao
public interface GradeDao {

    /**
     * Insert a new grade
     */
    @Insert
    long insertGrade(Grade grade);

    /**
     * Update a grade
     */
    @Update
    void updateGrade(Grade grade);

    /**
     * Get all grades for a student
     */
    @Query("SELECT * FROM grades WHERE student_id = :studentId ORDER BY semester ASC, exam_date DESC")
    LiveData<List<Grade>> getGradesByStudent(int studentId);

    /**
     * Get grades by student and semester
     */
    @Query("SELECT * FROM grades WHERE student_id = :studentId AND semester = :semester ORDER BY module_id ASC")
    LiveData<List<Grade>> getGradesByStudentAndSemester(int studentId, String semester);

    /**
     * Get grades by student and academic year
     */
    @Query("SELECT * FROM grades WHERE student_id = :studentId AND academic_year = :academicYear ORDER BY semester ASC, module_id ASC")
    LiveData<List<Grade>> getGradesByStudentAndYear(int studentId, String academicYear);

    /**
     * Get grades by module
     */
    @Query("SELECT * FROM grades WHERE student_id = :studentId AND module_id = :moduleId ORDER BY exam_date DESC")
    LiveData<List<Grade>> getGradesByModule(int studentId, int moduleId);

    /**
     * Get average grade for a student
     */
    @Query("SELECT AVG(grade_value) FROM grades WHERE student_id = :studentId")
    LiveData<Double> getAverageGrade(int studentId);

    /**
     * Get average grade by semester
     */
    @Query("SELECT AVG(grade_value) FROM grades WHERE student_id = :studentId AND semester = :semester")
    LiveData<Double> getAverageGradeBySemester(int studentId, String semester);

    /**
     * Get weighted average (considering coefficients)
     */
    @Query("SELECT SUM(grade_value * coefficient) / SUM(coefficient) " +
           "FROM grades WHERE student_id = :studentId AND semester = :semester")
    LiveData<Double> getWeightedAverageBySemester(int studentId, String semester);

    /**
     * Get total credits earned
     */
    @Query("SELECT COALESCE(SUM(credits), 0) FROM grades " +
           "WHERE student_id = :studentId AND status = 'PASSED'")
    LiveData<Integer> getTotalCreditsEarned(int studentId);

    /**
     * Get grade count by semester
     */
    @Query("SELECT COUNT(*) FROM grades WHERE student_id = :studentId AND semester = :semester")
    LiveData<Integer> getGradeCountBySemester(int studentId, String semester);

    /**
     * Get failed grades
     */
    @Query("SELECT * FROM grades WHERE student_id = :studentId AND status = 'FAILED' ORDER BY semester ASC")
    LiveData<List<Grade>> getFailedGrades(int studentId);

    /**
     * Check if module is passed
     */
    @Query("SELECT COUNT(*) > 0 FROM grades " +
           "WHERE student_id = :studentId AND module_id = :moduleId AND status = 'PASSED'")
    boolean isModulePassed(int studentId, int moduleId);

    /**
     * Get grades by module (for professors)
     */
    @Query("SELECT * FROM grades WHERE module_id = :moduleId ORDER BY student_id, exam_date DESC")
    LiveData<List<Grade>> getGradesByModuleOnly(int moduleId);

    /**
     * Get all grades for a specific student in a specific module
     */
    @Query("SELECT * FROM grades WHERE student_id = :studentId AND module_id = :moduleId ORDER BY exam_date DESC")
    LiveData<List<Grade>> getStudentGradesInModule(int studentId, int moduleId);
}
