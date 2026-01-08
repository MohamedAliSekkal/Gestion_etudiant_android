package ma.ensa.mobile.studentmanagement.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import ma.ensa.mobile.studentmanagement.R;
import ma.ensa.mobile.studentmanagement.databinding.ActivityHomeBinding;
import ma.ensa.mobile.studentmanagement.utils.PreferencesManager;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ViewBinding
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialiser PreferencesManager
        preferencesManager = new PreferencesManager(this);

        // Vérifier l'authentification
        if (!preferencesManager.isLoggedIn()) {
            navigateToLogin();
            return;
        }

        // Configurer la Toolbar
        setSupportActionBar(binding.toolbar);

        // Afficher les informations utilisateur
        displayUserInfo();

        // Configuration des listeners
        setupClickListeners();
    }

    /**
     * Affiche les informations de l'utilisateur connecté
     */
    private void displayUserInfo() {
        String username = preferencesManager.getUsername();
        String roleCode = preferencesManager.getRoleCode();

        binding.textViewUsername.setText(username);

        // Mapper le code rôle vers un nom lisible
        String roleName = getRoleName(roleCode);
        binding.textViewRole.setText(roleName);
    }

    /**
     * Convertit le code rôle en nom lisible
     */
    private String getRoleName(String roleCode) {
        // Correspond aux IDs dans la base de données
        switch (roleCode) {
            case "1":
                return "Administrateur";
            case "2":
                return "Service Scolarité";
            case "3":
                return "Service APOGÉE";
            case "4":
                return "Enseignant";
            case "5":
                return "Étudiant";
            default:
                return "Utilisateur";
        }
    }

    /**
     * Configure les listeners des boutons
     */
    private void setupClickListeners() {
        // Card Gestion des Étudiants
        binding.cardStudentManagement.setOnClickListener(v -> {
            // TODO: Naviguer vers StudentListActivity
            Toast.makeText(this, "Module Gestion Étudiants - À implémenter", Toast.LENGTH_SHORT).show();
        });

        // Card Déconnexion
        binding.cardLogout.setOnClickListener(v -> showLogoutDialog());
    }

    /**
     * Affiche une boîte de dialogue de confirmation de déconnexion
     */
    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Déconnexion")
                .setMessage("Êtes-vous sûr de vouloir vous déconnecter ?")
                .setPositiveButton("Oui", (dialog, which) -> logout())
                .setNegativeButton("Non", null)
                .show();
    }

    /**
     * Effectue la déconnexion
     */
    private void logout() {
        // Effacer la session
        preferencesManager.clearSession();

        Toast.makeText(this, "Déconnexion réussie", Toast.LENGTH_SHORT).show();

        // Retour à la page de login
        navigateToLogin();
    }

    /**
     * Navigation vers la page de connexion
     */
    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}