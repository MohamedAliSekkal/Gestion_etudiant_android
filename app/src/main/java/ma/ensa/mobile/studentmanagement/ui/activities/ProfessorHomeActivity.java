package ma.ensa.mobile.studentmanagement.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ma.ensa.mobile.studentmanagement.R;
import ma.ensa.mobile.studentmanagement.data.local.entity.Professor;
import ma.ensa.mobile.studentmanagement.utils.PreferencesManager;
import ma.ensa.mobile.studentmanagement.viewmodel.ProfessorViewModel;

public class ProfessorHomeActivity extends AppCompatActivity {

    private PreferencesManager preferencesManager;
    private ProfessorViewModel professorViewModel;
    private RecyclerView recyclerViewModules;
    private TextView textViewWelcome;
    private TextView textViewDepartment;
    private View layoutEmpty;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_home);

        preferencesManager = new PreferencesManager(this);

        // Setup toolbar
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle("Espace Professeur");
        }

        // Initialize views
        textViewWelcome = findViewById(R.id.textViewWelcome);
        textViewDepartment = findViewById(R.id.textViewDepartment);
        recyclerViewModules = findViewById(R.id.recyclerViewModules);
        layoutEmpty = findViewById(R.id.layoutEmpty);

        // Setup RecyclerView
        recyclerViewModules.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewModules.setHasFixedSize(true);

        // Initialize ViewModel
        professorViewModel = new ViewModelProvider(this).get(ProfessorViewModel.class);

        // Load professor data
        loadProfessorData();
    }

    private void loadProfessorData() {
        int userId = preferencesManager.getUserId();

        // Get professor by user ID
        professorViewModel.getProfessorByUserId(userId).observe(this, professor -> {
            if (professor != null) {
                updateUI(professor);
                loadModules(professor.getProfessorId());
            } else {
                Toast.makeText(this, "Erreur: Profil professeur introuvable", Toast.LENGTH_LONG).show();
                showEmptyState();
            }
        });
    }

    private void updateUI(Professor professor) {
        textViewWelcome.setText("Bienvenue, Prof. " + professor.getFullName());
        if (professor.getDepartment() != null) {
            textViewDepartment.setText("Département: " + professor.getDepartment());
        }
    }

    private void loadModules(int professorId) {
        professorViewModel.getModulesByProfessor(professorId).observe(this, modules -> {
            if (modules != null && !modules.isEmpty()) {
                showModulesList();
                // TODO: Set adapter with modules
                // ModuleAdapter adapter = new ModuleAdapter(modules, this::onModuleClick);
                // recyclerViewModules.setAdapter(adapter);

                Toast.makeText(this,
                    "Vous avez " + modules.size() + " module(s) assigné(s)",
                    Toast.LENGTH_SHORT).show();
            } else {
                showEmptyState();
            }
        });
    }

    private void showModulesList() {
        recyclerViewModules.setVisibility(View.VISIBLE);
        layoutEmpty.setVisibility(View.GONE);
    }

    private void showEmptyState() {
        recyclerViewModules.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.VISIBLE);
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
