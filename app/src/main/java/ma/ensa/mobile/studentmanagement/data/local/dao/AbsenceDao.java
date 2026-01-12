package ma.ensa.mobile.studentmanagement.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ma.ensa.mobile.studentmanagement.data.local.entity.Absence;

/**
 * B1: Absence DAO
 * Database access for absence records
 */
@Dao
public interface AbsenceDao {

    /**
     * Insert a new absence
     */
    @Insert
    long insertAbsence(Absence absence);

    /**
     * Update an absence
     */
    @Update
    void updateAbsence(Absence absence);

    /**
     * Get all absences for a student
     */
    @Query("SELECT * FROM absences WHERE student_id = :studentId ORDER BY absence_date DESC")
    LiveData<List<Absence>> getAbsencesByStudent(int studentId);

    /**
     * Get absences by student and semester
     */
    @Query("SELECT * FROM absences WHERE student_id = :studentId AND semester = :semester ORDER BY absence_date DESC")
    LiveData<List<Absence>> getAbsencesByStudentAndSemester(int studentId, String semester);

    /**
     * Get absences by student and academic year
     */
    @Query("SELECT * FROM absences WHERE student_id = :studentId AND academic_year = :academicYear ORDER BY absence_date DESC")
    LiveData<List<Absence>> getAbsencesByStudentAndYear(int studentId, String academicYear);

    /**
     * Get unjustified absences count for a student
     */
    @Query("SELECT COUNT(*) FROM absences WHERE student_id = :studentId AND is_justified = 0")
    LiveData<Integer> getUnjustifiedAbsencesCount(int studentId);

    /**
     * Get total absence hours for a student
     */
    @Query("SELECT COALESCE(SUM(hours), 0) FROM absences WHERE student_id = :studentId")
    LiveData<Integer> getTotalAbsenceHours(int studentId);

    /**
     * Get unjustified absence hours for a student
     */
    @Query("SELECT COALESCE(SUM(hours), 0) FROM absences WHERE student_id = :studentId AND is_justified = 0")
    LiveData<Integer> getUnjustifiedAbsenceHours(int studentId);

    /**
     * Get absences by module
     */
    @Query("SELECT * FROM absences WHERE student_id = :studentId AND module_code = :moduleCode ORDER BY absence_date DESC")
    LiveData<List<Absence>> getAbsencesByModule(int studentId, String moduleCode);

    /**
     * Get absence count by semester
     */
    @Query("SELECT COUNT(*) FROM absences WHERE student_id = :studentId AND semester = :semester")
    LiveData<Integer> getAbsenceCountBySemester(int studentId, String semester);
}
