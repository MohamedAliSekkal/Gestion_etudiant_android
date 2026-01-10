package ma.ensa.mobile.studentmanagement.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import ma.ensa.mobile.studentmanagement.R;
import ma.ensa.mobile.studentmanagement.utils.PreferencesManager;

public class StudentHomeActivity extends AppCompatActivity {

    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home_simple);

        preferencesManager = new PreferencesManager(this);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle("Espace Étudiant");
        }

        // Show coming soon message
        TextView tvMessage = findViewById(R.id.textViewComingSoon);
        tvMessage.setText("Fonctionnalités étudiantes en développement\n\n" +
                "Les fonctionnalités suivantes seront implémentées par la Personne B:\n\n" +
                "• Mon Profil\n" +
                "• Mes Absences\n" +
                "• Mes Notes\n\n" +
                "Connecté en tant que: " + preferencesManager.getUsername());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        preferencesManager.clearSession();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}