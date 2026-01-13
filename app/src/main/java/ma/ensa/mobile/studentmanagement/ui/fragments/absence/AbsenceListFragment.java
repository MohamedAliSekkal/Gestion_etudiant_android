package ma.ensa.mobile.studentmanagement.ui.fragments.absence;

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

import ma.ensa.mobile.studentmanagement.R;
import ma.ensa.mobile.studentmanagement.ui.adapters.AbsenceAdapter;
import ma.ensa.mobile.studentmanagement.utils.PreferencesManager;
import ma.ensa.mobile.studentmanagement.viewmodel.AbsenceViewModel;
import ma.ensa.mobile.studentmanagement.viewmodel.ProfileViewModel;

/**
 * B1: Absence List Fragment
 * Displays student absences
 */
public class AbsenceListFragment extends Fragment {

    private static final String TAG = "AbsenceListFragment";

    private AbsenceViewModel absenceViewModel;
    private ProfileViewModel profileViewModel;
    private RecyclerView recyclerViewAbsences;
    private TextView textViewTotalHours;
    private TextView textViewUnjustifiedHours;
    private View layoutEmpty;
    private View cardWarning;
    private AbsenceAdapter absenceAdapter;
    private PreferencesManager preferencesManager;

    private static final int WARNING_THRESHOLD_HOURS = 10;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_absence_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize PreferencesManager
        preferencesManager = new PreferencesManager(requireContext());

        // Initialize views
        recyclerViewAbsences = view.findViewById(R.id.recyclerViewAbsences);
        textViewTotalHours = view.findViewById(R.id.textViewTotalHours);
        textViewUnjustifiedHours = view.findViewById(R.id.textViewUnjustifiedHours);
        layoutEmpty = view.findViewById(R.id.layoutEmpty);
        cardWarning = view.findViewById(R.id.cardWarning);

        // Setup RecyclerView
        absenceAdapter = new AbsenceAdapter();
        recyclerViewAbsences.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewAbsences.setHasFixedSize(true);
        recyclerViewAbsences.setAdapter(absenceAdapter);

        // Initialize ViewModels
        absenceViewModel = new ViewModelProvider(this).get(AbsenceViewModel.class);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Load data
        loadAbsences();
    }

    private void loadAbsences() {
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
            Log.i(TAG, "Loading absences for student ID: " + studentId);

            // Load absences
            absenceViewModel.getAbsencesByStudent(studentId).observe(getViewLifecycleOwner(), absences -> {
                if (absences != null && !absences.isEmpty()) {
                    absenceAdapter.setAbsences(absences);
                    showAbsencesList();
                    Log.i(TAG, "Loaded " + absences.size() + " absences");
                } else {
                    showEmptyState();
                    Log.i(TAG, "No absences found");
                }
            });

            // Load total absence hours
            absenceViewModel.getTotalAbsenceHours(studentId).observe(getViewLifecycleOwner(), totalHours -> {
                if (totalHours != null && totalHours > 0) {
                    textViewTotalHours.setText(totalHours + "h");
                } else {
                    textViewTotalHours.setText("0h");
                }
            });

            // Load unjustified absence hours
            absenceViewModel.getUnjustifiedAbsenceHours(studentId).observe(getViewLifecycleOwner(), unjustifiedHours -> {
                if (unjustifiedHours != null && unjustifiedHours > 0) {
                    textViewUnjustifiedHours.setText(unjustifiedHours + "h");
                    // Show warning if unjustified hours exceed threshold
                    if (unjustifiedHours >= WARNING_THRESHOLD_HOURS) {
                        cardWarning.setVisibility(View.VISIBLE);
                    } else {
                        cardWarning.setVisibility(View.GONE);
                    }
                } else {
                    textViewUnjustifiedHours.setText("0h");
                    cardWarning.setVisibility(View.GONE);
                }
            });
        });
    }

    private void showEmptyState() {
        recyclerViewAbsences.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.VISIBLE);
        textViewTotalHours.setText("0h");
        textViewUnjustifiedHours.setText("0h");
        cardWarning.setVisibility(View.GONE);
    }

    private void showAbsencesList() {
        recyclerViewAbsences.setVisibility(View.VISIBLE);
        layoutEmpty.setVisibility(View.GONE);
    }
}
