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
import ma.ensa.mobile.studentmanagement.data.local.entity.Absence;
import ma.ensa.mobile.studentmanagement.viewmodel.ProfessorViewModel;

public class AbsenceDialogFragment extends DialogFragment {

    private static final String ARG_STUDENT_ID = "student_id";
    private static final String ARG_MODULE_ID = "module_id";
    private static final String ARG_STUDENT_NAME = "student_name";
    private static final String ARG_MODULE_NAME = "module_name";

    private ProfessorViewModel viewModel;

    private TextView textViewStudentInfo;
    private TextView textViewModuleInfo;
    private AutoCompleteTextView autoCompleteAbsenceType;
    private TextInputEditText editTextAbsenceDate;
    private TextInputEditText editTextReason;
    private TextView textViewError;
    private MaterialButton buttonCancel;
    private MaterialButton buttonSave;
    private ProgressBar progressBar;
    private TextInputLayout textInputLayoutAbsenceDate;

    private int studentId;
    private int moduleId;
    private String studentName;
    private String moduleName;
    private long selectedAbsenceDate;

    private final String[] absenceTypes = {"Justifiée", "Non justifiée"};

    public static AbsenceDialogFragment newInstance(int studentId, int moduleId, String studentName, String moduleName) {
        AbsenceDialogFragment fragment = new AbsenceDialogFragment();
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
        return inflater.inflate(R.layout.dialog_absence, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        viewModel = new ViewModelProvider(requireActivity()).get(ProfessorViewModel.class);

        textViewStudentInfo.setText("Étudiant: " + studentName);
        textViewModuleInfo.setText("Module: " + moduleName);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                absenceTypes
        );
        autoCompleteAbsenceType.setAdapter(adapter);

        selectedAbsenceDate = System.currentTimeMillis() / 1000;
        updateDateDisplay();

        textInputLayoutAbsenceDate.setEndIconOnClickListener(v -> showDatePicker());
        editTextAbsenceDate.setOnClickListener(v -> showDatePicker());

        buttonCancel.setOnClickListener(v -> dismiss());
        buttonSave.setOnClickListener(v -> validateAndSave());
    }

    private void initViews(View view) {
        textViewStudentInfo = view.findViewById(R.id.textViewStudentInfo);
        textViewModuleInfo = view.findViewById(R.id.textViewModuleInfo);
        autoCompleteAbsenceType = view.findViewById(R.id.autoCompleteAbsenceType);
        editTextAbsenceDate = view.findViewById(R.id.editTextAbsenceDate);
        editTextReason = view.findViewById(R.id.editTextReason);
        textViewError = view.findViewById(R.id.textViewError);
        buttonCancel = view.findViewById(R.id.buttonCancel);
        buttonSave = view.findViewById(R.id.buttonSave);
        progressBar = view.findViewById(R.id.progressBar);
        textInputLayoutAbsenceDate = view.findViewById(R.id.textInputLayoutAbsenceDate);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectedAbsenceDate * 1000);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    Calendar selected = Calendar.getInstance();
                    selected.set(year, month, dayOfMonth);
                    selectedAbsenceDate = selected.getTimeInMillis() / 1000;
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
        editTextAbsenceDate.setText(sdf.format(selectedAbsenceDate * 1000));
    }

    private void validateAndSave() {
        textViewError.setVisibility(View.GONE);

        String absenceType = autoCompleteAbsenceType.getText().toString().trim();
        String reason = editTextReason.getText().toString().trim();

        if (TextUtils.isEmpty(absenceType)) {
            showError("Veuillez sélectionner le type d'absence");
            return;
        }

        boolean isJustified = absenceType.equals("Justifiée");

        Absence absence = new Absence();
        absence.setStudentId(studentId);
        absence.setAbsenceDate(selectedAbsenceDate);
        absence.setAbsenceType(isJustified ? "JUSTIFIED" : "UNJUSTIFIED");
        absence.setJustified(isJustified);
        absence.setJustification(reason);
        absence.setHours(2); // Default 2 hours

        long currentTime = System.currentTimeMillis() / 1000;
        absence.setCreatedAt(currentTime);
        absence.setUpdatedAt(currentTime);

        showLoading();
        viewModel.insertAbsence(absence);

        Toast.makeText(requireContext(), "Absence enregistrée avec succès", Toast.LENGTH_SHORT).show();
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
