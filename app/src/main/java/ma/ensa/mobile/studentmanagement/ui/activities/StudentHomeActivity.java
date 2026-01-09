package ma.ensa.mobile.studentmanagement.ui.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import ma.ensa.mobile.studentmanagement.R;
import ma.ensa.mobile.studentmanagement.databinding.ActivityStudentHomeBinding;
import ma.ensa.mobile.studentmanagement.utils.PreferencesManager;

public class StudentHomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityStudentHomeBinding binding;
    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            binding = ActivityStudentHomeBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            preferencesManager = new PreferencesManager(this);

            // Setup toolbar
            Toolbar toolbar = findViewById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
            }

            // Setup navigation drawer
            DrawerLayout drawer = binding.drawerLayout;
            NavigationView navigationView = binding.navView;

            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_profile, R.id.nav_absences, R.id.nav_notes)
                    .setOpenableLayout(drawer)
                    .build();

            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_student_home);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);

            // Update header with user info
            updateNavigationHeader();

            // Set click listeners for menu items
            setupDrawerContent(navigationView);
        } catch (Exception e) {
            Toast.makeText(this, "Erreur: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            finish();
        }
    }

    private void updateNavigationHeader() {
        try {
            NavigationView navigationView = binding.navView;
            View headerView = navigationView.getHeaderView(0);

            TextView tvUsername = headerView.findViewById(R.id.textViewUsername);
            TextView tvEmail = headerView.findViewById(R.id.textViewEmail);

            if (tvUsername != null) {
                tvUsername.setText(preferencesManager.getUsername());
            }
            if (tvEmail != null) {
                tvEmail.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(item -> {
            // Handle navigation view item clicks here
            int id = item.getItemId();

            if (id == R.id.nav_logout) {
                logout();
                return true;
            }

            // Let the navigation UI handle other items
            return NavigationUI.onNavDestinationSelected(item,
                    Navigation.findNavController(this, R.id.nav_host_fragment_content_student_home));
        });
    }

    private void logout() {
        preferencesManager.clearSession();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_student_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}