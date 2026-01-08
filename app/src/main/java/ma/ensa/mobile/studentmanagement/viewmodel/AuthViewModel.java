package ma.ensa.mobile.studentmanagement.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ma.ensa.mobile.studentmanagement.data.local.database.AppDatabase;
import ma.ensa.mobile.studentmanagement.data.local.entity.User;
import ma.ensa.mobile.studentmanagement.data.repository.UserRepository;

public class AuthViewModel extends AndroidViewModel {

    private static final String TAG = "AuthViewModel";

    private UserRepository userRepository;
    private MutableLiveData<User> authenticatedUser;
    private MutableLiveData<String> authenticationError;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<Long> registrationResult;
    private MutableLiveData<String> registrationError;

    private ExecutorService executorService;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        authenticatedUser = new MutableLiveData<>();
        authenticationError = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(false);
        registrationResult = new MutableLiveData<>();
        registrationError = new MutableLiveData<>();
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Effectue l'authentification
     */
    public void login(String username, String password) {
        isLoading.setValue(true);

        executorService.execute(() -> {
            try {
                User user = userRepository.authenticate(username, password);

                // Mettre à jour sur le Main Thread
                authenticatedUser.postValue(user);

                if (user == null) {
                    authenticationError.postValue("Nom d'utilisateur ou mot de passe incorrect");
                } else {
                    authenticationError.postValue(null);
                }

            } catch (Exception e) {
                Log.e(TAG, "Login error", e);
                authenticationError.postValue("Erreur lors de la connexion");
                authenticatedUser.postValue(null);
            } finally {
                isLoading.postValue(false);
            }
        });
    }

    /**
     * Enregistre un nouvel utilisateur
     */
    public void register(String username, String email, String password, String fullName, String phone) {
        isLoading.setValue(true);

        executorService.execute(() -> {
            try {
                // Créer l'objet User
                User user = new User();
                user.setUsername(username);
                user.setEmail(email);
                user.setPasswordHash(password); // Sera hashé dans le Repository
                user.setFullName(fullName);
                user.setPhone(phone);
                user.setRoleId(5); // Role Étudiant par défaut

                // Enregistrer l'utilisateur
                long result = userRepository.registerUser(user);

                // Mettre à jour sur le Main Thread
                registrationResult.postValue(result);

                if (result == -1) {
                    registrationError.postValue("Ce nom d'utilisateur existe déjà");
                } else if (result == -2) {
                    registrationError.postValue("Cet email est déjà utilisé");
                } else if (result == -3) {
                    registrationError.postValue("Erreur lors de l'inscription");
                } else if (result > 0) {
                    registrationError.postValue(null);
                    Log.i(TAG, "User registered successfully with ID: " + result);
                }

            } catch (Exception e) {
                Log.e(TAG, "Registration error", e);
                registrationError.postValue("Erreur lors de l'inscription");
                registrationResult.postValue(-3L);
            } finally {
                isLoading.postValue(false);
            }
        });
    }

    // Getters pour LiveData observables
    public LiveData<User> getAuthenticatedUser() {
        return authenticatedUser;
    }

    public LiveData<String> getAuthenticationError() {
        return authenticationError;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Long> getRegistrationResult() {
        return registrationResult;
    }

    public LiveData<String> getRegistrationError() {
        return registrationError;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}