package ma.ensa.mobile.studentmanagement.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ma.ensa.mobile.studentmanagement.data.local.entity.StudentModule;

@Dao
public interface StudentModuleDao {

    @Insert
    long insert(StudentModule studentModule);

    @Update
    void update(StudentModule studentModule);

    @Delete
    void delete(StudentModule studentModule);

    @Query("SELECT * FROM student_modules WHERE enrollment_id = :enrollmentId")
    LiveData<StudentModule> getEnrollmentById(int enrollmentId);

    @Query("SELECT * FROM student_modules WHERE student_id = :studentId AND is_active = 1")
    LiveData<List<StudentModule>> getModulesByStudent(int studentId);

    @Query("SELECT * FROM student_modules WHERE module_id = :moduleId AND is_active = 1")
    LiveData<List<StudentModule>> getStudentsByModule(int moduleId);

    @Query("SELECT * FROM student_modules WHERE student_id = :studentId AND module_id = :moduleId")
    LiveData<StudentModule> getEnrollment(int studentId, int moduleId);

    @Query("SELECT * FROM student_modules WHERE student_id = :studentId AND module_id = :moduleId")
    StudentModule getEnrollmentSync(int studentId, int moduleId);

    @Query("SELECT COUNT(*) FROM student_modules WHERE student_id = :studentId AND is_active = 1")
    LiveData<Integer> getEnrollmentCountByStudent(int studentId);

    @Query("SELECT COUNT(*) FROM student_modules WHERE module_id = :moduleId AND is_active = 1")
    LiveData<Integer> getEnrollmentCountByModule(int moduleId);

    @Query("UPDATE student_modules SET is_active = 0, updated_at = :timestamp WHERE enrollment_id = :enrollmentId")
    void deactivateEnrollment(int enrollmentId, long timestamp);

    @Query("UPDATE student_modules SET is_active = 1, updated_at = :timestamp WHERE enrollment_id = :enrollmentId")
    void reactivateEnrollment(int enrollmentId, long timestamp);
}
