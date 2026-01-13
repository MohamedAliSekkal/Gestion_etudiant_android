package ma.ensa.mobile.studentmanagement.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.ensa.mobile.studentmanagement.R;
import ma.ensa.mobile.studentmanagement.data.local.entity.Module;
import ma.ensa.mobile.studentmanagement.data.local.entity.Student;
import ma.ensa.mobile.studentmanagement.viewmodel.AcademicViewModel;
import ma.ensa.mobile.studentmanagement.viewmodel.StudentViewModel;

public class AddStudentToModuleDialogFragment extends DialogFragment {

    private AcademicViewModel academicViewModel;
    private StudentViewModel studentViewModel;

    private AutoCompleteTextView autoCompleteModule;
    private AutoCompleteTextView autoCompleteStudent;

    private List<Module> moduleList = new ArrayList<>();
    private List<Student> studentList = new ArrayList<>();
    private Map<String, Integer> moduleMap = new HashMap<>();
    private Map<String, Integer> studentMap = new HashMap<>();

    private Integer selectedModuleId = null;
    private Integer selectedStudentId = null;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_student_to_module, null);

        // Initialize ViewModels
        academicViewModel = new ViewModelProvider(this).get(AcademicViewModel.class);
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);

        // Initialize views
        autoCompleteModule = view.findViewById(R.id.autoCompleteModule);
        autoCompleteStudent = view.findViewById(R.id.autoCompleteStudent);
        MaterialButton buttonCancel = view.findViewById(R.id.buttonCancel);
        MaterialButton buttonAdd = view.findViewById(R.id.buttonAdd);

        // Load data
        loadModules();
        loadStudents();

        // Setup listeners
        autoCompleteModule.setOnItemClickListener((parent, v, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            selectedModuleId = moduleMap.get(selected);
        });

        autoCompleteStudent.setOnItemClickListener((parent, v, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            selectedStudentId = studentMap.get(selected);
        });

        buttonCancel.setOnClickListener(v -> dismiss());

        buttonAdd.setOnClickListener(v -> addStudentToModule());

        builder.setView(view);
        return builder.create();
    }

    private void loadModules() {
        academicViewModel.getAllActiveModules().observe(this, modules -> {
            if (modules != null && !modules.isEmpty()) {
                moduleList = modules;
                List<String> moduleNames = new ArrayList<>();
                for (Module module : modules) {
                    String display = module.getModuleCode() + " - " + module.getModuleName();
                    moduleNames.add(display);
                    moduleMap.put(display, module.getModuleId());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    moduleNames
                );
                autoCompleteModule.setAdapter(adapter);
            }
        });
    }

    private void loadStudents() {
        studentViewModel.getAllActiveStudents().observe(this, students -> {
            if (students != null && !students.isEmpty()) {
                studentList = students;
                List<String> studentNames = new ArrayList<>();
                for (Student student : students) {
                    String display = student.getApogeeNumber() + " - " +
                                   student.getFirstName() + " " + student.getLastName();
                    studentNames.add(display);
                    studentMap.put(display, student.getStudentId());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    studentNames
                );
                autoCompleteStudent.setAdapter(adapter);
            }
        });
    }

    private void addStudentToModule() {
        // Validate selections
        if (selectedModuleId == null) {
            Toast.makeText(requireContext(), "Veuillez sélectionner un module", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedStudentId == null) {
            Toast.makeText(requireContext(), "Veuillez sélectionner un étudiant", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add student to module
        studentViewModel.enrollStudentInModule(selectedStudentId, selectedModuleId);

        Toast.makeText(requireContext(), "Étudiant ajouté au module avec succès", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    public static AddStudentToModuleDialogFragment newInstance() {
        return new AddStudentToModuleDialogFragment();
    }
}
