package ma.ensa.mobile.studentmanagement.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ma.ensa.mobile.studentmanagement.data.local.entity.Professor;

@Dao
public interface ProfessorDao {

    @Insert
    long insert(Professor professor);

    @Update
    void update(Professor professor);

    @Delete
    void delete(Professor professor);

    @Query("SELECT * FROM professors WHERE professor_id = :professorId")
    LiveData<Professor> getProfessorById(int professorId);

    @Query("SELECT * FROM professors WHERE professor_id = :professorId")
    Professor getProfessorByIdSync(int professorId);

    @Query("SELECT * FROM professors WHERE user_id = :userId")
    LiveData<Professor> getProfessorByUserId(int userId);

    @Query("SELECT * FROM professors WHERE user_id = :userId")
    Professor getProfessorByUserIdSync(int userId);

    @Query("SELECT * FROM professors WHERE employee_number = :employeeNumber")
    LiveData<Professor> getProfessorByEmployeeNumber(String employeeNumber);

    @Query("SELECT * FROM professors WHERE is_active = 1 ORDER BY last_name, first_name")
    LiveData<List<Professor>> getAllActiveProfessors();

    @Query("SELECT * FROM professors WHERE department = :department AND is_active = 1 ORDER BY last_name, first_name")
    LiveData<List<Professor>> getProfessorsByDepartment(String department);

    @Query("SELECT COUNT(*) FROM professors WHERE is_active = 1")
    LiveData<Integer> getActiveProfessorCount();

    @Query("SELECT DISTINCT department FROM professors WHERE department IS NOT NULL AND is_active = 1 ORDER BY department")
    LiveData<List<String>> getAllDepartments();
}
