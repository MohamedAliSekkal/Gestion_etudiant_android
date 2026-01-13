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
import ma.ensa.mobile.studentmanagement.data.local.entity.Professor;
import ma.ensa.mobile.studentmanagement.viewmodel.AcademicViewModel;
import ma.ensa.mobile.studentmanagement.viewmodel.ProfessorViewModel;

public class AssignProfessorDialogFragment extends DialogFragment {

    private AcademicViewModel academicViewModel;
    private ProfessorViewModel professorViewModel;

    private AutoCompleteTextView autoCompleteModule;
    private AutoCompleteTextView autoCompleteProfessor;

    private List<Module> moduleList = new ArrayList<>();
    private List<Professor> professorList = new ArrayList<>();
    private Map<String, Integer> moduleMap = new HashMap<>();
    private Map<String, Integer> professorMap = new HashMap<>();

    private Integer selectedModuleId = null;
    private Integer selectedProfessorId = null;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_assign_professor, null);

        // Initialize ViewModels
        academicViewModel = new ViewModelProvider(this).get(AcademicViewModel.class);
        professorViewModel = new ViewModelProvider(this).get(ProfessorViewModel.class);

        // Initialize views
        autoCompleteModule = view.findViewById(R.id.autoCompleteModule);
        autoCompleteProfessor = view.findViewById(R.id.autoCompleteProfessor);
        MaterialButton buttonCancel = view.findViewById(R.id.buttonCancel);
        MaterialButton buttonAssign = view.findViewById(R.id.buttonAssign);

        // Load data
        loadModules();
        loadProfessors();

        // Setup listeners
        autoCompleteModule.setOnItemClickListener((parent, v, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            selectedModuleId = moduleMap.get(selected);
        });

        autoCompleteProfessor.setOnItemClickListener((parent, v, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            selectedProfessorId = professorMap.get(selected);
        });

        buttonCancel.setOnClickListener(v -> dismiss());

        buttonAssign.setOnClickListener(v -> assignProfessor());

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

    private void loadProfessors() {
        professorViewModel.getAllActiveProfessors().observe(this, professors -> {
            if (professors != null && !professors.isEmpty()) {
                professorList = professors;
                List<String> professorNames = new ArrayList<>();
                for (Professor professor : professors) {
                    String display = professor.getFirstName() + " " + professor.getLastName();
                    professorNames.add(display);
                    professorMap.put(display, professor.getProfessorId());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    professorNames
                );
                autoCompleteProfessor.setAdapter(adapter);
            }
        });
    }

    private void assignProfessor() {
        // Validate selections
        if (selectedModuleId == null) {
            Toast.makeText(requireContext(), "Veuillez sélectionner un module", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedProfessorId == null) {
            Toast.makeText(requireContext(), "Veuillez sélectionner un professeur", Toast.LENGTH_SHORT).show();
            return;
        }

        // Assign professor to module
        academicViewModel.assignProfessorToModule(selectedModuleId, selectedProfessorId);

        Toast.makeText(requireContext(), "Professeur assigné avec succès", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    public static AssignProfessorDialogFragment newInstance() {
        return new AssignProfessorDialogFragment();
    }
}
