package ma.ensa.mobile.studentmanagement.ui.fragments.absence;

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

import java.util.ArrayList;
import java.util.List;

import ma.ensa.mobile.studentmanagement.R;
import ma.ensa.mobile.studentmanagement.data.local.entity.Absence;
import ma.ensa.mobile.studentmanagement.viewmodel.AbsenceViewModel;
import ma.ensa.mobile.studentmanagement.viewmodel.ProfileViewModel;

/**
 * B1: Absence List Fragment
 * Displays student absences
 */
public class AbsenceListFragment extends Fragment {

    private AbsenceViewModel absenceViewModel;
    private ProfileViewModel profileViewModel;
    private RecyclerView recyclerViewAbsences;
    private TextView textViewTotalHours;
    private TextView textViewUnjustifiedHours;
    private View layoutEmpty;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_absence_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        recyclerViewAbsences = view.findViewById(R.id.recyclerViewAbsences);
        textViewTotalHours = view.findViewById(R.id.textViewTotalHours);
        textViewUnjustifiedHours = view.findViewById(R.id.textViewUnjustifiedHours);
        layoutEmpty = view.findViewById(R.id.layoutEmpty);

        // Setup RecyclerView
        recyclerViewAbsences.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewAbsences.setHasFixedSize(true);

        // Initialize ViewModels
        absenceViewModel = new ViewModelProvider(this).get(AbsenceViewModel.class);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Load data
        loadAbsences();
    }

    private void loadAbsences() {
        // For now, show empty state with sample message
        // In production, you would:
        // 1. Get current student ID from ProfileViewModel
        // 2. Load absences using absenceViewModel.getAbsencesByStudent(studentId)
        // 3. Update UI with actual data

        showEmptyState();
        textViewTotalHours.setText("0h");
        textViewUnjustifiedHours.setText("0h");
    }

    private void showEmptyState() {
        recyclerViewAbsences.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.VISIBLE);
    }

    private void showAbsencesList() {
        recyclerViewAbsences.setVisibility(View.VISIBLE);
        layoutEmpty.setVisibility(View.GONE);
    }
}
