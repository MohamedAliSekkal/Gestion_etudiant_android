package ma.ensa.mobile.studentmanagement.ui.fragments.academic;

import android.os.Bundle;
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

import ma.ensa.mobile.studentmanagement.R;
import ma.ensa.mobile.studentmanagement.viewmodel.AcademicViewModel;
import ma.ensa.mobile.studentmanagement.viewmodel.ProfileViewModel;

/**
 * B2: Academic Records Fragment
 * Displays student grades and academic performance
 */
public class AcademicRecordsFragment extends Fragment {

    private AcademicViewModel academicViewModel;
    private ProfileViewModel profileViewModel;
    private RecyclerView recyclerViewGrades;
    private TextView textViewOverallAverage;
    private TextView textViewTotalCredits;
    private View layoutEmpty;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_academic_records, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        recyclerViewGrades = view.findViewById(R.id.recyclerViewGrades);
        textViewOverallAverage = view.findViewById(R.id.textViewOverallAverage);
        textViewTotalCredits = view.findViewById(R.id.textViewTotalCredits);
        layoutEmpty = view.findViewById(R.id.layoutEmpty);

        // Setup RecyclerView
        recyclerViewGrades.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewGrades.setHasFixedSize(true);

        // Initialize ViewModels
        academicViewModel = new ViewModelProvider(this).get(AcademicViewModel.class);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Load data
        loadGrades();
    }

    private void loadGrades() {
        // For now, show empty state with sample message
        // In production, you would:
        // 1. Get current student ID from ProfileViewModel
        // 2. Load grades using academicViewModel.getGradesByStudent(studentId)
        // 3. Calculate and display averages
        // 4. Update UI with actual data

        showEmptyState();
        textViewOverallAverage.setText("-");
        textViewTotalCredits.setText("0");
    }

    private void showEmptyState() {
        recyclerViewGrades.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.VISIBLE);
    }

    private void showGradesList() {
        recyclerViewGrades.setVisibility(View.VISIBLE);
        layoutEmpty.setVisibility(View.GONE);
    }
}
