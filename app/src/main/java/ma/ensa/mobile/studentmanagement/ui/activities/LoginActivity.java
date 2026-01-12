package ma.ensa.mobile.studentmanagement.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import ma.ensa.mobile.studentmanagement.R;
import ma.ensa.mobile.studentmanagement.data.local.entity.User;
import ma.ensa.mobile.studentmanagement.databinding.ActivityLoginBinding;
import ma.ensa.mobile.studentmanagement.utils.PreferencesManager;
import ma.ensa.mobile.studentmanagement.viewmodel.AuthViewModel;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private ActivityLoginBinding binding;
    private AuthViewModel authViewModel;
    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ViewBinding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialiser PreferencesManager
        preferencesManager = new PreferencesManager(this);

        // Vérifier si déjà connecté
        if (preferencesManager.isLoggedIn()) {
            navigateToHome();
            return;
        }

        // Initialiser ViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Observer les LiveData
        setupObservers();

        // Listener bouton connexion
        binding.buttonLogin.setOnClickListener(v -> attemptLogin());

        // Listener lien inscription
        binding.textViewRegister.setOnClickListener(v -> navigateToRegister());
    }

    /**
     * Configure les observateurs LiveData
     */
    private void setupObservers() {
        // Observer l'utilisateur authentifié
        authViewModel.getAuthenticatedUser().observe(this, user -> {
            if (user != null) {
                onLoginSuccess(user);
            }
        });

        // Observer les erreurs
        authViewModel.getAuthenticationError().observe(this, error -> {
            if (error != null) {
                showError(error);
            } else {
                hideError();
            }
        });

        // Observer l'état de chargement
        authViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                showLoading();
            } else {
                hideLoading();
            }
        });
    }

    /**
     * Tente la connexion
     */
    private void attemptLogin() {
        // Réinitialiser les erreurs
        hideError();

        // Récupérer les valeurs
        String username = binding.editTextUsername.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(username)) {
            binding.textInputLayoutUsername.setError("Champ requis");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            binding.textInputLayoutPassword.setError("Champ requis");
            return;
        }

        // Effacer les erreurs
        binding.textInputLayoutUsername.setError(null);
        binding.textInputLayoutPassword.setError(null);

        // Lancer l'authentification
        authViewModel.login(username, password);
    }

    /**
     * Appelé lors du succès de connexion
     */
    private void onLoginSuccess(User user) {
        // Sauvegarder la session
        // Note: roleCode sera récupéré via une requête sur la table roles
        // Pour simplifier, on utilise directement l'ID du rôle
        preferencesManager.saveUserSession(
                user.getUserId(),
                user.getUsername(),
                String.valueOf(user.getRoleId()));

        Toast.makeText(this, "Bienvenue " + user.getFullName(), Toast.LENGTH_SHORT).show();

        // Naviguer vers la page d'accueil
        navigateToHome();
    }

    /**
     * Navigation vers l'écran d'accueil en fonction du rôle
     */
    private void navigateToHome() {
        String roleId = preferencesManager.getRoleCode();
        Intent intent;

        try {
            // Route users based on role
            switch (roleId) {
                case "1": // Admin
                case "2": // Scolarité
                case "3": // APOGÉE
                    // Admin and staff go to AdminHomeActivity with full access
                    intent = new Intent(this, AdminHomeActivity.class);
                    break;

                case "4": // Enseignant
                    // Teachers go to ProfessorHomeActivity
                    intent = new Intent(this, ProfessorHomeActivity.class);
                    break;

                case "5": // Student
                    // Students go to StudentHomeActivity (Person B features not implemented yet)
                    Toast.makeText(this, "Interface étudiant - Fonctionnalités en développement par la Personne B", Toast.LENGTH_LONG).show();
                    intent = new Intent(this, StudentHomeActivity.class);
                    break;

                default:
                    // Fallback for unknown roles
                    Log.w(TAG, "Unknown role: " + roleId + ", falling back to HomeActivity");
                    intent = new Intent(this, HomeActivity.class);
                    break;
            }

            startActivity(intent);
            finish(); // Prevent going back to login

        } catch (Exception e) {
            // Safety fallback if navigation fails
            Log.e(TAG, "Navigation error: " + e.getMessage(), e);
            Toast.makeText(this, "Erreur de navigation: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Navigation vers l'écran d'inscription
     */
    private void navigateToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Affiche un message d'erreur
     */
    private void showError(String message) {
        binding.textViewError.setText(message);
        binding.textViewError.setVisibility(View.VISIBLE);
    }

    /**
     * Cache le message d'erreur
     */
    private void hideError() {
        binding.textViewError.setVisibility(View.GONE);
    }

    /**
     * Affiche l'indicateur de chargement
     */
    private void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.buttonLogin.setEnabled(false);
    }

    /**
     * Cache l'indicateur de chargement
     */
    private void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
        binding.buttonLogin.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}