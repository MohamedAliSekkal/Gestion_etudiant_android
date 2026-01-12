package ma.ensa.mobile.studentmanagement.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import ma.ensa.mobile.studentmanagement.data.local.dao.StudentModuleDao;
import ma.ensa.mobile.studentmanagement.data.local.database.AppDatabase;
import ma.ensa.mobile.studentmanagement.data.local.entity.StudentModule;

public class StudentModuleRepository {

    private StudentModuleDao studentModuleDao;

    public StudentModuleRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        studentModuleDao = database.studentModuleDao();
    }

    public void insert(StudentModule studentModule) {
        AppDatabase.databaseWriteExecutor.execute(() -> studentModuleDao.insert(studentModule));
    }

    public void update(StudentModule studentModule) {
        AppDatabase.databaseWriteExecutor.execute(() -> studentModuleDao.update(studentModule));
    }

    public void delete(StudentModule studentModule) {
        AppDatabase.databaseWriteExecutor.execute(() -> studentModuleDao.delete(studentModule));
    }

    public LiveData<StudentModule> getEnrollmentById(int enrollmentId) {
        return studentModuleDao.getEnrollmentById(enrollmentId);
    }

    public LiveData<List<StudentModule>> getModulesByStudent(int studentId) {
        return studentModuleDao.getModulesByStudent(studentId);
    }

    public LiveData<List<StudentModule>> getStudentsByModule(int moduleId) {
        return studentModuleDao.getStudentsByModule(moduleId);
    }

    public LiveData<StudentModule> getEnrollment(int studentId, int moduleId) {
        return studentModuleDao.getEnrollment(studentId, moduleId);
    }

    public LiveData<Integer> getEnrollmentCountByStudent(int studentId) {
        return studentModuleDao.getEnrollmentCountByStudent(studentId);
    }

    public LiveData<Integer> getEnrollmentCountByModule(int moduleId) {
        return studentModuleDao.getEnrollmentCountByModule(moduleId);
    }

    public void deactivateEnrollment(int enrollmentId) {
        long timestamp = System.currentTimeMillis() / 1000;
        AppDatabase.databaseWriteExecutor.execute(() ->
            studentModuleDao.deactivateEnrollment(enrollmentId, timestamp));
    }

    public void reactivateEnrollment(int enrollmentId) {
        long timestamp = System.currentTimeMillis() / 1000;
        AppDatabase.databaseWriteExecutor.execute(() ->
            studentModuleDao.reactivateEnrollment(enrollmentId, timestamp));
    }
}
