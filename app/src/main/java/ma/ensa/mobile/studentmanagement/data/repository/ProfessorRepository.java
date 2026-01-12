package ma.ensa.mobile.studentmanagement.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import ma.ensa.mobile.studentmanagement.data.local.dao.ProfessorDao;
import ma.ensa.mobile.studentmanagement.data.local.database.AppDatabase;
import ma.ensa.mobile.studentmanagement.data.local.entity.Professor;

public class ProfessorRepository {

    private ProfessorDao professorDao;

    public ProfessorRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        professorDao = database.professorDao();
    }

    public void insert(Professor professor) {
        AppDatabase.databaseWriteExecutor.execute(() -> professorDao.insert(professor));
    }

    public void update(Professor professor) {
        AppDatabase.databaseWriteExecutor.execute(() -> professorDao.update(professor));
    }

    public void delete(Professor professor) {
        AppDatabase.databaseWriteExecutor.execute(() -> professorDao.delete(professor));
    }

    public LiveData<Professor> getProfessorById(int professorId) {
        return professorDao.getProfessorById(professorId);
    }

    public LiveData<Professor> getProfessorByUserId(int userId) {
        return professorDao.getProfessorByUserId(userId);
    }

    public Professor getProfessorByUserIdSync(int userId) {
        return professorDao.getProfessorByUserIdSync(userId);
    }

    public LiveData<Professor> getProfessorByEmployeeNumber(String employeeNumber) {
        return professorDao.getProfessorByEmployeeNumber(employeeNumber);
    }

    public LiveData<List<Professor>> getAllActiveProfessors() {
        return professorDao.getAllActiveProfessors();
    }

    public LiveData<List<Professor>> getProfessorsByDepartment(String department) {
        return professorDao.getProfessorsByDepartment(department);
    }

    public LiveData<Integer> getActiveProfessorCount() {
        return professorDao.getActiveProfessorCount();
    }

    public LiveData<List<String>> getAllDepartments() {
        return professorDao.getAllDepartments();
    }
}
