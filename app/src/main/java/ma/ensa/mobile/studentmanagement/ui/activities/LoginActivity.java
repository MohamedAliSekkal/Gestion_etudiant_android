package ma.ensa.mobile.studentmanagement.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
                String.valueOf(user.getRoleId())
        );

        Toast.makeText(this, "Bienvenue " + user.getFullName(), Toast.LENGTH_SHORT).show();

        // Naviguer vers la page d'accueil
        navigateToHome();
    }

    /**
     * Navigation vers l'écran d'accueil
     */
    private void navigateToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish(); // Empêche le retour à la page de login
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