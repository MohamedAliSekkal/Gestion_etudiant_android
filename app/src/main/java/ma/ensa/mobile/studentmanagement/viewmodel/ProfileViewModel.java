package ma.ensa.mobile.studentmanagement.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ma.ensa.mobile.studentmanagement.data.local.entity.Student;
import ma.ensa.mobile.studentmanagement.data.repository.StudentRepository;

/**
 * ViewModel for Student Profile (B3)
 * Manages student profile data for display
 */
public class ProfileViewModel extends AndroidViewModel {

    private StudentRepository studentRepository;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        studentRepository = new StudentRepository(application);
    }

    /**
     * Get student by username
     * Used for student profile viewing
     */
    public LiveData<Student> getStudentByUsername(String username) {
        return studentRepository.getStudentByUsername(username);
    }

    /**
     * Get student by ID
     * Fallback method if needed
     */
    public LiveData<Student> getStudentById(int studentId) {
        return studentRepository.getStudentById(studentId);
    }
}
