# 📱 MODULE GESTION DES ÉTUDIANTS - ESMS
## Engineering Schools Management System - Student Management Module

---

## 📋 TABLE DES MATIÈRES

1. [Description Générale du Projet](#1-description-générale-du-projet)
2. [Architecture Globale de l'Application](#2-architecture-globale-de-lapplication)
3. [Architecture Interne des Fichiers et Packages](#3-architecture-interne-des-fichiers-et-packages)
4. [Détail des Couches MVVM](#4-détail-des-couches-mvvm)
5. [Liste des Écrans et Pages](#5-liste-des-écrans-et-pages)
6. [Interactions Entre Composants](#6-interactions-entre-composants)
7. [Bonnes Pratiques](#7-bonnes-pratiques)
8. [Technologies et Dépendances](#8-technologies-et-dépendances)

---

## 1. DESCRIPTION GÉNÉRALE DU PROJET

### 1.1 Contexte

Le **Module Gestion des Étudiants** fait partie du système global **ESMS (Engineering Schools Management System)** de l'École Nationale des Sciences Appliquées de Tétouan (ENSA Tétouan). 

Ce module constitue une **application mobile Android native** développée en **Java**, permettant la gestion complète du cycle de vie des étudiants, depuis leur inscription initiale jusqu'à l'archivage de leur dossier après graduation.

### 1.2 Objectifs du Module

- ✅ **Digitaliser** la gestion administrative des étudiants
- ✅ **Centraliser** les informations académiques et personnelles
- ✅ **Faciliter** l'accès mobile aux données pour tous les acteurs
- ✅ **Automatiser** les processus de gestion du parcours universitaire
- ✅ **Sécuriser** les données avec authentification basée sur les rôles

### 1.3 Périmètre Fonctionnel

Le module couvre exclusivement :

| **Fonctionnalité** | **Description** |
|-------------------|----------------|
| **BF1** - Gestion des informations | Création, consultation, modification, archivage des étudiants |
| **BF2** - Parcours universitaire | Gestion des niveaux, affectation de filières, répartition en groupes |
| **BF3** - Gestion des absences | Enregistrement et consultation des absences par les enseignants |
| **BF4** - Recherche et filtrage | Recherche rapide d'étudiants par identifiant ou critères |
| **BF5** - Profil étudiant | Consultation et modification du profil personnel par l'étudiant |

### 1.4 Acteurs du Système

1. **Administrateur** - Gestion globale du système et des utilisateurs
2. **Service Scolarité** - Gestion des dossiers administratifs
3. **Service APOGÉE** - Gestion académique et affectations
4. **Enseignant** - Gestion des absences et consultation
5. **Étudiant** - Consultation et mise à jour de son profil

---

## 2. ARCHITECTURE GLOBALE DE L'APPLICATION

### 2.1 Pattern Architectural : MVVM

L'application adopte le pattern **MVVM (Model-View-ViewModel)** recommandé par Google pour Android :

```
┌─────────────────────────────────────────────────────────────┐
│                      APPLICATION ANDROID                     │
│                    (Module Gestion Étudiants)                │
└─────────────────────────────────────────────────────────────┘
                              │
        ┌─────────────────────┼─────────────────────┐
        │                     │                     │
        ▼                     ▼                     ▼
┌──────────────┐      ┌──────────────┐     ┌──────────────┐
│     VIEW     │      │  VIEWMODEL   │     │    MODEL     │
│              │◄─────┤              │────►│              │
│  Activities  │      │   LiveData   │     │  Entities    │
│  Fragments   │      │   Logic      │     │  Room DB     │
│  Layouts     │      │              │     │  Repository  │
└──────────────┘      └──────────────┘     └──────────────┘
        │                     │                     │
        │                     │                     │
        └─────────────────────┴─────────────────────┘
                              │
                              ▼
                    ┌──────────────────┐
                    │  BASE DE DONNÉES │
                    │  SQLite + Room   │
                    └──────────────────┘
```

### 2.2 Flux de Données

```
┌──────────┐         ┌────────────┐         ┌────────────┐         ┌──────────┐
│          │ Observe │            │ Request │            │  Query  │          │
│   View   │◄────────┤ ViewModel  │────────►│ Repository │────────►│ Database │
│          │         │            │         │            │         │  (Room)  │
└──────────┘         └────────────┘         └────────────┘         └──────────┘
     │                     ▲                       ▲
     │ User Action         │ LiveData              │ Data
     └─────────────────────┘                       │
                                                   │
                                          ┌────────┴────────┐
                                          │   DAO (Data     │
                                          │ Access Objects) │
                                          └─────────────────┘
```

### 2.3 Principe de Séparation des Responsabilités

| **Couche** | **Responsabilité** | **Ne fait PAS** |
|-----------|-------------------|----------------|
| **View** | Affichage UI, interactions utilisateur | Logique métier, accès DB direct |
| **ViewModel** | Préparation données, gestion état | Références Activity/Context, accès DB direct |
| **Repository** | Source unique de données, abstraction | Logique UI, transformation pour affichage |
| **Model** | Entités, DAOs, base de données | Logique métier, UI |

---

## 3. ARCHITECTURE INTERNE DES FICHIERS ET PACKAGES

### 3.1 Structure Complète du Projet

```
app/
├── manifests/
│   └── AndroidManifest.xml
│
├── java/
│   └── ma.ensa.mobile.studentmanagement/
│       │
│       ├── 📦 data/                          # COUCHE MODEL
│       │   ├── local/                        # Base de données locale
│       │   │   ├── database/
│       │   │   │   ├── AppDatabase.java      # Configuration Room
│       │   │   │   └── DatabaseCallback.java # Initialisation données
│       │   │   │
│       │   │   ├── dao/                      # Data Access Objects
│       │   │   │   ├── StudentDao.java
│       │   │   │   ├── AcademicRecordDao.java
│       │   │   │   ├── BranchDao.java
│       │   │   │   ├── LevelDao.java
│       │   │   │   ├── GroupDao.java
│       │   │   │   ├── AbsenceDao.java
│       │   │   │   ├── CourseDao.java
│       │   │   │   ├── TeacherDao.java
│       │   │   │   └── UserDao.java
│       │   │   │
│       │   │   └── entity/                   # Entités (tables)
│       │   │       ├── Student.java
│       │   │       ├── AcademicRecord.java
│       │   │       ├── LevelHistory.java
│       │   │       ├── Branch.java
│       │   │       ├── Level.java
│       │   │       ├── Group.java
│       │   │       ├── BranchChoice.java
│       │   │       ├── Absence.java
│       │   │       ├── Course.java
│       │   │       ├── Teacher.java
│       │   │       ├── User.java
│       │   │       └── Role.java
│       │   │
│       │   └── repository/                   # Repositories
│       │       ├── StudentRepository.java
│       │       ├── AcademicRepository.java
│       │       ├── AbsenceRepository.java
│       │       ├── BranchRepository.java
│       │       └── UserRepository.java
│       │
│       ├── 📦 ui/                            # COUCHE VIEW
│       │   ├── activities/
│       │   │   ├── SplashActivity.java
│       │   │   ├── LoginActivity.java
│       │   │   └── MainActivity.java
│       │   │
│       │   ├── fragments/
│       │   │   ├── home/
│       │   │   │   └── HomeFragment.java
│       │   │   │
│       │   │   ├── student/                  # Gestion étudiants
│       │   │   │   ├── StudentListFragment.java
│       │   │   │   ├── StudentDetailFragment.java
│       │   │   │   ├── StudentCreateFragment.java
│       │   │   │   └── StudentEditFragment.java
│       │   │   │
│       │   │   ├── absence/                  # Gestion absences
│       │   │   │   ├── AbsenceListFragment.java
│       │   │   │   └── AbsenceRecordFragment.java
│       │   │   │
│       │   │   ├── academic/                 # Parcours académique
│       │   │   │   ├── BranchChoiceFragment.java
│       │   │   │   └── AcademicInfoFragment.java
│       │   │   │
│       │   │   └── profile/                  # Profil étudiant
│       │   │       └── ProfileFragment.java
│       │   │
│       │   └── adapters/                     # RecyclerView Adapters
│       │       ├── StudentAdapter.java
│       │       ├── AbsenceAdapter.java
│       │       └── BranchAdapter.java
│       │
│       ├── 📦 viewmodel/                     # COUCHE VIEWMODEL
│       │   ├── StudentViewModel.java
│       │   ├── AbsenceViewModel.java
│       │   ├── AcademicViewModel.java
│       │   ├── AuthViewModel.java
│       │   └── ProfileViewModel.java
│       │
│       ├── 📦 utils/                         # Utilitaires
│       │   ├── Constants.java                # Constantes globales
│       │   ├── DateUtils.java                # Manipulation dates
│       │   ├── ValidationUtils.java          # Validation données
│       │   ├── NetworkUtils.java             # Gestion réseau
│       │   └── PreferencesManager.java       # SharedPreferences
│       │
│       └── 📦 models/                        # DTOs et POJOs
│           ├── dto/
│           │   ├── StudentDto.java
│           │   └── AbsenceDto.java
│           └── enums/
│               ├── StudentStatus.java
│               ├── StudentOrigin.java
│               ├── UserRole.java
│               └── AbsenceStatus.java
│
└── res/
    ├── drawable/                             # Icônes et images
    ├── layout/                               # Layouts XML
    │   ├── activity_splash.xml
    │   ├── activity_login.xml
    │   ├── activity_main.xml
    │   ├── fragment_home.xml
    │   ├── fragment_student_list.xml
    │   ├── fragment_student_detail.xml
    │   ├── fragment_student_create.xml
    │   ├── item_student.xml                  # Item RecyclerView
    │   └── item_absence.xml
    │
    ├── menu/                                 # Menus
    │   ├── bottom_nav_menu.xml
    │   └── main_menu.xml
    │
    ├── navigation/                           # Navigation Graph
    │   └── nav_graph.xml
    │
    ├── values/                               # Ressources
    │   ├── strings.xml
    │   ├── colors.xml
    │   ├── dimens.xml
    │   └── styles.xml
    │
    └── values-fr/                            # Internationalisation
        └── strings.xml
```

### 3.2 Convention de Nommage

#### **Packages**
- Tout en **minuscules**
- Séparés par **points**
- Exemple : `ma.ensa.mobile.studentmanagement.data.repository`

#### **Classes**
- **PascalCase** (première lettre majuscule)
- Suffixes selon le rôle :
  - Entités : `Student`, `Branch`, `Absence`
  - DAOs : `StudentDao`, `AbsenceDao`
  - ViewModels : `StudentViewModel`
  - Repositories : `StudentRepository`
  - Adapters : `StudentAdapter`
  - Fragments : `StudentListFragment`
  - Activities : `MainActivity`

#### **Méthodes et Variables**
- **camelCase** (première lettre minuscule)
- Exemples : `getStudentById()`, `currentStudent`, `isArchived`

#### **Constantes**
- **UPPER_SNAKE_CASE**
- Exemples : `MAX_STUDENTS`, `DB_VERSION`, `SHARED_PREF_KEY`

#### **Layouts XML**
- **snake_case** en minuscules
- Préfixe selon le type :
  - Activities : `activity_main.xml`
  - Fragments : `fragment_student_list.xml`
  - Items RecyclerView : `item_student.xml`
  - Dialogs : `dialog_confirm.xml`

---

## 4. DÉTAIL DES COUCHES MVVM

### 4.1 COUCHE MODEL (Données)

#### **A. Entités (Entity) - Tables de la Base de Données**

Chaque entité représente une table SQLite gérée par Room.

**Exemple : Student.java**
```java
@Entity(tableName = "students",
        indices = {
            @Index(value = "apogee_number", unique = true),
            @Index(value = "email", unique = true)
        },
        foreignKeys = {
            @ForeignKey(entity = Branch.class, 
                       parentColumns = "branch_id",
                       childColumns = "branch_id"),
            @ForeignKey(entity = Level.class,
                       parentColumns = "level_id", 
                       childColumns = "level_id")
        })
public class Student {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "student_id")
    private int studentId;
    
    @ColumnInfo(name = "apogee_number")
    @NonNull
    private String apogeeNumber;
    
    @ColumnInfo(name = "first_name")
    @NonNull
    private String firstName;
    
    // ... autres champs
}
```

**Liste des Entités :**
1. `Student` - Informations étudiants
2. `AcademicRecord` - Dossier académique
3. `LevelHistory` - Historique des niveaux
4. `Branch` - Filières
5. `Level` - Niveaux d'étude
6. `Group` - Groupes pédagogiques
7. `BranchChoice` - Choix de filières
8. `Absence` - Absences
9. `Course` - Matières
10. `Teacher` - Enseignants
11. `User` - Utilisateurs
12. `Role` - Rôles

#### **B. DAO (Data Access Objects) - Interface d'Accès aux Données**

Les DAOs définissent les opérations CRUD.

**Exemple : StudentDao.java**
```java
@Dao
public interface StudentDao {
    
    @Insert
    long insertStudent(Student student);
    
    @Update
    void updateStudent(Student student);
    
    @Delete
    void deleteStudent(Student student);
    
    @Query("SELECT * FROM students WHERE student_id = :id")
    LiveData<Student> getStudentById(int id);
    
    @Query("SELECT * FROM students WHERE is_archived = 0 ORDER BY last_name ASC")
    LiveData<List<Student>> getAllActiveStudents();
    
    @Query("SELECT * FROM students WHERE apogee_number = :apogeeNumber")
    LiveData<Student> getStudentByApogee(String apogeeNumber);
    
    @Query("UPDATE students SET is_archived = 1, archive_date = :date WHERE student_id = :id")
    void archiveStudent(int id, Date date);
}
```

#### **C. Database - Configuration Room**

**AppDatabase.java**
```java
@Database(
    entities = {
        Student.class,
        AcademicRecord.class,
        LevelHistory.class,
        Branch.class,
        Level.class,
        Group.class,
        BranchChoice.class,
        Absence.class,
        Course.class,
        Teacher.class,
        User.class,
        Role.class
    },
    version = 1,
    exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    
    private static volatile AppDatabase INSTANCE;
    
    // DAOs
    public abstract StudentDao studentDao();
    public abstract AcademicRecordDao academicRecordDao();
    public abstract AbsenceDao absenceDao();
    // ... autres DAOs
    
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "esms_database"
                    ).addCallback(new DatabaseCallback())
                     .build();
                }
            }
        }
        return INSTANCE;
    }
}
```

#### **D. Repository - Source Unique de Données**

Le Repository abstrait la source des données (locale ou distante future).

**StudentRepository.java**
```java
public class StudentRepository {
    
    private StudentDao studentDao;
    private LiveData<List<Student>> allStudents;
    
    public StudentRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        studentDao = database.studentDao();
        allStudents = studentDao.getAllActiveStudents();
    }
    
    // Opérations asynchrones avec ExecutorService
    public void insert(Student student) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentDao.insertStudent(student);
        });
    }
    
    public void update(Student student) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentDao.updateStudent(student);
        });
    }
    
    public void archive(int studentId, Date date) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentDao.archiveStudent(studentId, date);
        });
    }
    
    public LiveData<List<Student>> getAllStudents() {
        return allStudents;
    }
    
    public LiveData<Student> getStudentById(int id) {
        return studentDao.getStudentById(id);
    }
}
```

---

### 4.2 COUCHE VIEWMODEL (Logique de Présentation)

Le ViewModel prépare les données pour l'affichage et survit aux changements de configuration.

**StudentViewModel.java**
```java
public class StudentViewModel extends AndroidViewModel {
    
    private StudentRepository repository;
    private LiveData<List<Student>> allStudents;
    private MutableLiveData<Student> selectedStudent;
    
    public StudentViewModel(@NonNull Application application) {
        super(application);
        repository = new StudentRepository(application);
        allStudents = repository.getAllStudents();
        selectedStudent = new MutableLiveData<>();
    }
    
    // Getters pour LiveData observables
    public LiveData<List<Student>> getAllStudents() {
        return allStudents;
    }
    
    public LiveData<Student> getSelectedStudent() {
        return selectedStudent;
    }
    
    // Opérations métier
    public void createStudent(Student student) {
        if (validateStudent(student)) {
            repository.insert(student);
        }
    }
    
    public void updateStudent(Student student) {
        repository.update(student);
    }
    
    public void archiveStudent(int studentId) {
        repository.archive(studentId, new Date());
    }
    
    public void selectStudent(int studentId) {
        LiveData<Student> student = repository.getStudentById(studentId);
        selectedStudent.setValue(student.getValue());
    }
    
    // Validation
    private boolean validateStudent(Student student) {
        return student != null 
            && !student.getApogeeNumber().isEmpty()
            && !student.getFirstName().isEmpty()
            && !student.getLastName().isEmpty();
    }
    
    // Recherche
    public LiveData<Student> searchByApogee(String apogeeNumber) {
        return repository.getStudentById(apogeeNumber);
    }
}
```

**Caractéristiques importantes :**
- ✅ Hérite de `AndroidViewModel` (accès Application Context)
- ✅ Utilise `LiveData` pour observer les changements
- ✅ **Aucune référence** à Activity/Fragment/View
- ✅ Survit aux rotations d'écran
- ✅ Logique de validation et transformation

---

### 4.3 COUCHE VIEW (Interface Utilisateur)

#### **A. Activities**

**MainActivity.java** - Conteneur principal avec navigation
```java
public class MainActivity extends AppCompatActivity {
    
    private ActivityMainBinding binding;
    private NavController navController;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        // Configuration Navigation Component
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
            .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        
        // BottomNavigationView
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
        
        // Toolbar
        setSupportActionBar(binding.toolbar);
        NavigationUI.setupActionBarWithNavController(this, navController);
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}
```

#### **B. Fragments**

**StudentListFragment.java**
```java
public class StudentListFragment extends Fragment {
    
    private FragmentStudentListBinding binding;
    private StudentViewModel viewModel;
    private StudentAdapter adapter;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, 
                            ViewGroup container, 
                            Bundle savedInstanceState) {
        binding = FragmentStudentListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialiser ViewModel
        viewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        
        // Configurer RecyclerView
        setupRecyclerView();
        
        // Observer les données
        observeStudents();
        
        // Actions utilisateur
        setupClickListeners();
    }
    
    private void setupRecyclerView() {
        adapter = new StudentAdapter(student -> {
            // Navigation vers détails
            Bundle bundle = new Bundle();
            bundle.putInt("studentId", student.getStudentId());
            Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_list_to_detail, bundle);
        });
        
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setHasFixedSize(true);
    }
    
    private void observeStudents() {
        viewModel.getAllStudents().observe(getViewLifecycleOwner(), students -> {
            adapter.submitList(students);
            binding.emptyView.setVisibility(
                students.isEmpty() ? View.VISIBLE : View.GONE
            );
        });
    }
    
    private void setupClickListeners() {
        binding.fabAddStudent.setOnClickListener(v -> {
            Navigation.findNavController(v)
                .navigate(R.id.action_list_to_create);
        });
    }
}
```

#### **C. Adapters (RecyclerView)**

**StudentAdapter.java**
```java
public class StudentAdapter extends ListAdapter<Student, StudentAdapter.StudentViewHolder> {
    
    private OnItemClickListener listener;
    
    public StudentAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }
    
    private static final DiffUtil.ItemCallback<Student> DIFF_CALLBACK = 
        new DiffUtil.ItemCallback<Student>() {
            @Override
            public boolean areItemsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
                return oldItem.getStudentId() == newItem.getStudentId();
            }
            
            @Override
            public boolean areContentsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
                return oldItem.equals(newItem);
            }
        };
    
    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStudentBinding binding = ItemStudentBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false
        );
        return new StudentViewHolder(binding);
    }
    
    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = getItem(position);
        holder.bind(student);
    }
    
    class StudentViewHolder extends RecyclerView.ViewHolder {
        private ItemStudentBinding binding;
        
        public StudentViewHolder(ItemStudentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }
        
        public void bind(Student student) {
            binding.textName.setText(student.getFullName());
            binding.textApogee.setText(student.getApogeeNumber());
            binding.textBranch.setText(student.getBranchName());
            binding.textLevel.setText(student.getLevelName());
        }
    }
    
    public interface OnItemClickListener {
        void onItemClick(Student student);
    }
}
```

---

## 5. LISTE DES ÉCRANS ET PAGES

### 5.1 Écrans Principaux

| **#** | **Écran** | **Rôle** | **Acteurs** | **Navigation** |
|-------|----------|---------|------------|---------------|
| **S1** | Splash Screen | Écran de démarrage avec logo ENSA | Tous | → Login/Home |
| **S2** | Login | Authentification utilisateur | Tous | → Home |
| **S3** | Home | Tableau de bord personnalisé selon rôle | Tous | → Modules |

### 5.2 Module Gestion Étudiants

| **#** | **Écran** | **Fichiers** | **Rôle** | **Acteurs** |
|-------|----------|-------------|---------|------------|
| **E1** | Liste Étudiants | `StudentListFragment.java`<br>`fragment_student_list.xml` | Affiche liste complète des étudiants actifs avec recherche | Scolarité, Apogée, Enseignant |
| **E2** | Détails Étudiant | `StudentDetailFragment.java`<br>`fragment_student_detail.xml` | Affiche informations complètes d'un étudiant | Scolarité, Apogée, Enseignant |
| **E3** | Créer Étudiant | `StudentCreateFragment.java`<br>`fragment_student_create.xml` | Formulaire de création nouveau dossier | Scolarité |
| **E4** | Modifier Étudiant | `StudentEditFragment.java`<br>`fragment_student_edit.xml` | Formulaire de modification informations | Scolarité |
| **E5** | Profil Étudiant | `ProfileFragment.java`<br>`fragment_profile.xml` | Consultation/modification profil personnel | Étudiant |

### 5.3 Module Absences

| **#** | **Écran** | **Fichiers** | **Rôle** | **Acteurs** |
|-------|----------|-------------|---------|------------|
| **A1** | Liste Absences | `AbsenceListFragment.java`<br>`fragment_absence_list.xml` | Affiche historique des absences | Enseignant, Étudiant |
| **A2** | Enregistrer Absence | `AbsenceRecordFragment.java`<br>`fragment_absence_record.xml` | Formulaire d'enregistrement absence | Enseignant |

### 5.4 Module Académique

| **#** | **Écran** | **Fichiers** | **Rôle** | **Acteurs** |
|-------|----------|-------------|---------|------------|
| **AC1** | Choix Filière | `BranchChoiceFragment.java`<br>`fragment_branch_choice.xml` | Saisie des vœux de filières | Étudiant |
| **AC2** | Info Académique | `AcademicInfoFragment.java`<br>`fragment_academic_info.xml` | Affiche parcours académique complet | Tous |

### 5.5 Navigation Graph (Architecture)

```
                    ┌──────────────┐
                    │ SplashScreen │
                    └──────┬───────┘
                           │
                           ▼
                    ┌──────────────┐
                    │    Login     │
                    └──────┬───────┘
                           │
                           ▼
              ┌────────────────────────┐
              │    MainActivity        │
              │  (Navigation Host)     │
              └────────────────────────┘
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
        ▼                  ▼                  ▼
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│     Home     │  │   Students   │  │   Profile    │
└──────────────┘  └──────┬───────┘  └──────────────┘
                         │
        ┌────────────────┼────────────────┐
        │                │                │
        ▼                ▼                ▼
   ┌────────┐      ┌─────────┐     ┌──────────┐
   │  List  │─────►│ Details │────►│   Edit   │
   └────────┘      └─────────┘     └──────────┘
        │
        ▼
   ┌────────┐
   │ Create │
   └────────┘
```

---

## 6. INTERACTIONS ENTRE COMPOSANTS

### 6.1 Flux Complet : Affichage Liste Étudiants

```
┌─────────────────────────────────────────────────────────────────┐
│ 1. USER ACTION                                                  │
└─────────────────────────────────────────────────────────────────┘
    │ L'utilisateur ouvre StudentListFragment
    ▼
┌─────────────────────────────────────────────────────────────────┐
│ 2. FRAGMENT (View)                                              │
│    StudentListFragment.onViewCreated()                          │
│    ├─ Initialise StudentViewModel                               │
│    ├─ Configure RecyclerView + Adapter                          │
│    └─ Observe viewModel.getAllStudents()                        │
└─────────────────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────────────────┐
│ 3. VIEWMODEL                                                    │
│    StudentViewModel.getAllStudents()                            │
│    └─ Retourne LiveData<List<Student>> du Repository            │
└─────────────────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────────────────┐
│ 4. REPOSITORY                                                   │
│    StudentRepository.getAllStudents()                           │
│    └─ Appelle studentDao.getAllActiveStudents()                 │
└─────────────────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────────────────┐
│ 5. DAO                                                          │
│    StudentDao.getAllActiveStudents()                            │
│    └─ Exécute requête SQL SELECT sur Room Database              │
└─────────────────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────────────────┐
│ 6. DATABASE (SQLite + Room)                                     │
│    Retourne List<Student> via LiveData                          │
└─────────────────────────────────────────────────────────────────┘
    │
    │ LiveData notifie automatiquement
    ▼
┌─────────────────────────────────────────────────────────────────┐
│ 7. OBSERVER (Fragment)                                          │
│    observe(getViewLifecycleOwner(), students -> {               │
│        adapter.submitList(students);                            │
│    })                                                           │
└─────────────────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────────────────┐
│ 8. UI UPDATE                                                    │
│    RecyclerView affiche la liste mise à jour                    │
└─────────────────────────────────────────────────────────────────┘
```

### 6.2 Flux : Création d'un Étudiant

```
User Tap FAB "Ajouter"
    │
    ▼
Navigation → StudentCreateFragment
    │
    ▼
User saisit formulaire + Tap "Enregistrer"
    │
    ▼
Fragment récupère données → student object
    │
    ▼
viewModel.createStudent(student)
    │
    ▼
ViewModel valide données
    │
    ├─ ✅ Valide → repository.insert(student)
    │                      │
    │                      ▼
    │               ExecutorService (Thread Background)
    │                      │
    │                      ▼
    │               studentDao.insertStudent(student)
    │                      │
    │                      ▼
    │               Room insère dans SQLite
    │                      │
    │                      ▼
    │               LiveData<List> notifie observers
    │                      │
    │                      ▼
    │               StudentListFragment reçoit MAJ
    │                      │
    │                      ▼
    │               RecyclerView rafraîchi
    │                      │
    │                      ▼
    └───────────────► Navigation retour → Liste
    
    ├─ ❌ Invalide → Affiche Toast erreur
```

### 6.3 Communication Fragment ↔ ViewModel ↔ Repository

```java
// Fragment observe LiveData
viewModel.getAllStudents().observe(getViewLifecycleOwner(), students -> {
    // UI Thread - Safe
    adapter.submitList(students);
});

// ViewModel expose LiveData
public LiveData<List<Student>> getAllStudents() {
    return repository.getAllStudents(); // Provient du DAO
}

// Repository retourne LiveData du DAO
public LiveData<List<Student>> getAllStudents() {
    return studentDao.getAllActiveStudents(); // Room gère le threading
}

// DAO - Room génère l'implémentation
@Query("SELECT * FROM students WHERE is_archived = 0")
LiveData<List<Student>> getAllActiveStudents(); // Retour auto sur Main Thread
```

---

## 7. BONNES PRATIQUES

### 7.1 Architecture Android (MVVM)

✅ **Séparation stricte des couches**
- View ne communique **JAMAIS** directement avec Model
- ViewModel ne garde **AUCUNE référence** à View (Activity/Fragment/Context)
- Repository = seule source de vérité

✅ **Gestion du cycle de vie**
- Utiliser `LiveData` pour observer les données
- `ViewModel` survit aux changements de configuration
- Observer avec `getViewLifecycleOwner()` dans Fragments

✅ **Threading**
- **UI Thread** : Affichage uniquement
- **Background Thread** : Opérations DB (ExecutorService, Coroutines)
- Room retourne `LiveData` qui switch automatiquement sur Main Thread

✅ **Navigation**
- Utiliser **Navigation Component** d'Android Jetpack
- Navigation Graph pour la structure
- Safe Args pour passer données entre Fragments

### 7.2 Base de Données (Room)

✅ **Design**
- Normalisation des tables (3NF minimum)
- Index sur colonnes fréquemment recherchées
- Foreign Keys pour intégrité référentielle

✅ **DAOs**
- Requêtes SQL vérifiées à la compilation
- Retourner `LiveData` pour observation réactive
- Utiliser `@Transaction` pour opérations atomiques

✅ **Migrations**
```java
static final Migration MIGRATION_1_2 = new Migration(1, 2) {
    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database) {
        database.execSQL("ALTER TABLE students ADD COLUMN photo_url TEXT");
    }
};
```

### 7.3 Code Java

✅ **Conventions**
- CamelCase pour méthodes/variables
- PascalCase pour classes
- UPPER_SNAKE_CASE pour constantes
- Commentaires Javadoc pour classes publiques

✅ **Null Safety**
```java
@NonNull  // Jamais null
@Nullable // Peut être null

// Vérifications
if (student != null) {
    // Safe
}
```

✅ **Resource Management**
```java
// ViewBinding (pas findViewById)
private FragmentStudentListBinding binding;

@Override
public void onDestroyView() {
    super.onDestroyView();
    binding = null; // Éviter memory leaks
}
```

✅ **Strings externalisés**
```java
// ❌ MAL
textView.setText("Liste des étudiants");

// ✅ BON
textView.setText(R.string.student_list_title);
```

### 7.4 UI/UX Mobile

✅ **Material Design**
- Utiliser Material Components (MaterialButton, TextInputLayout, etc.)
- Respecter les guidelines Google
- Animations fluides (transitions, ripple effects)

✅ **Responsive**
- Layouts adaptatifs (ConstraintLayout)
- Support orientation portrait/paysage
- Différentes tailles d'écran (dp, sp)

✅ **Accessibilité**
```xml
android:contentDescription="@string/description"
android:importantForAccessibility="yes"
```

✅ **Performance**
- RecyclerView avec ViewHolder pattern
- DiffUtil pour calcul différentiel
- Image loading optimisé (Glide/Picasso)

### 7.5 Sécurité

✅ **Authentification**
- JWT tokens stockés en `SharedPreferences` chiffrées
- Session timeout
- Validation côté client ET serveur

✅ **Données sensibles**
```java
// EncryptedSharedPreferences pour tokens
EncryptedSharedPreferences.create(
    context,
    "secure_prefs",
    masterKey,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
);
```

✅ **Validation**
- Valider toutes les entrées utilisateur
- Sanitizer les données avant insertion DB
- Regex pour formats (email, téléphone, etc.)

### 7.6 Testing

✅ **Structure de tests**
```
app/src/
├── androidTest/    # Tests instrumentés (UI, DB)
│   └── java/
│       └── ExampleInstrumentedTest.java
│
└── test/           # Tests unitaires (ViewModels, Utils)
    └── java/
        └── ExampleUnitTest.java
```

✅ **Tests unitaires (JUnit)**
```java
@Test
public void testStudentValidation() {
    Student student = new Student();
    student.setFirstName("");
    assertFalse(ValidationUtils.isValidStudent(student));
}
```

✅ **Tests instrumentés (Espresso)**
```java
@Test
public void testStudentListDisplayed() {
    onView(withId(R.id.recyclerView))
        .check(matches(isDisplayed()));
}
```

---

## 8. TECHNOLOGIES ET DÉPENDANCES

### 8.1 Configuration Gradle

**build.gradle (Module :app)**
```gradle
plugins {
    id 'com.android.application'
}

android {
    namespace 'ma.ensa.mobile.studentmanagement'
    compileSdk 34

    defaultConfig {
        applicationId "ma.ensa.mobile.studentmanagement"
        minSdk 26
        targetSdk 34
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }
    
    buildFeatures {
        viewBinding true
        dataBinding true
    }
}

dependencies {
    // AndroidX Core
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    
    // Architecture Components
    implementation 'androidx.lifecycle:lifecycle-viewmodel:2.7.0'
    implementation 'androidx.lifecycle:lifecycle-livedata:2.7.0'
    implementation 'androidx.lifecycle:lifecycle-runtime:2.7.0'
    annotationProcessor 'androidx.lifecycle:lifecycle-compiler:2.7.0'
    
    // Room Database
    def room_version = "2.6.1"
    implementation "androidx.room:room-runtime:$room_version"
    annotationProcessor "androidx.room:room-compiler:$room_version"
    implementation "androidx.room:room-ktx:$room_version"
    
    // Navigation Component
    def nav_version = "2.7.6"
    implementation "androidx.navigation:navigation-fragment:$nav_version"
    implementation "androidx.navigation:navigation-ui:$nav_version"
    
    // RecyclerView
    implementation 'androidx.recyclerview:recyclerview:1.3.2'
    
    // CardView
    implementation 'androidx.cardview:cardview:1.0.0'
    
    // SwipeRefreshLayout
    implementation 'androidx.swiperefreshlayout:swiperefreshlayout:1.1.0'
    
    // Testing
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
    androidTestImplementation "androidx.room:room-testing:$room_version"
}
```

### 8.2 Versions Technologiques

| **Composant** | **Version** | **Usage** |
|--------------|------------|----------|
| **Java** | 17 | Langage de programmation |
| **Android SDK** | 34 (Android 14) | API Target |
| **Min SDK** | 26 (Android 8.0) | Compatibilité minimale |
| **Gradle** | 8.2 | Build system |
| **Android Studio** | Ladybug ou + | IDE |
| **Room** | 2.6.1 | ORM Database |
| **Lifecycle** | 2.7.0 | ViewModel, LiveData |
| **Navigation** | 2.7.6 | Navigation Component |
| **Material** | 1.11.0 | Material Design Components |

### 8.3 Bibliothèques Principales

#### **Room (SQLite ORM)**
```java
// Entité
@Entity
public class Student { }

// DAO
@Dao
public interface StudentDao { }

// Database
@Database
public abstract class AppDatabase extends RoomDatabase { }
```

#### **LiveData (Observable Data)**
```java
// Dans ViewModel
private LiveData<List<Student>> students;

// Dans Fragment
viewModel.getStudents().observe(this, list -> {
    // UI update
});
```

#### **ViewModel (Survit aux rotations)**
```java
public class StudentViewModel extends AndroidViewModel {
    // Logique de présentation
}
```

#### **ViewBinding (Remplace findViewById)**
```java
private FragmentStudentListBinding binding;
binding = FragmentStudentListBinding.inflate(inflater);
binding.textTitle.setText("Liste");
```

#### **Navigation Component**
```java
// Navigation entre Fragments
Navigation.findNavController(view)
    .navigate(R.id.action_list_to_detail);
```

---

## 📌 RÉSUMÉ EXÉCUTIF

### ✅ Points Clés du Projet

1. **Architecture MVVM** stricte avec séparation claire des responsabilités
2. **Room Database** pour persistance locale SQLite
3. **LiveData** pour observation réactive des données
4. **Navigation Component** pour navigation entre écrans
5. **Material Design** pour UI moderne et cohérente
6. **ViewBinding** pour accès type-safe aux vues
7. **Repository Pattern** pour abstraction des sources de données
8. **Support multilingue** (Français/Anglais)
9. **Tests unitaires et instrumentés**
10. **Respect des best practices Android**

### 📐 Structure en 4 Couches

```
┌─────────────────────────────────────────┐
│  VIEW (Activities, Fragments, Layouts)  │ ← UI + Interactions utilisateur
├─────────────────────────────────────────┤
│  VIEWMODEL (Logique de présentation)    │ ← Prépare données pour UI
├─────────────────────────────────────────┤
│  REPOSITORY (Source unique de données)  │ ← Abstraction accès données
├─────────────────────────────────────────┤
│  MODEL (Entities, DAOs, Database)       │ ← Persistance SQLite
└─────────────────────────────────────────┘
```

### 🎯 Prochaines Étapes

1. ✅ **Validation de cette structure** par vous
2. ⏳ **Génération script SQLite** complet
3. ⏳ **Création des premières pages** (Splash, Login, Liste)
4. ⏳ **Implémentation couche Model** (Entities + DAOs)
5. ⏳ **Développement incrémental** des fonctionnalités

---

**Date de création :** {{ date }}  
**Version :** 1.0  
**Projet :** ESMS - Module Gestion des Étudiants  
**Établissement :** ENSA Tétouan  
**Équipe :** Groupe 9

---

*Ce document constitue la référence architecturale complète du projet. Toute modification doit être documentée et validée.*