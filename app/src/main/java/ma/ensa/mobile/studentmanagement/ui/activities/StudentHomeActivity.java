package ma.ensa.mobile.studentmanagement.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ma.ensa.mobile.studentmanagement.R;
import ma.ensa.mobile.studentmanagement.ui.fragments.absence.AbsenceListFragment;
import ma.ensa.mobile.studentmanagement.ui.fragments.academic.AcademicRecordsFragment;
import ma.ensa.mobile.studentmanagement.ui.fragments.profile.ProfileFragment;
import ma.ensa.mobile.studentmanagement.utils.PreferencesManager;

public class StudentHomeActivity extends AppCompatActivity {

    private PreferencesManager preferencesManager;
    private BottomNavigationView bottomNavigation;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home_simple);

        preferencesManager = new PreferencesManager(this);

        // Setup toolbar
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle("Mon Profil");
        }

        // Setup bottom navigation
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Fragment selectedFragment = null;
            String title = "";

            if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
                title = "Mon Profil";
            } else if (itemId == R.id.nav_absences) {
                selectedFragment = new AbsenceListFragment();
                title = "Mes Absences";
            } else if (itemId == R.id.nav_grades) {
                selectedFragment = new AcademicRecordsFragment();
                title = "Mes Notes";
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                if (toolbar != null) {
                    toolbar.setTitle(title);
                }
                return true;
            }

            return false;
        });

        // Load ProfileFragment by default (B3: Student Profile)
        if (savedInstanceState == null) {
            bottomNavigation.setSelectedItemId(R.id.nav_profile);
        }
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