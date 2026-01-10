package ma.ensa.mobile.studentmanagement.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ma.ensa.mobile.studentmanagement.data.local.entity.Level;

@Dao
public interface LevelDao {

    @Insert
    long insertLevel(Level level);

    @Update
    void updateLevel(Level level);

    @Query("SELECT * FROM levels WHERE is_active = 1 ORDER BY year_number ASC")
    LiveData<List<Level>> getAllActiveLevels();

    @Query("SELECT * FROM levels ORDER BY year_number ASC")
    LiveData<List<Level>> getAllLevels();

    @Query("SELECT * FROM levels WHERE level_id = :levelId")
    LiveData<Level> getLevelById(int levelId);

    @Query("SELECT * FROM levels WHERE level_code = :levelCode")
    LiveData<Level> getLevelByCode(String levelCode);

    @Query("SELECT * FROM levels WHERE cycle = :cycle AND is_active = 1")
    LiveData<List<Level>> getLevelsByCycle(String cycle);

    @Query("SELECT COUNT(*) FROM levels WHERE is_active = 1")
    int getActiveLevelCount();
}
