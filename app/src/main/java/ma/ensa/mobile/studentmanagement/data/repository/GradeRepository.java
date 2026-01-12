package ma.ensa.mobile.studentmanagement.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import ma.ensa.mobile.studentmanagement.data.local.dao.GradeDao;
import ma.ensa.mobile.studentmanagement.data.local.database.AppDatabase;
import ma.ensa.mobile.studentmanagement.data.local.entity.Grade;

/**
 * B2: Grade Repository
 * Manages grade data access
 */
public class GradeRepository {

    private GradeDao gradeDao;

    public GradeRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        gradeDao = database.gradeDao();
    }

    /**
     * Get all grades for a student
     */
    public LiveData<List<Grade>> getGradesByStudent(int studentId) {
        return gradeDao.getGradesByStudent(studentId);
    }

    /**
     * Get grades by student and semester
     */
    public LiveData<List<Grade>> getGradesByStudentAndSemester(int studentId, String semester) {
        return gradeDao.getGradesByStudentAndSemester(studentId, semester);
    }

    /**
     * Get grades by student and academic year
     */
    public LiveData<List<Grade>> getGradesByStudentAndYear(int studentId, String academicYear) {
        return gradeDao.getGradesByStudentAndYear(studentId, academicYear);
    }

    /**
     * Get grades by module
     */
    public LiveData<List<Grade>> getGradesByModule(int studentId, String moduleCode) {
        return gradeDao.getGradesByModule(studentId, moduleCode);
    }

    /**
     * Get average grade for a student
     */
    public LiveData<Double> getAverageGrade(int studentId) {
        return gradeDao.getAverageGrade(studentId);
    }

    /**
     * Get average grade by semester
     */
    public LiveData<Double> getAverageGradeBySemester(int studentId, String semester) {
        return gradeDao.getAverageGradeBySemester(studentId, semester);
    }

    /**
     * Get weighted average by semester
     */
    public LiveData<Double> getWeightedAverageBySemester(int studentId, String semester) {
        return gradeDao.getWeightedAverageBySemester(studentId, semester);
    }

    /**
     * Get total credits earned
     */
    public LiveData<Integer> getTotalCreditsEarned(int studentId) {
        return gradeDao.getTotalCreditsEarned(studentId);
    }

    /**
     * Get grade count by semester
     */
    public LiveData<Integer> getGradeCountBySemester(int studentId, String semester) {
        return gradeDao.getGradeCountBySemester(studentId, semester);
    }

    /**
     * Get failed grades
     */
    public LiveData<List<Grade>> getFailedGrades(int studentId) {
        return gradeDao.getFailedGrades(studentId);
    }
}
