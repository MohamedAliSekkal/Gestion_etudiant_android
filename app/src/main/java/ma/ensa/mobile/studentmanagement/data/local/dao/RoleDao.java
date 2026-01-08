package ma.ensa.mobile.studentmanagement.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ma.ensa.mobile.studentmanagement.data.local.entity.Role;

@Dao
public interface RoleDao {

    @Insert
    long insertRole(Role role);

    @Query("SELECT * FROM roles WHERE role_id = :roleId")
    LiveData<Role> getRoleById(int roleId);

    @Query("SELECT * FROM roles WHERE role_code = :roleCode LIMIT 1")
    Role getRoleByCode(String roleCode);

    @Query("SELECT * FROM roles WHERE is_active = 1 ORDER BY priority_level ASC")
    LiveData<List<Role>> getAllActiveRoles();

    @Query("SELECT COUNT(*) FROM roles")
    int getRoleCount();
}