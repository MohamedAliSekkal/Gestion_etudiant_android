package ma.ensa.mobile.studentmanagement.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ma.ensa.mobile.studentmanagement.data.local.entity.Branch;

@Dao
public interface BranchDao {

    @Insert
    long insertBranch(Branch branch);

    @Update
    void updateBranch(Branch branch);

    @Query("SELECT * FROM branches WHERE is_active = 1 ORDER BY branch_name ASC")
    LiveData<List<Branch>> getAllActiveBranches();

    @Query("SELECT * FROM branches ORDER BY branch_name ASC")
    LiveData<List<Branch>> getAllBranches();

    @Query("SELECT * FROM branches WHERE branch_id = :branchId")
    LiveData<Branch> getBranchById(int branchId);

    @Query("SELECT * FROM branches WHERE branch_code = :branchCode")
    LiveData<Branch> getBranchByCode(String branchCode);

    @Query("SELECT * FROM branches WHERE cycle = :cycle AND is_active = 1")
    LiveData<List<Branch>> getBranchesByCycle(String cycle);

    @Query("SELECT COUNT(*) FROM branches WHERE is_active = 1")
    int getActiveBranchCount();
}
