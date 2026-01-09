package ma.ensa.mobile.studentmanagement.ui.fragments.student;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.textfield.TextInputEditText;

import ma.ensa.mobile.studentmanagement.R;
import ma.ensa.mobile.studentmanagement.databinding.FragmentStudentListBinding;
import ma.ensa.mobile.studentmanagement.ui.adapters.StudentAdapter;
import ma.ensa.mobile.studentmanagement.viewmodel.StudentViewModel;

public class StudentListFragment extends Fragment {

    private FragmentStudentListBinding binding;
    private StudentViewModel viewModel;
    private StudentAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStudentListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(StudentViewModel.class);

        // Setup RecyclerView
        setupRecyclerView();

        // Observe students
        observeStudents();

        // Setup search
        setupSearch();

        // Setup FAB
        binding.fabAddStudent.setOnClickListener(v -> navigateToCreate());
    }

    private void setupRecyclerView() {
        adapter = new StudentAdapter(student -> {
            // Navigate to detail with student ID
            Bundle bundle = new Bundle();
            bundle.putInt("studentId", student.getStudentId());
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_studentListFragment_to_studentDetailFragment, bundle);
        });

        binding.recyclerViewStudents.setAdapter(adapter);
        binding.recyclerViewStudents.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewStudents.setHasFixedSize(true);
    }

    private void observeStudents() {
        viewModel.getAllActiveStudents().observe(getViewLifecycleOwner(), students -> {
            adapter.submitList(students);

            // Show/hide empty view
            if (students == null || students.isEmpty()) {
                binding.layoutEmpty.setVisibility(View.VISIBLE);
                binding.recyclerViewStudents.setVisibility(View.GONE);
            } else {
                binding.layoutEmpty.setVisibility(View.GONE);
                binding.recyclerViewStudents.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupSearch() {
        TextInputEditText searchEditText = binding.editTextSearch;

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    // Show all students
                    observeStudents();
                } else {
                    // Search students
                    viewModel.searchStudents(query).observe(getViewLifecycleOwner(), students -> {
                        adapter.submitList(students);

                        if (students == null || students.isEmpty()) {
                            binding.layoutEmpty.setVisibility(View.VISIBLE);
                            binding.recyclerViewStudents.setVisibility(View.GONE);
                        } else {
                            binding.layoutEmpty.setVisibility(View.GONE);
                            binding.recyclerViewStudents.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void navigateToCreate() {
        Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_studentListFragment_to_studentCreateFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
