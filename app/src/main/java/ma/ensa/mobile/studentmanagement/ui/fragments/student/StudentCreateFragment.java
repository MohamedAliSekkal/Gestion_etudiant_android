package ma.ensa.mobile.studentmanagement.ui.fragments.student;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import ma.ensa.mobile.studentmanagement.R;
import ma.ensa.mobile.studentmanagement.data.local.entity.Student;
import ma.ensa.mobile.studentmanagement.databinding.FragmentStudentCreateBinding;
import ma.ensa.mobile.studentmanagement.ui.adapters.ModuleSelectionAdapter;
import ma.ensa.mobile.studentmanagement.viewmodel.AcademicViewModel;
import ma.ensa.mobile.studentmanagement.viewmodel.StudentViewModel;

public class StudentCreateFragment extends Fragment {

    private FragmentStudentCreateBinding binding;
    private StudentViewModel viewModel;
    private AcademicViewModel academicViewModel;
    private ModuleSelectionAdapter moduleAdapter;
    private boolean isEditMode = false;
    private int studentId = -1;
    private Student currentStudent;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStudentCreateBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModels
        viewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        academicViewModel = new ViewModelProvider(this).get(AcademicViewModel.class);

        // Check if edit mode
        if (getArguments() != null) {
            studentId = getArguments().getInt("studentId", -1);
            isEditMode = studentId != -1;
        }

        // Setup gender dropdown
        setupGenderDropdown();

        // Setup modules RecyclerView
        setupModulesRecyclerView();

        // Setup observers
        setupObservers();

        // Load student data if edit mode
        if (isEditMode) {
            loadStudentData();
            binding.buttonSave.setText("Mettre à jour");
        }

        // Setup save button
        binding.buttonSave.setOnClickListener(v -> attemptSave());
    }

    private void setupGenderDropdown() {
        String[] genders = new String[]{"Masculin", "Féminin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                genders
        );
        binding.autoCompleteGender.setAdapter(adapter);
    }

    private void setupModulesRecyclerView() {
        moduleAdapter = new ModuleSelectionAdapter();
        binding.recyclerViewModules.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewModules.setAdapter(moduleAdapter);

        // Load all active modules
        academicViewModel.getAllActiveModules().observe(getViewLifecycleOwner(), modules -> {
            if (modules != null && !modules.isEmpty()) {
                moduleAdapter.setModules(modules);
                binding.recyclerViewModules.setVisibility(View.VISIBLE);
                binding.textViewNoModules.setVisibility(View.GONE);
            } else {
                binding.recyclerViewModules.setVisibility(View.GONE);
                binding.textViewNoModules.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupObservers() {
        // Observe operation result
        viewModel.getOperationResult().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                if (result > 0) {
                    Toast.makeText(getContext(),
                            isEditMode ? "Étudiant mis à jour avec succès" : "Étudiant créé avec succès",
                            Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(binding.getRoot()).navigateUp();
                }
            }
        });

        // Observe errors
        viewModel.getOperationError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                showError(error);
            } else {
                hideError();
            }
        });

        // Observe loading
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                showLoading();
            } else {
                hideLoading();
            }
        });
    }

    private void loadStudentData() {
        viewModel.getStudentById(studentId).observe(getViewLifecycleOwner(), student -> {
            if (student != null) {
                currentStudent = student;
                populateFields(student);
            }
        });
    }

    private void populateFields(Student student) {
        binding.editTextFirstName.setText(student.getFirstName());
        binding.editTextLastName.setText(student.getLastName());
        binding.editTextApogee.setText(student.getApogeeNumber());
        binding.editTextCNE.setText(student.getCne());
        binding.editTextEmail.setText(student.getEmail());
        binding.editTextPhone.setText(student.getPhone());
        binding.editTextAddress.setText(student.getAddress());
        binding.editTextCity.setText(student.getCity());

        if (student.getGender() != null) {
            String genderDisplay = student.getGender().equals("M") ? "Masculin" : "Féminin";
            binding.autoCompleteGender.setText(genderDisplay, false);
        }
    }

    private void attemptSave() {
        // Clear errors
        clearFieldErrors();
        hideError();

        // Get values
        String firstName = binding.editTextFirstName.getText().toString().trim();
        String lastName = binding.editTextLastName.getText().toString().trim();
        String apogee = binding.editTextApogee.getText().toString().trim();
        String cne = binding.editTextCNE.getText().toString().trim();
        String email = binding.editTextEmail.getText().toString().trim();
        String phone = binding.editTextPhone.getText().toString().trim();
        String genderDisplay = binding.autoCompleteGender.getText().toString().trim();
        String address = binding.editTextAddress.getText().toString().trim();
        String city = binding.editTextCity.getText().toString().trim();

        // Validate
        boolean isValid = true;

        if (TextUtils.isEmpty(firstName)) {
            binding.editTextFirstName.setError("Champ requis");
            isValid = false;
        }

        if (TextUtils.isEmpty(lastName)) {
            binding.editTextLastName.setError("Champ requis");
            isValid = false;
        }

        if (TextUtils.isEmpty(apogee)) {
            binding.textInputLayoutApogee.setError("Champ requis");
            isValid = false;
        } else if (apogee.length() < 8) {
            binding.textInputLayoutApogee.setError("Minimum 8 chiffres");
            isValid = false;
        }

        if (TextUtils.isEmpty(cne)) {
            binding.textInputLayoutCNE.setError("Champ requis");
            isValid = false;
        }

        if (TextUtils.isEmpty(email)) {
            binding.textInputLayoutEmail.setError("Champ requis");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.textInputLayoutEmail.setError("Email invalide");
            isValid = false;
        }

        if (!isValid) {
            return;
        }

        // Convert gender
        String gender = genderDisplay.equals("Masculin") ? "M" :
                       genderDisplay.equals("Féminin") ? "F" : null;

        // Create or update student
        if (isEditMode && currentStudent != null) {
            // Update existing student
            currentStudent.setFirstName(firstName);
            currentStudent.setLastName(lastName);
            currentStudent.setApogeeNumber(apogee);
            currentStudent.setCne(cne);
            currentStudent.setEmail(email);
            currentStudent.setPhone(phone);
            currentStudent.setGender(gender);
            currentStudent.setAddress(address);
            currentStudent.setCity(city);

            viewModel.updateStudent(currentStudent);
            Toast.makeText(getContext(), "Étudiant mis à jour", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(binding.getRoot()).navigateUp();

        } else {
            // Create new student
            Student student = new Student();
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setApogeeNumber(apogee);
            student.setCne(cne);
            student.setEmail(email);
            student.setPhone(phone);
            student.setGender(gender);
            student.setAddress(address);
            student.setCity(city);
            student.setStatus("ACTIVE");
            student.setEnrollmentDate(System.currentTimeMillis() / 1000);

            // Get selected modules
            List<Integer> selectedModuleIds = moduleAdapter.getSelectedModuleIds();

            // Create student with modules
            viewModel.createStudentWithModules(student, selectedModuleIds);
        }
    }

    private void clearFieldErrors() {
        binding.textInputLayoutApogee.setError(null);
        binding.textInputLayoutCNE.setError(null);
        binding.textInputLayoutEmail.setError(null);
    }

    private void showError(String message) {
        binding.textViewError.setText(message);
        binding.textViewError.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        binding.textViewError.setVisibility(View.GONE);
    }

    private void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.buttonSave.setEnabled(false);
    }

    private void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
        binding.buttonSave.setEnabled(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
