package ma.ensa.mobile.studentmanagement.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import ma.ensa.mobile.studentmanagement.R;
import ma.ensa.mobile.studentmanagement.data.local.entity.Branch;
import ma.ensa.mobile.studentmanagement.data.local.entity.Level;
import ma.ensa.mobile.studentmanagement.ui.activities.MainActivity;
import ma.ensa.mobile.studentmanagement.viewmodel.DashboardViewModel;

/**
 * Dashboard Fragment (A3)
 * Displays statistics: student counts, branches, levels
 */
public class HomeFragment extends Fragment {

    private DashboardViewModel viewModel;
    private TextView textViewActiveStudents;
    private TextView textViewArchivedStudents;
    private TextView textViewTotalBranches;
    private TextView textViewTotalLevels;
    private TextView textViewBranchList;
    private TextView textViewLevelList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        textViewActiveStudents = root.findViewById(R.id.textViewActiveStudents);
        textViewArchivedStudents = root.findViewById(R.id.textViewArchivedStudents);
        textViewTotalBranches = root.findViewById(R.id.textViewTotalBranches);
        textViewTotalLevels = root.findViewById(R.id.textViewTotalLevels);
        textViewBranchList = root.findViewById(R.id.textViewBranchList);
        textViewLevelList = root.findViewById(R.id.textViewLevelList);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        // Observe statistics
        observeStatistics();

        // Setup Manage Students button
        Button btnManageStudents = view.findViewById(R.id.buttonManageStudents);
        if (btnManageStudents != null) {
            btnManageStudents.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            });
        }

        // Setup Assign Professor button
        Button btnAssignProfessor = view.findViewById(R.id.buttonAssignProfessor);
        if (btnAssignProfessor != null) {
            btnAssignProfessor.setOnClickListener(v -> openAssignProfessorDialog());
        }

        // Setup Add Student to Module button
        Button btnAddStudentToModule = view.findViewById(R.id.buttonAddStudentToModule);
        if (btnAddStudentToModule != null) {
            btnAddStudentToModule.setOnClickListener(v -> openAddStudentToModuleDialog());
        }
    }

    private void openAssignProfessorDialog() {
        ma.ensa.mobile.studentmanagement.ui.dialogs.AssignProfessorDialogFragment dialog =
            ma.ensa.mobile.studentmanagement.ui.dialogs.AssignProfessorDialogFragment.newInstance();
        dialog.show(getParentFragmentManager(), "AssignProfessorDialog");
    }

    private void openAddStudentToModuleDialog() {
        ma.ensa.mobile.studentmanagement.ui.dialogs.AddStudentToModuleDialogFragment dialog =
            ma.ensa.mobile.studentmanagement.ui.dialogs.AddStudentToModuleDialogFragment.newInstance();
        dialog.show(getParentFragmentManager(), "AddStudentToModuleDialog");
    }

    private void observeStatistics() {
        // Observe active student count
        viewModel.getActiveStudentCount().observe(getViewLifecycleOwner(), count -> {
            if (count != null && textViewActiveStudents != null) {
                textViewActiveStudents.setText(String.valueOf(count));
            }
        });

        // Observe archived student count
        viewModel.getArchivedStudentCount().observe(getViewLifecycleOwner(), count -> {
            if (count != null && textViewArchivedStudents != null) {
                textViewArchivedStudents.setText(String.valueOf(count));
            }
        });

        // Observe branches
        viewModel.getAllBranches().observe(getViewLifecycleOwner(), branches -> {
            if (branches != null) {
                if (textViewTotalBranches != null) {
                    textViewTotalBranches.setText(String.valueOf(branches.size()));
                }
                if (textViewBranchList != null) {
                    displayBranches(branches);
                }
            }
        });

        // Observe levels
        viewModel.getAllLevels().observe(getViewLifecycleOwner(), levels -> {
            if (levels != null) {
                if (textViewTotalLevels != null) {
                    textViewTotalLevels.setText(String.valueOf(levels.size()));
                }
                if (textViewLevelList != null) {
                    displayLevels(levels);
                }
            }
        });
    }

    private void displayBranches(List<Branch> branches) {
        StringBuilder sb = new StringBuilder();
        for (Branch branch : branches) {
            sb.append("• ").append(branch.getBranchCode())
              .append(" - ").append(branch.getBranchName())
              .append("\n");
        }
        textViewBranchList.setText(sb.toString());
    }

    private void displayLevels(List<Level> levels) {
        StringBuilder sb = new StringBuilder();
        for (Level level : levels) {
            sb.append("• ").append(level.getLevelCode())
              .append(" - ").append(level.getLevelName())
              .append("\n");
        }
        textViewLevelList.setText(sb.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        textViewActiveStudents = null;
        textViewArchivedStudents = null;
        textViewTotalBranches = null;
        textViewTotalLevels = null;
        textViewBranchList = null;
        textViewLevelList = null;
    }
}
