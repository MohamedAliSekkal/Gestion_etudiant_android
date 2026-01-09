package ma.ensa.mobile.studentmanagement.ui.fragments.student;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ma.ensa.mobile.studentmanagement.R;
import ma.ensa.mobile.studentmanagement.data.local.entity.Student;
import ma.ensa.mobile.studentmanagement.databinding.FragmentStudentDetailBinding;
import ma.ensa.mobile.studentmanagement.viewmodel.StudentViewModel;

public class StudentDetailFragment extends Fragment {

    private FragmentStudentDetailBinding binding;
    private StudentViewModel viewModel;
    private int studentId;
    private Student currentStudent;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStudentDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(StudentViewModel.class);

        // Get student ID from arguments
        if (getArguments() != null) {
            studentId = getArguments().getInt("studentId", -1);
        }

        if (studentId == -1) {
            Toast.makeText(getContext(), "Erreur: Étudiant introuvable", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(binding.getRoot()).navigateUp();
            return;
        }

        // Load student details
        loadStudentDetails();

        // Setup buttons
        binding.buttonEdit.setOnClickListener(v -> navigateToEdit());
        binding.buttonArchive.setOnClickListener(v -> showArchiveConfirmation());
    }

    private void loadStudentDetails() {
        binding.progressBar.setVisibility(View.VISIBLE);

        viewModel.getStudentById(studentId).observe(getViewLifecycleOwner(), student -> {
            binding.progressBar.setVisibility(View.GONE);

            if (student != null) {
                currentStudent = student;
                displayStudentInfo(student);
            } else {
                Toast.makeText(getContext(), "Étudiant introuvable", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(binding.getRoot()).navigateUp();
            }
        });
    }

    private void displayStudentInfo(Student student) {
        // Personal Information
        binding.textViewFullName.setText(student.getFullName());
        binding.textViewApogee.setText(student.getApogeeNumber());
        binding.textViewCNE.setText(student.getCne());
        binding.textViewEmail.setText(student.getEmail());
        binding.textViewPhone.setText(student.getPhone() != null ? student.getPhone() : "Non renseigné");

        // Gender
        String gender = "Non renseigné";
        if (student.getGender() != null) {
            gender = student.getGender().equals("M") ? "Masculin" :
                     student.getGender().equals("F") ? "Féminin" : student.getGender();
        }
        binding.textViewGender.setText(gender);

        // Date of Birth
        if (student.getDateOfBirth() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String dateStr = sdf.format(new Date(student.getDateOfBirth() * 1000));
            binding.textViewDateOfBirth.setText(dateStr);
        } else {
            binding.textViewDateOfBirth.setText("Non renseignée");
        }

        // Address
        String address = student.getAddress() != null ? student.getAddress() : "Non renseignée";
        if (student.getCity() != null) {
            address += ", " + student.getCity();
        }
        binding.textViewAddress.setText(address);

        // Status
        binding.textViewStatus.setText(student.getStatus());

        // Change archive button text if already archived
        if (student.isArchived()) {
            binding.buttonArchive.setText("Restaurer");
        }
    }

    private void navigateToEdit() {
        Bundle bundle = new Bundle();
        bundle.putInt("studentId", studentId);
        Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_studentDetailFragment_to_studentEditFragment, bundle);
    }

    private void showArchiveConfirmation() {
        if (currentStudent == null) return;

        String title, message, positiveButton;

        if (currentStudent.isArchived()) {
            title = "Restaurer l'étudiant";
            message = "Voulez-vous restaurer " + currentStudent.getFullName() + " ?";
            positiveButton = "Restaurer";
        } else {
            title = "Archiver l'étudiant";
            message = "Voulez-vous archiver " + currentStudent.getFullName() + " ?\nCette action peut être annulée.";
            positiveButton = "Archiver";
        }

        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButton, (dialog, which) -> {
                    if (currentStudent.isArchived()) {
                        viewModel.restoreStudent(studentId);
                        Toast.makeText(getContext(), "Étudiant restauré", Toast.LENGTH_SHORT).show();
                    } else {
                        viewModel.archiveStudent(studentId);
                        Toast.makeText(getContext(), "Étudiant archivé", Toast.LENGTH_SHORT).show();
                    }
                    Navigation.findNavController(binding.getRoot()).navigateUp();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
