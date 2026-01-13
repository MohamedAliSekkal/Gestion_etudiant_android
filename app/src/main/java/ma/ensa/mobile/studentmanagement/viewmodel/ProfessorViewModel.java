package ma.ensa.mobile.studentmanagement.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ma.ensa.mobile.studentmanagement.data.local.entity.Absence;
import ma.ensa.mobile.studentmanagement.data.local.entity.Grade;
import ma.ensa.mobile.studentmanagement.data.local.entity.Module;
import ma.ensa.mobile.studentmanagement.data.local.entity.Professor;
import ma.ensa.mobile.studentmanagement.data.local.entity.Student;
import ma.ensa.mobile.studentmanagement.data.repository.AbsenceRepository;
import ma.ensa.mobile.studentmanagement.data.repository.GradeRepository;
import ma.ensa.mobile.studentmanagement.data.repository.ModuleRepository;
import ma.ensa.mobile.studentmanagement.data.repository.ProfessorRepository;
import ma.ensa.mobile.studentmanagement.data.repository.StudentRepository;

public class ProfessorViewModel extends AndroidViewModel {

    private ProfessorRepository professorRepository;
    private ModuleRepository moduleRepository;
    private GradeRepository gradeRepository;
    private StudentRepository studentRepository;
    private AbsenceRepository absenceRepository;

    public ProfessorViewModel(@NonNull Application application) {
        super(application);
        professorRepository = new ProfessorRepository(application);
        moduleRepository = new ModuleRepository(application);
        gradeRepository = new GradeRepository(application);
        studentRepository = new StudentRepository(application);
        absenceRepository = new AbsenceRepository(application);
    }

    // Professor operations
    public LiveData<Professor> getProfessorByUserId(int userId) {
        return professorRepository.getProfessorByUserId(userId);
    }

    public Professor getProfessorByUserIdSync(int userId) {
        return professorRepository.getProfessorByUserIdSync(userId);
    }

    public LiveData<List<Professor>> getAllActiveProfessors() {
        return professorRepository.getAllActiveProfessors();
    }

    // Module operations
    public LiveData<List<Module>> getModulesByProfessor(int professorId) {
        return moduleRepository.getModulesByProfessor(professorId);
    }

    public LiveData<Module> getModuleById(int moduleId) {
        return moduleRepository.getModuleById(moduleId);
    }

    public LiveData<Integer> getModuleCountByProfessor(int professorId) {
        return moduleRepository.getModuleCountByProfessor(professorId);
    }

    // Grade operations
    public void insertGrade(Grade grade) {
        gradeRepository.insertGrade(grade);
    }

    public void updateGrade(Grade grade) {
        gradeRepository.updateGrade(grade);
    }

    public LiveData<List<Grade>> getGradesByModuleOnly(int moduleId) {
        return gradeRepository.getGradesByModuleOnly(moduleId);
    }

    public LiveData<List<Grade>> getStudentGradesInModule(int studentId, int moduleId) {
        return gradeRepository.getStudentGradesInModule(studentId, moduleId);
    }

    // Student operations
    public LiveData<List<Student>> getStudentsByModule(int moduleId) {
        return studentRepository.getStudentsByModule(moduleId);
    }

    // Absence operations
    public void insertAbsence(Absence absence) {
        absenceRepository.insertAbsence(absence);
    }

    public LiveData<List<Absence>> getAbsencesByStudent(int studentId) {
        return absenceRepository.getAbsencesByStudent(studentId);
    }
}
