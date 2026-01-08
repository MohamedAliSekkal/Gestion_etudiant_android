package ma.ensa.mobile.studentmanagement.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import ma.ensa.mobile.studentmanagement.R;
import ma.ensa.mobile.studentmanagement.databinding.ActivityRegisterBinding;
import ma.ensa.mobile.studentmanagement.viewmodel.AuthViewModel;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ViewBinding
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialiser ViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Observer les LiveData
        setupObservers();

        // Listener bouton inscription
        binding.buttonRegister.setOnClickListener(v -> attemptRegister());

        // Listener lien connexion
        binding.textViewLogin.setOnClickListener(v -> navigateToLogin());
    }

    /**
     * Configure les observateurs LiveData
     */
    private void setupObservers() {
        // Observer le résultat d'inscription
        authViewModel.getRegistrationResult().observe(this, result -> {
            if (result != null && result > 0) {
                onRegistrationSuccess();
            }
        });

        // Observer les erreurs
        authViewModel.getRegistrationError().observe(this, error -> {
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
     * Tente l'inscription
     */
    private void attemptRegister() {
        // Réinitialiser les erreurs
        hideError();
        clearFieldErrors();

        // Récupérer les valeurs
        String fullName = binding.editTextFullName.getText().toString().trim();
        String username = binding.editTextUsername.getText().toString().trim();
        String email = binding.editTextEmail.getText().toString().trim();
        String phone = binding.editTextPhone.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();
        String confirmPassword = binding.editTextConfirmPassword.getText().toString().trim();

        // Validation
        boolean isValid = true;

        if (TextUtils.isEmpty(fullName)) {
            binding.textInputLayoutFullName.setError("Champ requis");
            isValid = false;
        }

        if (TextUtils.isEmpty(username)) {
            binding.textInputLayoutUsername.setError("Champ requis");
            isValid = false;
        } else if (username.length() < 3) {
            binding.textInputLayoutUsername.setError("Au moins 3 caractères");
            isValid = false;
        }

        if (TextUtils.isEmpty(email)) {
            binding.textInputLayoutEmail.setError("Champ requis");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.textInputLayoutEmail.setError("Email invalide");
            isValid = false;
        }

        if (TextUtils.isEmpty(phone)) {
            binding.textInputLayoutPhone.setError("Champ requis");
            isValid = false;
        } else if (phone.length() < 10) {
            binding.textInputLayoutPhone.setError("Numéro invalide");
            isValid = false;
        }

        if (TextUtils.isEmpty(password)) {
            binding.textInputLayoutPassword.setError("Champ requis");
            isValid = false;
        } else if (password.length() < 6) {
            binding.textInputLayoutPassword.setError("Au moins 6 caractères");
            isValid = false;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            binding.textInputLayoutConfirmPassword.setError("Champ requis");
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            binding.textInputLayoutConfirmPassword.setError("Les mots de passe ne correspondent pas");
            isValid = false;
        }

        if (!isValid) {
            return;
        }

        // Lancer l'inscription
        authViewModel.register(username, email, password, fullName, phone);
    }

    /**
     * Appelé lors du succès d'inscription
     */
    private void onRegistrationSuccess() {
        Toast.makeText(this, "Inscription réussie ! Vous pouvez maintenant vous connecter.", Toast.LENGTH_LONG).show();
        navigateToLogin();
    }

    /**
     * Navigation vers l'écran de connexion
     */
    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
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
     * Efface les erreurs des champs
     */
    private void clearFieldErrors() {
        binding.textInputLayoutFullName.setError(null);
        binding.textInputLayoutUsername.setError(null);
        binding.textInputLayoutEmail.setError(null);
        binding.textInputLayoutPhone.setError(null);
        binding.textInputLayoutPassword.setError(null);
        binding.textInputLayoutConfirmPassword.setError(null);
    }

    /**
     * Affiche l'indicateur de chargement
     */
    private void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.buttonRegister.setEnabled(false);
    }

    /**
     * Cache l'indicateur de chargement
     */
    private void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
        binding.buttonRegister.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
