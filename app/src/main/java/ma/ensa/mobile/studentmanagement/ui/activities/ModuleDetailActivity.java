package ma.ensa.mobile.studentmanagement.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ma.ensa.mobile.studentmanagement.R;
import ma.ensa.mobile.studentmanagement.data.local.entity.Module;
import ma.ensa.mobile.studentmanagement.data.local.entity.Student;
import ma.ensa.mobile.studentmanagement.ui.adapters.StudentInModuleAdapter;
import ma.ensa.mobile.studentmanagement.ui.dialogs.AbsenceDialogFragment;
import ma.ensa.mobile.studentmanagement.ui.dialogs.GradeEntryDialogFragment;
import ma.ensa.mobile.studentmanagement.viewmodel.ProfessorViewModel;

public class ModuleDetailActivity extends AppCompatActivity implements
        StudentInModuleAdapter.OnStudentActionListener {

    public static final String EXTRA_MODULE_ID = "module_id";

    private ProfessorViewModel viewModel;
    private StudentInModuleAdapter adapter;

    private Toolbar toolbar;
    private TextView textViewModuleCode;
    private TextView textViewModuleName;
    private TextView textViewModuleInfo;
    private TextView textViewStudentCount;
    private RecyclerView recyclerViewStudents;
    private View layoutEmpty;
    private ProgressBar progressBar;

    private int moduleId;
    private Module currentModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_detail);

        // Get module ID from intent
        moduleId = getIntent().getIntExtra(EXTRA_MODULE_ID, -1);
        if (moduleId == -1) {
            Toast.makeText(this, "Erreur: Module introuvable", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        initViews();

        // Setup toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Détails du Module");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // Setup RecyclerView
        adapter = new StudentInModuleAdapter(this);
        recyclerViewStudents.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewStudents.setAdapter(adapter);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(ProfessorViewModel.class);

        // Load data
        loadModuleDetails();
        loadStudents();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        textViewModuleCode = findViewById(R.id.textViewModuleCode);
        textViewModuleName = findViewById(R.id.textViewModuleName);
        textViewModuleInfo = findViewById(R.id.textViewModuleInfo);
        textViewStudentCount = findViewById(R.id.textViewStudentCount);
        recyclerViewStudents = findViewById(R.id.recyclerViewStudents);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        progressBar = findViewById(R.id.progressBar);
    }

    private void loadModuleDetails() {
        viewModel.getModuleById(moduleId).observe(this, module -> {
            if (module != null) {
                currentModule = module;
                displayModuleInfo(module);
            }
        });
    }

    private void displayModuleInfo(Module module) {
        textViewModuleCode.setText(module.getModuleCode());
        textViewModuleName.setText(module.getModuleName());

        // Build info string
        StringBuilder info = new StringBuilder();
        info.append(module.getSemester()).append(" | ");
        info.append(module.getCredits()).append(" crédits");

        if (module.getHoursCourse() != null || module.getHoursTd() != null || module.getHoursTp() != null) {
            info.append(" | ");
            if (module.getHoursCourse() != null && module.getHoursCourse() > 0) {
                info.append(module.getHoursCourse()).append("h Cours");
            }
            if (module.getHoursTd() != null && module.getHoursTd() > 0) {
                if (module.getHoursCourse() != null && module.getHoursCourse() > 0) info.append(" + ");
                info.append(module.getHoursTd()).append("h TD");
            }
            if (module.getHoursTp() != null && module.getHoursTp() > 0) {
                if ((module.getHoursCourse() != null && module.getHoursCourse() > 0) ||
                    (module.getHoursTd() != null && module.getHoursTd() > 0)) info.append(" + ");
                info.append(module.getHoursTp()).append("h TP");
            }
        }

        textViewModuleInfo.setText(info.toString());
    }

    private void loadStudents() {
        showLoading();
        viewModel.getStudentsByModule(moduleId).observe(this, students -> {
            hideLoading();
            if (students != null && !students.isEmpty()) {
                showStudentsList();
                adapter.setStudents(students);
                textViewStudentCount.setText(students.size() + " étudiant" + (students.size() > 1 ? "s" : "") + " inscrit" + (students.size() > 1 ? "s" : ""));
            } else {
                showEmptyState();
                textViewStudentCount.setText("Aucun étudiant inscrit");
            }
        });
    }

    @Override
    public void onAddGradeClick(Student student) {
        if (currentModule == null) {
            Toast.makeText(this, "Erreur: Module non chargé", Toast.LENGTH_SHORT).show();
            return;
        }

        GradeEntryDialogFragment dialog = GradeEntryDialogFragment.newInstance(
                student.getStudentId(),
                moduleId,
                student.getFullName(),
                currentModule.getModuleName()
        );
        dialog.show(getSupportFragmentManager(), "GradeEntryDialog");
    }

    @Override
    public void onAddAbsenceClick(Student student) {
        if (currentModule == null) {
            Toast.makeText(this, "Erreur: Module non chargé", Toast.LENGTH_SHORT).show();
            return;
        }

        AbsenceDialogFragment dialog = AbsenceDialogFragment.newInstance(
                student.getStudentId(),
                moduleId,
                student.getFullName(),
                currentModule.getModuleName()
        );
        dialog.show(getSupportFragmentManager(), "AbsenceDialog");
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewStudents.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void showStudentsList() {
        recyclerViewStudents.setVisibility(View.VISIBLE);
        layoutEmpty.setVisibility(View.GONE);
    }

    private void showEmptyState() {
        recyclerViewStudents.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.VISIBLE);
    }
}
