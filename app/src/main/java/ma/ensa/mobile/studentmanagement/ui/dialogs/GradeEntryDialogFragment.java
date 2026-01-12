package ma.ensa.mobile.studentmanagement.ui.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ma.ensa.mobile.studentmanagement.R;
import ma.ensa.mobile.studentmanagement.data.local.entity.Grade;
import ma.ensa.mobile.studentmanagement.viewmodel.ProfessorViewModel;

public class GradeEntryDialogFragment extends DialogFragment {

    private static final String ARG_STUDENT_ID = "student_id";
    private static final String ARG_MODULE_ID = "module_id";
    private static final String ARG_STUDENT_NAME = "student_name";
    private static final String ARG_MODULE_NAME = "module_name";

    private ProfessorViewModel viewModel;

    private TextView textViewStudentInfo;
    private TextView textViewModuleInfo;
    private AutoCompleteTextView autoCompleteExamType;
    private TextInputEditText editTextGrade;
    private TextInputEditText editTextCoefficient;
    private TextInputEditText editTextExamDate;
    private TextView textViewError;
    private MaterialButton buttonCancel;
    private MaterialButton buttonSave;
    private ProgressBar progressBar;
    private TextInputLayout textInputLayoutExamDate;

    private int studentId;
    private int moduleId;
    private String studentName;
    private String moduleName;
    private long selectedExamDate;

    private final String[] examTypes = {
            "Contrôle Continu",
            "Travaux Pratiques",
            "Projet",
            "Examen Final",
            "Examen de Rattrapage"
    };

    public static GradeEntryDialogFragment newInstance(int studentId, int moduleId, String studentName, String moduleName) {
        GradeEntryDialogFragment fragment = new GradeEntryDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_STUDENT_ID, studentId);
        args.putInt(ARG_MODULE_ID, moduleId);
        args.putString(ARG_STUDENT_NAME, studentName);
        args.putString(ARG_MODULE_NAME, moduleName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            studentId = getArguments().getInt(ARG_STUDENT_ID);
            moduleId = getArguments().getInt(ARG_MODULE_ID);
            studentName = getArguments().getString(ARG_STUDENT_NAME);
            moduleName = getArguments().getString(ARG_MODULE_NAME);
        }
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_StudentManagement);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_grade_entry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        textViewStudentInfo = view.findViewById(R.id.textViewStudentInfo);
        textViewModuleInfo = view.findViewById(R.id.textViewModuleInfo);
        autoCompleteExamType = view.findViewById(R.id.autoCompleteExamType);
        editTextGrade = view.findViewById(R.id.editTextGrade);
        editTextCoefficient = view.findViewById(R.id.editTextCoefficient);
        editTextExamDate = view.findViewById(R.id.editTextExamDate);
        textViewError = view.findViewById(R.id.textViewError);
        buttonCancel = view.findViewById(R.id.buttonCancel);
        buttonSave = view.findViewById(R.id.buttonSave);
        progressBar = view.findViewById(R.id.progressBar);
        textInputLayoutExamDate = view.findViewById(R.id.textInputLayoutExamDate);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ProfessorViewModel.class);

        // Display info
        textViewStudentInfo.setText("Étudiant: " + studentName);
        textViewModuleInfo.setText("Module: " + moduleName);

        // Setup exam type dropdown
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                examTypes
        );
        autoCompleteExamType.setAdapter(adapter);

        // Set default exam date to today
        selectedExamDate = System.currentTimeMillis() / 1000;
        updateDateDisplay();

        // Setup date picker
        textInputLayoutExamDate.setEndIconOnClickListener(v -> showDatePicker());
        editTextExamDate.setOnClickListener(v -> showDatePicker());

        // Button listeners
        buttonCancel.setOnClickListener(v -> dismiss());
        buttonSave.setOnClickListener(v -> validateAndSave());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectedExamDate * 1000);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    Calendar selected = Calendar.getInstance();
                    selected.set(year, month, dayOfMonth);
                    selectedExamDate = selected.getTimeInMillis() / 1000;
                    updateDateDisplay();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateDateDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        editTextExamDate.setText(sdf.format(selectedExamDate * 1000));
    }

    private void validateAndSave() {
        // Clear previous errors
        textViewError.setVisibility(View.GONE);

        // Get values
        String examType = autoCompleteExamType.getText().toString().trim();
        String gradeStr = editTextGrade.getText().toString().trim();
        String coefficientStr = editTextCoefficient.getText().toString().trim();

        // Validate
        if (TextUtils.isEmpty(examType)) {
            showError("Veuillez sélectionner le type d'évaluation");
            return;
        }

        if (TextUtils.isEmpty(gradeStr)) {
            showError("Veuillez saisir une note");
            return;
        }

        if (TextUtils.isEmpty(coefficientStr)) {
            showError("Veuillez saisir un coefficient");
            return;
        }

        double grade;
        double coefficient;

        try {
            grade = Double.parseDouble(gradeStr);
            if (grade < 0 || grade > 20) {
                showError("La note doit être entre 0 et 20");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Note invalide");
            return;
        }

        try {
            coefficient = Double.parseDouble(coefficientStr);
            if (coefficient <= 0) {
                showError("Le coefficient doit être positif");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Coefficient invalide");
            return;
        }

        // Create Grade object
        Grade gradeObj = new Grade();
        gradeObj.setStudentId(studentId);
        gradeObj.setModuleId(moduleId);
        gradeObj.setExamType(examType);
        gradeObj.setGradeValue(grade);
        gradeObj.setCoefficient(coefficient);
        gradeObj.setExamDate(selectedExamDate);

        // Set timestamps
        long currentTime = System.currentTimeMillis() / 1000;
        gradeObj.setCreatedAt(currentTime);
        gradeObj.setUpdatedAt(currentTime);

        // Save
        showLoading();
        viewModel.insertGrade(gradeObj);

        // Show success and dismiss
        Toast.makeText(requireContext(), "Note enregistrée avec succès", Toast.LENGTH_SHORT).show();
        dismiss();
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
