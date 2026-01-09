package ma.ensa.mobile.studentmanagement.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ma.ensa.mobile.studentmanagement.data.local.entity.Student;
import ma.ensa.mobile.studentmanagement.data.repository.StudentRepository;

public class StudentViewModel extends AndroidViewModel {

    private static final String TAG = "StudentViewModel";

    private StudentRepository repository;
    private LiveData<List<Student>> allActiveStudents;
    private MutableLiveData<Student> selectedStudent;
    private MutableLiveData<Long> operationResult;
    private MutableLiveData<String> operationError;
    private MutableLiveData<Boolean> isLoading;
    private LiveData<List<Student>> searchResults;

    private ExecutorService executorService;

    public StudentViewModel(@NonNull Application application) {
        super(application);
        repository = new StudentRepository(application);
        allActiveStudents = repository.getAllActiveStudents();
        selectedStudent = new MutableLiveData<>();
        operationResult = new MutableLiveData<>();
        operationError = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(false);
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Create a new student
     */
    public void createStudent(Student student) {
        isLoading.setValue(true);

        executorService.execute(() -> {
            try {
                // Validate student
                if (!validateStudent(student)) {
                    operationError.postValue("Veuillez remplir tous les champs obligatoires");
                    operationResult.postValue(-4L);
                    isLoading.postValue(false);
                    return;
                }

                long result = repository.insert(student);

                operationResult.postValue(result);

                if (result == -1) {
                    operationError.postValue("Ce numéro Apogée existe déjà");
                } else if (result == -2) {
                    operationError.postValue("Ce CNE existe déjà");
                } else if (result == -3) {
                    operationError.postValue("Cet email est déjà utilisé");
                } else if (result == -4) {
                    operationError.postValue("Erreur lors de la création");
                } else if (result > 0) {
                    operationError.postValue(null);
                    Log.i(TAG, "Student created successfully with ID: " + result);
                }

            } catch (Exception e) {
                Log.e(TAG, "Error creating student", e);
                operationError.postValue("Erreur lors de la création");
                operationResult.postValue(-4L);
            } finally {
                isLoading.postValue(false);
            }
        });
    }

    /**
     * Update an existing student
     */
    public void updateStudent(Student student) {
        if (!validateStudent(student)) {
            operationError.setValue("Veuillez remplir tous les champs obligatoires");
            return;
        }

        repository.update(student);
        operationError.setValue(null);
        Log.i(TAG, "Student update initiated");
    }

    /**
     * Archive a student
     */
    public void archiveStudent(int studentId) {
        repository.archive(studentId);
        Log.i(TAG, "Student archive initiated for ID: " + studentId);
    }

    /**
     * Restore an archived student
     */
    public void restoreStudent(int studentId) {
        repository.restore(studentId);
        Log.i(TAG, "Student restore initiated for ID: " + studentId);
    }

    /**
     * Delete a student permanently
     */
    public void deleteStudent(Student student) {
        repository.delete(student);
        Log.i(TAG, "Student deletion initiated");
    }

    /**
     * Select a student for detail view
     */
    public void selectStudent(int studentId) {
        LiveData<Student> student = repository.getStudentById(studentId);
        // The fragment will observe this LiveData directly
    }

    /**
     * Get a student by ID
     */
    public LiveData<Student> getStudentById(int studentId) {
        return repository.getStudentById(studentId);
    }

    /**
     * Search by Apogee number (BF4)
     */
    public LiveData<Student> searchByApogee(String apogeeNumber) {
        return repository.searchByApogee(apogeeNumber);
    }

    /**
     * Search students by query
     */
    public LiveData<List<Student>> searchStudents(String query) {
        return repository.searchStudents(query);
    }

    /**
     * Get all active students
     */
    public LiveData<List<Student>> getAllActiveStudents() {
        return allActiveStudents;
    }

    /**
     * Get all archived students
     */
    public LiveData<List<Student>> getAllArchivedStudents() {
        return repository.getAllArchivedStudents();
    }

    /**
     * Get students by status
     */
    public LiveData<List<Student>> getStudentsByStatus(String status) {
        return repository.getStudentsByStatus(status);
    }

    /**
     * Validate student data
     */
    private boolean validateStudent(Student student) {
        return student != null
                && student.getApogeeNumber() != null && !student.getApogeeNumber().isEmpty()
                && student.getCne() != null && !student.getCne().isEmpty()
                && student.getFirstName() != null && !student.getFirstName().isEmpty()
                && student.getLastName() != null && !student.getLastName().isEmpty()
                && student.getEmail() != null && !student.getEmail().isEmpty();
    }

    // Getters for LiveData observables
    public LiveData<Student> getSelectedStudent() {
        return selectedStudent;
    }

    public LiveData<Long> getOperationResult() {
        return operationResult;
    }

    public LiveData<String> getOperationError() {
        return operationError;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}
