package ma.ensa.mobile.studentmanagement.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ma.ensa.mobile.studentmanagement.data.local.dao.BranchDao;
import ma.ensa.mobile.studentmanagement.data.local.dao.LevelDao;
import ma.ensa.mobile.studentmanagement.data.local.dao.StudentDao;
import ma.ensa.mobile.studentmanagement.data.local.database.AppDatabase;
import ma.ensa.mobile.studentmanagement.data.local.entity.Branch;
import ma.ensa.mobile.studentmanagement.data.local.entity.Level;

/**
 * ViewModel for Dashboard statistics (A3)
 * Provides LiveData for real-time statistics display
 */
public class DashboardViewModel extends AndroidViewModel {

    private StudentDao studentDao;
    private BranchDao branchDao;
    private LevelDao levelDao;

    // Statistics LiveData
    private LiveData<Integer> activeStudentCount;
    private LiveData<Integer> archivedStudentCount;
    private LiveData<List<Branch>> allBranches;
    private LiveData<List<Level>> allLevels;

    public DashboardViewModel(@NonNull Application application) {
        super(application);

        AppDatabase database = AppDatabase.getInstance(application);
        studentDao = database.studentDao();
        branchDao = database.branchDao();
        levelDao = database.levelDao();

        // Initialize LiveData
        activeStudentCount = studentDao.getActiveStudentCountLive();
        archivedStudentCount = studentDao.getArchivedStudentCountLive();
        allBranches = branchDao.getAllActiveBranches();
        allLevels = levelDao.getAllActiveLevels();
    }

    /**
     * Get count of active students
     */
    public LiveData<Integer> getActiveStudentCount() {
        return activeStudentCount;
    }

    /**
     * Get count of archived students
     */
    public LiveData<Integer> getArchivedStudentCount() {
        return archivedStudentCount;
    }

    /**
     * Get all active branches
     */
    public LiveData<List<Branch>> getAllBranches() {
        return allBranches;
    }

    /**
     * Get all active levels
     */
    public LiveData<List<Level>> getAllLevels() {
        return allLevels;
    }

    /**
     * Get total branch count
     */
    public int getTotalBranchCount() {
        return branchDao.getActiveBranchCount();
    }

    /**
     * Get total level count
     */
    public int getTotalLevelCount() {
        return levelDao.getActiveLevelCount();
    }
}
