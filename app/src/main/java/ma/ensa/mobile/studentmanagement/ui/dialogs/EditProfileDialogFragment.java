package ma.ensa.mobile.studentmanagement.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import ma.ensa.mobile.studentmanagement.R;
import ma.ensa.mobile.studentmanagement.data.local.entity.Student;
import ma.ensa.mobile.studentmanagement.viewmodel.StudentViewModel;

public class EditProfileDialogFragment extends DialogFragment {

    private static final String ARG_STUDENT = "student";

    private StudentViewModel viewModel;
    private Student student;

    private TextInputEditText editTextPhone;
    private TextInputEditText editTextAddress;
    private TextInputEditText editTextCity;
    private TextView textViewError;
    private MaterialButton buttonCancel;
    private MaterialButton buttonSave;
    private ProgressBar progressBar;

    public static EditProfileDialogFragment newInstance(Student student) {
        EditProfileDialogFragment fragment = new EditProfileDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_STUDENT, student);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            student = (Student) getArguments().getSerializable(ARG_STUDENT);
        }
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_StudentManagement);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        editTextPhone = view.findViewById(R.id.editTextPhone);
        editTextAddress = view.findViewById(R.id.editTextAddress);
        editTextCity = view.findViewById(R.id.editTextCity);
        textViewError = view.findViewById(R.id.textViewError);
        buttonCancel = view.findViewById(R.id.buttonCancel);
        buttonSave = view.findViewById(R.id.buttonSave);
        progressBar = view.findViewById(R.id.progressBar);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(StudentViewModel.class);

        // Pre-fill current data
        if (student != null) {
            editTextPhone.setText(student.getPhone());
            editTextAddress.setText(student.getAddress());
            editTextCity.setText(student.getCity());
        }

        // Button listeners
        buttonCancel.setOnClickListener(v -> dismiss());
        buttonSave.setOnClickListener(v -> validateAndSave());

        // Observe operation result
        viewModel.getOperationResult().observe(getViewLifecycleOwner(), result -> {
            if (result != null && result > 0) {
                Toast.makeText(requireContext(), "Profil mis à jour avec succès", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        // Observe errors
        viewModel.getOperationError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                showError(error);
                hideLoading();
            }
        });
    }

    private void validateAndSave() {
        // Clear previous errors
        textViewError.setVisibility(View.GONE);

        // Get values
        String phone = editTextPhone.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String city = editTextCity.getText().toString().trim();

        // Validate phone (optional but if provided must be valid)
        if (!TextUtils.isEmpty(phone) && phone.length() < 10) {
            showError("Numéro de téléphone invalide (minimum 10 chiffres)");
            return;
        }

        // Update student object
        if (student != null) {
            student.setPhone(phone);
            student.setAddress(address);
            student.setCity(city);
            student.setUpdatedAt(System.currentTimeMillis() / 1000);

            // Show loading
            showLoading();

            // Update in database
            viewModel.updateStudent(student);
        }
    }

    private void showError(String message) {
        textViewError.setText(message);
        textViewError.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        buttonSave.setEnabled(false);
        buttonCancel.setEnabled(false);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
        buttonSave.setEnabled(true);
        buttonCancel.setEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }
}
