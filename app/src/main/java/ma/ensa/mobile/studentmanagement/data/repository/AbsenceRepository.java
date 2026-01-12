package ma.ensa.mobile.studentmanagement.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import ma.ensa.mobile.studentmanagement.data.local.dao.AbsenceDao;
import ma.ensa.mobile.studentmanagement.data.local.database.AppDatabase;
import ma.ensa.mobile.studentmanagement.data.local.entity.Absence;

/**
 * B1: Absence Repository
 * Manages absence data access
 */
public class AbsenceRepository {

    private AbsenceDao absenceDao;

    public AbsenceRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        absenceDao = database.absenceDao();
    }

    /**
     * Get all absences for a student
     */
    public LiveData<List<Absence>> getAbsencesByStudent(int studentId) {
        return absenceDao.getAbsencesByStudent(studentId);
    }

    /**
     * Get absences by student and semester
     */
    public LiveData<List<Absence>> getAbsencesByStudentAndSemester(int studentId, String semester) {
        return absenceDao.getAbsencesByStudentAndSemester(studentId, semester);
    }

    /**
     * Get absences by student and academic year
     */
    public LiveData<List<Absence>> getAbsencesByStudentAndYear(int studentId, String academicYear) {
        return absenceDao.getAbsencesByStudentAndYear(studentId, academicYear);
    }

    /**
     * Get unjustified absences count
     */
    public LiveData<Integer> getUnjustifiedAbsencesCount(int studentId) {
        return absenceDao.getUnjustifiedAbsencesCount(studentId);
    }

    /**
     * Get total absence hours
     */
    public LiveData<Integer> getTotalAbsenceHours(int studentId) {
        return absenceDao.getTotalAbsenceHours(studentId);
    }

    /**
     * Get unjustified absence hours
     */
    public LiveData<Integer> getUnjustifiedAbsenceHours(int studentId) {
        return absenceDao.getUnjustifiedAbsenceHours(studentId);
    }

    /**
     * Get absences by module
     */
    public LiveData<List<Absence>> getAbsencesByModule(int studentId, String moduleCode) {
        return absenceDao.getAbsencesByModule(studentId, moduleCode);
    }

    /**
     * Get absence count by semester
     */
    public LiveData<Integer> getAbsenceCountBySemester(int studentId, String semester) {
        return absenceDao.getAbsenceCountBySemester(studentId, semester);
    }

    /**
     * Insert a new absence
     */
    public void insertAbsence(Absence absence) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            absenceDao.insertAbsence(absence);
        });
    }
}
