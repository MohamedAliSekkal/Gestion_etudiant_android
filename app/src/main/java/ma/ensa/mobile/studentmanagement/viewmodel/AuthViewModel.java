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

    private ExecutorService executorService;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        authenticatedUser = new MutableLiveData<>();
        authenticationError = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(false);
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

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}