package ma.ensa.mobile.studentmanagement.ui.fragments.academic;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

import ma.ensa.mobile.studentmanagement.R;
import ma.ensa.mobile.studentmanagement.ui.adapters.GradeAdapter;
import ma.ensa.mobile.studentmanagement.utils.PreferencesManager;
import ma.ensa.mobile.studentmanagement.viewmodel.AcademicViewModel;
import ma.ensa.mobile.studentmanagement.viewmodel.ProfileViewModel;

/**
 * B2: Academic Records Fragment
 * Displays student grades and academic performance
 */
public class AcademicRecordsFragment extends Fragment {

    private static final String TAG = "AcademicRecords";

    private AcademicViewModel academicViewModel;
    private ProfileViewModel profileViewModel;
    private RecyclerView recyclerViewGrades;
    private TextView textViewOverallAverage;
    private TextView textViewTotalCredits;
    private View layoutEmpty;
    private View cardCongratulations;
    private GradeAdapter gradeAdapter;
    private PreferencesManager preferencesManager;

    private static final double EXCELLENT_THRESHOLD = 14.0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_academic_records, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize PreferencesManager
        preferencesManager = new PreferencesManager(requireContext());

        // Initialize views
        recyclerViewGrades = view.findViewById(R.id.recyclerViewGrades);
        textViewOverallAverage = view.findViewById(R.id.textViewOverallAverage);
        textViewTotalCredits = view.findViewById(R.id.textViewTotalCredits);
        layoutEmpty = view.findViewById(R.id.layoutEmpty);
        cardCongratulations = view.findViewById(R.id.cardCongratulations);

        // Setup RecyclerView
        gradeAdapter = new GradeAdapter();
        recyclerViewGrades.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewGrades.setHasFixedSize(true);
        recyclerViewGrades.setAdapter(gradeAdapter);

        // Initialize ViewModels
        academicViewModel = new ViewModelProvider(this).get(AcademicViewModel.class);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Load data
        loadGrades();
    }

    private void loadGrades() {
        // Get current logged-in user's username
        String username = preferencesManager.getUsername();

        if (username == null || username.isEmpty()) {
            Log.e(TAG, "No username found in preferences");
            showEmptyState();
            return;
        }

        // Get student profile by username
        profileViewModel.getStudentByUsername(username).observe(getViewLifecycleOwner(), student -> {
            if (student == null) {
                Log.e(TAG, "Student not found for username: " + username);
                showEmptyState();
                return;
            }

            int studentId = student.getStudentId();
            Log.i(TAG, "Loading grades for student ID: " + studentId);

            // Load grades
            academicViewModel.getGradesByStudent(studentId).observe(getViewLifecycleOwner(), grades -> {
                if (grades != null && !grades.isEmpty()) {
                    gradeAdapter.setGrades(grades);
                    showGradesList();
                    Log.i(TAG, "Loaded " + grades.size() + " grades");
                } else {
                    showEmptyState();
                    Log.i(TAG, "No grades found");
                }
            });

            // Load average grade
            academicViewModel.getAverageGrade(studentId).observe(getViewLifecycleOwner(), average -> {
                if (average != null && average > 0) {
                    textViewOverallAverage.setText(String.format(Locale.FRANCE, "%.2f", average));
                    // Show congratulations if average is excellent
                    if (average >= EXCELLENT_THRESHOLD) {
                        cardCongratulations.setVisibility(View.VISIBLE);
                    } else {
                        cardCongratulations.setVisibility(View.GONE);
                    }
                } else {
                    textViewOverallAverage.setText("-");
                    cardCongratulations.setVisibility(View.GONE);
                }
            });

            // Load total credits earned
            academicViewModel.getTotalCreditsEarned(studentId).observe(getViewLifecycleOwner(), credits -> {
                if (credits != null) {
                    textViewTotalCredits.setText(String.valueOf(credits));
                } else {
                    textViewTotalCredits.setText("0");
                }
            });
        });
    }

    private void showEmptyState() {
        recyclerViewGrades.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.VISIBLE);
        textViewOverallAverage.setText("-");
        textViewTotalCredits.setText("0");
        cardCongratulations.setVisibility(View.GONE);
    }

    private void showGradesList() {
        recyclerViewGrades.setVisibility(View.VISIBLE);
        layoutEmpty.setVisibility(View.GONE);
    }
}
