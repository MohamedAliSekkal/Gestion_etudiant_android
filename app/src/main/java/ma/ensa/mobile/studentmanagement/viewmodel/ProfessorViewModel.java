package ma.ensa.mobile.studentmanagement.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ma.ensa.mobile.studentmanagement.data.local.entity.Grade;
import ma.ensa.mobile.studentmanagement.data.local.entity.Module;
import ma.ensa.mobile.studentmanagement.data.local.entity.Professor;
import ma.ensa.mobile.studentmanagement.data.repository.GradeRepository;
import ma.ensa.mobile.studentmanagement.data.repository.ModuleRepository;
import ma.ensa.mobile.studentmanagement.data.repository.ProfessorRepository;

public class ProfessorViewModel extends AndroidViewModel {

    private ProfessorRepository professorRepository;
    private ModuleRepository moduleRepository;
    private GradeRepository gradeRepository;

    public ProfessorViewModel(@NonNull Application application) {
        super(application);
        professorRepository = new ProfessorRepository(application);
        moduleRepository = new ModuleRepository(application);
        gradeRepository = new GradeRepository(application);
    }

    // Professor operations
    public LiveData<Professor> getProfessorByUserId(int userId) {
        return professorRepository.getProfessorByUserId(userId);
    }

    public Professor getProfessorByUserIdSync(int userId) {
        return professorRepository.getProfessorByUserIdSync(userId);
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
}
