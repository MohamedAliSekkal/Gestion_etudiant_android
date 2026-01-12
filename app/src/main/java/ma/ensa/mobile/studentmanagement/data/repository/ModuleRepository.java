package ma.ensa.mobile.studentmanagement.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import ma.ensa.mobile.studentmanagement.data.local.dao.ModuleDao;
import ma.ensa.mobile.studentmanagement.data.local.database.AppDatabase;
import ma.ensa.mobile.studentmanagement.data.local.entity.Module;

public class ModuleRepository {

    private ModuleDao moduleDao;

    public ModuleRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        moduleDao = database.moduleDao();
    }

    public void insert(Module module) {
        AppDatabase.databaseWriteExecutor.execute(() -> moduleDao.insert(module));
    }

    public void update(Module module) {
        AppDatabase.databaseWriteExecutor.execute(() -> moduleDao.update(module));
    }

    public void delete(Module module) {
        AppDatabase.databaseWriteExecutor.execute(() -> moduleDao.delete(module));
    }

    public LiveData<Module> getModuleById(int moduleId) {
        return moduleDao.getModuleById(moduleId);
    }

    public LiveData<Module> getModuleByCode(String moduleCode) {
        return moduleDao.getModuleByCode(moduleCode);
    }

    public LiveData<List<Module>> getAllActiveModules() {
        return moduleDao.getAllActiveModules();
    }

    public LiveData<List<Module>> getModulesByProfessor(int professorId) {
        return moduleDao.getModulesByProfessor(professorId);
    }

    public LiveData<List<Module>> getModulesByBranch(int branchId) {
        return moduleDao.getModulesByBranch(branchId);
    }

    public LiveData<List<Module>> getModulesByLevel(int levelId) {
        return moduleDao.getModulesByLevel(levelId);
    }

    public LiveData<List<Module>> getModulesBySemester(String semester, String academicYear) {
        return moduleDao.getModulesBySemester(semester, academicYear);
    }

    public LiveData<List<Module>> getModulesByBranchLevelSemester(int branchId, int levelId, String semester) {
        return moduleDao.getModulesByBranchLevelSemester(branchId, levelId, semester);
    }

    public LiveData<Integer> getModuleCountByProfessor(int professorId) {
        return moduleDao.getModuleCountByProfessor(professorId);
    }

    public LiveData<List<Module>> getUnassignedModules() {
        return moduleDao.getUnassignedModules();
    }

    public void assignProfessor(int moduleId, int professorId) {
        long timestamp = System.currentTimeMillis() / 1000;
        AppDatabase.databaseWriteExecutor.execute(() ->
            moduleDao.assignProfessor(moduleId, professorId, timestamp));
    }

    public void unassignProfessor(int moduleId) {
        long timestamp = System.currentTimeMillis() / 1000;
        AppDatabase.databaseWriteExecutor.execute(() ->
            moduleDao.unassignProfessor(moduleId, timestamp));
    }
}
