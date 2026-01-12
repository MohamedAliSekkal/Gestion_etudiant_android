package ma.ensa.mobile.studentmanagement.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ma.ensa.mobile.studentmanagement.data.local.entity.Grade;
import ma.ensa.mobile.studentmanagement.data.repository.GradeRepository;

/**
 * B2: Academic ViewModel
 * Manages UI-related data for academic records (grades)
 */
public class AcademicViewModel extends AndroidViewModel {

    private GradeRepository gradeRepository;

    public AcademicViewModel(@NonNull Application application) {
        super(application);
        gradeRepository = new GradeRepository(application);
    }

    /**
     * Get all grades for a student
     */
    public LiveData<List<Grade>> getGradesByStudent(int studentId) {
        return gradeRepository.getGradesByStudent(studentId);
    }

    /**
     * Get grades by student and semester
     */
    public LiveData<List<Grade>> getGradesByStudentAndSemester(int studentId, String semester) {
        return gradeRepository.getGradesByStudentAndSemester(studentId, semester);
    }

    /**
     * Get grades by student and academic year
     */
    public LiveData<List<Grade>> getGradesByStudentAndYear(int studentId, String academicYear) {
        return gradeRepository.getGradesByStudentAndYear(studentId, academicYear);
    }

    /**
     * Get grades by module
     */
    public LiveData<List<Grade>> getGradesByModule(int studentId, String moduleCode) {
        return gradeRepository.getGradesByModule(studentId, moduleCode);
    }

    /**
     * Get average grade for a student
     */
    public LiveData<Double> getAverageGrade(int studentId) {
        return gradeRepository.getAverageGrade(studentId);
    }

    /**
     * Get average grade by semester
     */
    public LiveData<Double> getAverageGradeBySemester(int studentId, String semester) {
        return gradeRepository.getAverageGradeBySemester(studentId, semester);
    }

    /**
     * Get weighted average by semester
     */
    public LiveData<Double> getWeightedAverageBySemester(int studentId, String semester) {
        return gradeRepository.getWeightedAverageBySemester(studentId, semester);
    }

    /**
     * Get total credits earned
     */
    public LiveData<Integer> getTotalCreditsEarned(int studentId) {
        return gradeRepository.getTotalCreditsEarned(studentId);
    }

    /**
     * Get grade count by semester
     */
    public LiveData<Integer> getGradeCountBySemester(int studentId, String semester) {
        return gradeRepository.getGradeCountBySemester(studentId, semester);
    }

    /**
     * Get failed grades
     */
    public LiveData<List<Grade>> getFailedGrades(int studentId) {
        return gradeRepository.getFailedGrades(studentId);
    }
}
