package ma.ensa.mobile.studentmanagement.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ma.ensa.mobile.studentmanagement.data.local.entity.Absence;
import ma.ensa.mobile.studentmanagement.data.repository.AbsenceRepository;

/**
 * B1: Absence ViewModel
 * Manages UI-related data for absences
 */
public class AbsenceViewModel extends AndroidViewModel {

    private AbsenceRepository absenceRepository;

    public AbsenceViewModel(@NonNull Application application) {
        super(application);
        absenceRepository = new AbsenceRepository(application);
    }

    /**
     * Get all absences for a student
     */
    public LiveData<List<Absence>> getAbsencesByStudent(int studentId) {
        return absenceRepository.getAbsencesByStudent(studentId);
    }

    /**
     * Get absences by student and semester
     */
    public LiveData<List<Absence>> getAbsencesByStudentAndSemester(int studentId, String semester) {
        return absenceRepository.getAbsencesByStudentAndSemester(studentId, semester);
    }

    /**
     * Get absences by student and academic year
     */
    public LiveData<List<Absence>> getAbsencesByStudentAndYear(int studentId, String academicYear) {
        return absenceRepository.getAbsencesByStudentAndYear(studentId, academicYear);
    }

    /**
     * Get unjustified absences count
     */
    public LiveData<Integer> getUnjustifiedAbsencesCount(int studentId) {
        return absenceRepository.getUnjustifiedAbsencesCount(studentId);
    }

    /**
     * Get total absence hours
     */
    public LiveData<Integer> getTotalAbsenceHours(int studentId) {
        return absenceRepository.getTotalAbsenceHours(studentId);
    }

    /**
     * Get unjustified absence hours
     */
    public LiveData<Integer> getUnjustifiedAbsenceHours(int studentId) {
        return absenceRepository.getUnjustifiedAbsenceHours(studentId);
    }

    /**
     * Get absences by module
     */
    public LiveData<List<Absence>> getAbsencesByModule(int studentId, String moduleCode) {
        return absenceRepository.getAbsencesByModule(studentId, moduleCode);
    }

    /**
     * Get absence count by semester
     */
    public LiveData<Integer> getAbsenceCountBySemester(int studentId, String semester) {
        return absenceRepository.getAbsenceCountBySemester(studentId, semester);
    }
}
