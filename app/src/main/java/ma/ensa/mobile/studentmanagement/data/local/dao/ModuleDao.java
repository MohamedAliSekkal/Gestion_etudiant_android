package ma.ensa.mobile.studentmanagement.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ma.ensa.mobile.studentmanagement.data.local.entity.Module;

@Dao
public interface ModuleDao {

    @Insert
    long insert(Module module);

    @Update
    void update(Module module);

    @Delete
    void delete(Module module);

    @Query("SELECT * FROM modules WHERE module_id = :moduleId")
    LiveData<Module> getModuleById(int moduleId);

    @Query("SELECT * FROM modules WHERE module_id = :moduleId")
    Module getModuleByIdSync(int moduleId);

    @Query("SELECT * FROM modules WHERE module_code = :moduleCode")
    LiveData<Module> getModuleByCode(String moduleCode);

    @Query("SELECT * FROM modules WHERE is_active = 1 ORDER BY module_code ASC")
    LiveData<List<Module>> getAllActiveModules();

    @Query("SELECT * FROM modules WHERE professor_id = :professorId AND is_active = 1 ORDER BY semester, module_code")
    LiveData<List<Module>> getModulesByProfessor(int professorId);

    @Query("SELECT * FROM modules WHERE branch_id = :branchId AND is_active = 1")
    LiveData<List<Module>> getModulesByBranch(int branchId);

    @Query("SELECT * FROM modules WHERE level_id = :levelId AND is_active = 1")
    LiveData<List<Module>> getModulesByLevel(int levelId);

    @Query("SELECT * FROM modules WHERE semester = :semester AND academic_year = :academicYear AND is_active = 1")
    LiveData<List<Module>> getModulesBySemester(String semester, String academicYear);

    @Query("SELECT * FROM modules WHERE branch_id = :branchId AND level_id = :levelId AND semester = :semester AND is_active = 1")
    LiveData<List<Module>> getModulesByBranchLevelSemester(int branchId, int levelId, String semester);

    @Query("SELECT COUNT(*) FROM modules WHERE professor_id = :professorId AND is_active = 1")
    LiveData<Integer> getModuleCountByProfessor(int professorId);

    @Query("SELECT * FROM modules WHERE professor_id IS NULL AND is_active = 1")
    LiveData<List<Module>> getUnassignedModules();

    @Query("UPDATE modules SET professor_id = :professorId, updated_at = :timestamp WHERE module_id = :moduleId")
    void assignProfessor(int moduleId, int professorId, long timestamp);

    @Query("UPDATE modules SET professor_id = NULL, updated_at = :timestamp WHERE module_id = :moduleId")
    void unassignProfessor(int moduleId, long timestamp);
}
