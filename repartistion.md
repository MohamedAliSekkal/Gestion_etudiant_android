# 📋 RÉPARTITION DU TRAVAIL - MODULE GESTION DES ÉTUDIANTS

## Organisation du Développement par Fonctionnalités

**Projet :** ESMS (Engineering Schools Management System)  
**Module :** Gestion des Étudiants  
**Équipe :** 2 Développeurs  
**Architecture :** Android (Java) - MVVM - SQLite (Room)  
**Méthodologie :** Agile SCRUM

---

## 📊 PRINCIPE DE RÉPARTITION

### **Approche Retenue : Full-Stack par Fonctionnalité**

Chaque développeur est responsable de **l'intégralité** d'une fonctionnalité, incluant :
- ✅ Interface utilisateur (Activity/Fragment + XML)
- ✅ Logique de présentation (ViewModel)
- ✅ Logique métier (Repository)
- ✅ Accès aux données (DAO + Entity)
- ✅ Tests unitaires et d'intégration

### **Avantages de cette approche :**
- ✅ **Autonomie** : Chaque développeur maîtrise sa fonctionnalité de bout en bout
- ✅ **Cohérence** : Moins de dépendances inter-équipes
- ✅ **Responsabilité claire** : Ownership complète
- ✅ **Parallélisation** : Travail simultané sans conflits majeurs

---

## 🎯 VUE D'ENSEMBLE DE LA RÉPARTITION

| **Développeur** | **Fonctionnalités** | **Complexité** | **Charge estimée** |
|-----------------|---------------------|----------------|-------------------|
| **Personne A** | 🔐 Authentification<br>👥 Gestion CRUD Étudiants<br>📊 Statistiques Dashboard | **Haute** | ~60% |
| **Personne B** | ❌ Gestion des Absences<br>🎓 Parcours Académique<br>👤 Profil Étudiant | **Moyenne** | ~40% |

**Note :** La charge est équilibrée par la complexité des fonctionnalités.

---

# DÉVELOPPEUR A - FONCTIONNALITÉS ASSIGNÉES

## 🔐 FONCTIONNALITÉ A1 : AUTHENTIFICATION ET GESTION DES SESSIONS

### **Description**
Système complet d'authentification sécurisée avec gestion des rôles, sessions utilisateur et sécurité (BCrypt, verrouillage compte, tentatives échouées).

### **Responsabilités**
- ✅ Écran de connexion (Login)
- ✅ Écran splash screen
- ✅ Gestion de la session utilisateur (SharedPreferences)
- ✅ Authentification sécurisée (BCrypt)
- ✅ Gestion des rôles (ADMIN, SCOLARITE, APOGEE, TEACHER, STUDENT)
- ✅ Déconnexion

### **Fichiers à Créer/Modifier**

#### **📱 UI Layer (View)**
| **Fichier** | **Type** | **Description** |
|------------|----------|-----------------|
| `SplashActivity.java` | Activity | Écran de démarrage avec logo |
| `LoginActivity.java` | Activity | Formulaire de connexion |
| `activity_splash.xml` | Layout | Design splash screen |
| `activity_login.xml` | Layout | Formulaire login (Material Design) |

#### **🎨 ViewModel Layer**
| **Fichier** | **Description** |
|------------|-----------------|
| `AuthViewModel.java` | Gestion état authentification, validation, LiveData |

#### **💾 Data Layer (Model)**
| **Fichier** | **Type** | **Description** |
|------------|----------|-----------------|
| `User.java` | Entity | Table users (username, password_hash, role_id, etc.) |
| `Role.java` | Entity | Table roles (role_code, role_name, priority_level) |
| `UserDao.java` | DAO | Requêtes authentification, getUserByUsername(), updateFailedAttempts() |
| `RoleDao.java` | DAO | Requêtes getRoleByCode(), getAllRoles() |
| `UserRepository.java` | Repository | Logique authenticate(), lockUser(), updateLastLogin() |

#### **🛠️ Utils**
| **Fichier** | **Description** |
|------------|-----------------|
| `PreferencesManager.java` | Gestion session (saveUserSession, isLoggedIn, clearSession) |
| `ValidationUtils.java` | Validation email, username, password |

#### **🔧 Database**
| **Fichier** | **Description** |
|------------|-----------------|
| `AppDatabase.java` | Configuration Room, singleton, entités User et Role |
| `DatabaseCallback.java` | Seed data initial (5 rôles, 1 admin par défaut) |

#### **📦 Enums**
| **Fichier** | **Description** |
|------------|-----------------|
| `UserRole.java` | Enum (ADMIN, SCOLARITE, APOGEE, TEACHER, STUDENT) |

### **Points de Coordination avec Personne B**

#### **1. Interface de Contrat : Session Utilisateur**
```java
// Contrat partagé pour accéder aux infos de session
public interface ISessionManager {
    int getUserId();
    String getUsername();
    String getRoleCode();
    boolean isLoggedIn();
}
```
**Usage par B :** Pour filtrer les données selon le rôle (ex: étudiant voit uniquement son profil)

#### **2. Dépendance : AppDatabase**
**A** crée la configuration Room initiale  
**B** ajoute ses entités (Student, Absence, etc.) à `@Database(entities = {...})`

#### **3. Navigation : Point d'entrée**
**A** gère :
- `SplashActivity` (launcher)
- Redirection vers `LoginActivity` si non connecté
- Redirection vers `MainActivity` (conteneur de B) si connecté

---

## 👥 FONCTIONNALITÉ A2 : GESTION CRUD DES ÉTUDIANTS

### **Description**
Gestion complète du cycle de vie des étudiants : création, consultation, modification, archivage. Inclut la liste paginée, la recherche, et les filtres.

### **Responsabilités**
- ✅ Liste des étudiants (RecyclerView paginé)
- ✅ Détails d'un étudiant
- ✅ Création d'un nouvel étudiant (formulaire multi-étapes)
- ✅ Modification des informations
- ✅ Archivage d'un étudiant
- ✅ Recherche et filtrage (par nom, APOGÉE, filière, niveau)

### **Fichiers à Créer/Modifier**

#### **📱 UI Layer (View)**
| **Fichier** | **Type** | **Description** |
|------------|----------|-----------------|
| `MainActivity.java` | Activity | Conteneur principal avec BottomNavigationView |
| `StudentListFragment.java` | Fragment | Liste étudiants avec RecyclerView |
| `StudentDetailFragment.java` | Fragment | Affichage détaillé d'un étudiant |
| `StudentCreateFragment.java` | Fragment | Formulaire création (multi-étapes) |
| `StudentEditFragment.java` | Fragment | Formulaire modification |
| `activity_main.xml` | Layout | Navigation avec BottomNav + NavHostFragment |
| `fragment_student_list.xml` | Layout | RecyclerView + SearchView + FloatingActionButton |
| `fragment_student_detail.xml` | Layout | CardViews avec infos détaillées |
| `fragment_student_create.xml` | Layout | Formulaire avec TextInputLayouts |
| `fragment_student_edit.xml` | Layout | Formulaire édition |
| `item_student.xml` | Layout | Item RecyclerView (photo, nom, APOGÉE, filière) |
| `StudentAdapter.java` | Adapter | RecyclerView.Adapter avec DiffUtil |

#### **🎨 ViewModel Layer**
| **Fichier** | **Description** |
|------------|-----------------|
| `StudentViewModel.java` | CRUD étudiants, LiveData<List<Student>>, recherche, filtrage |

#### **💾 Data Layer (Model)**
| **Fichier** | **Type** | **Description** |
|------------|----------|-----------------|
| `Student.java` | Entity | Table students (apogee_number, first_name, last_name, email, branch_id, level_id, status, etc.) |
| `Branch.java` | Entity | Table branches (branch_code, branch_name, cycle, capacity) |
| `Level.java` | Entity | Table levels (level_code, level_name, year_number, cycle) |
| `Group.java` | Entity | Table groups (group_code, group_name, group_type, branch_id, level_id) |
| `StudentDao.java` | DAO | CRUD, getAllActiveStudents(), getStudentByApogee(), searchStudents(), archiveStudent() |
| `BranchDao.java` | DAO | getAllBranches(), getBranchById() |
| `LevelDao.java` | DAO | getAllLevels(), getLevelById() |
| `GroupDao.java` | DAO | getGroupsByBranch(), getGroupsByLevel() |
| `StudentRepository.java` | Repository | Logique métier CRUD, validation, gestion archivage |
| `BranchRepository.java` | Repository | Accès filières |

#### **🛠️ Utils**
| **Fichier** | **Description** |
|------------|-----------------|
| `DateUtils.java` | Conversion timestamps ↔ dates, formatage |
| `ValidationUtils.java` | Validation email, APOGÉE, CNE, téléphone |
| `Constants.java` | Constantes (status, regex, formats) |

#### **📦 Enums**
| **Fichier** | **Description** |
|------------|-----------------|
| `StudentStatus.java` | Enum (ACTIVE, ARCHIVED, GRADUATED) |
| `StudentOrigin.java` | Enum (CONCOURS, CNC, TRANSFER) |

#### **🗺️ Navigation**
| **Fichier** | **Description** |
|------------|-----------------|
| `nav_graph.xml` | Navigation Component : actions entre fragments |

### **Points de Coordination avec Personne B**

#### **1. Entité Student partagée**
**A** crée `Student.java`  
**B** utilise `Student.java` pour lier absences et profil

**Contrat :**
```java
// A garantit ces méthodes publiques dans Student.java
public int getStudentId();
public String getApogeeNumber();
public String getFullName(); // first_name + last_name
public int getBranchId();
public int getLevelId();
```

#### **2. StudentRepository partagé**
**A** implémente les méthodes CRUD de base  
**B** peut ajouter des méthodes spécifiques (ex: `getStudentWithAbsences()`)

#### **3. Navigation vers Profil**
Dans `StudentDetailFragment`, **A** fournit un bouton "Voir Profil Complet"  
→ Navigation vers `ProfileFragment` (géré par B)

#### **4. Seed Data**
**A** ajoute dans `DatabaseCallback` :
- 7 filières (GI, GSTR, ML, GM, GC, GCSE, BDAI)
- 5 niveaux (CP1, CP2, CI1, CI2, CI3)
- 5-10 étudiants de test

---

## 📊 FONCTIONNALITÉ A3 : DASHBOARD ET STATISTIQUES

### **Description**
Écran d'accueil avec statistiques globales : nombre d'étudiants par filière, par niveau, taux d'absences, graphiques.

### **Responsabilités**
- ✅ Écran Home avec cards statistiques
- ✅ Graphiques (nombre étudiants par filière, évolution inscriptions)
- ✅ Indicateurs clés (total étudiants, nouveaux inscrits, diplômés)
- ✅ Accès rapide aux fonctionnalités

### **Fichiers à Créer/Modifier**

#### **📱 UI Layer (View)**
| **Fichier** | **Type** | **Description** |
|------------|----------|-----------------|
| `HomeFragment.java` | Fragment | Dashboard avec statistiques |
| `fragment_home.xml` | Layout | Cards, graphiques (MPAndroidChart ou similar) |

#### **🎨 ViewModel Layer**
| **Fichier** | **Description** |
|------------|-----------------|
| `DashboardViewModel.java` | Calcul statistiques, LiveData pour graphiques |

#### **💾 Data Layer (Model)**
| **Fichier** | **Description** |
|------------|-----------------|
| Extension de `StudentRepository` | Méthodes getStatsByBranch(), getStatsByLevel(), getTotalActive() |

### **Points de Coordination avec Personne B**

**B** fournit des statistiques sur les absences  
**A** les intègre dans le Dashboard

---

# DÉVELOPPEUR B - FONCTIONNALITÉS ASSIGNÉES

## ❌ FONCTIONNALITÉ B1 : GESTION DES ABSENCES

### **Description**
Système complet d'enregistrement et de consultation des absences. Les enseignants enregistrent les absences, les étudiants peuvent consulter leur historique.

### **Responsabilités**
- ✅ Liste des absences (par étudiant ou globale)
- ✅ Enregistrement d'une nouvelle absence (enseignant)
- ✅ Filtrage par date, matière, statut (justifiée/injustifiée)
- ✅ Statistiques d'absences par étudiant
- ✅ Mode hors-ligne (is_synced)

### **Fichiers à Créer/Modifier**

#### **📱 UI Layer (View)**
| **Fichier** | **Type** | **Description** |
|------------|----------|-----------------|
| `AbsenceListFragment.java` | Fragment | Liste absences avec filtres |
| `AbsenceRecordFragment.java` | Fragment | Formulaire enregistrement absence |
| `fragment_absence_list.xml` | Layout | RecyclerView + filtres (date, statut) |
| `fragment_absence_record.xml` | Layout | Formulaire (étudiant, cours, date, heure, type séance) |
| `item_absence.xml` | Layout | Item RecyclerView absence |
| `AbsenceAdapter.java` | Adapter | RecyclerView.Adapter avec DiffUtil |

#### **🎨 ViewModel Layer**
| **Fichier** | **Description** |
|------------|-----------------|
| `AbsenceViewModel.java` | CRUD absences, LiveData, filtrage, statistiques |

#### **💾 Data Layer (Model)**
| **Fichier** | **Type** | **Description** |
|------------|----------|-----------------|
| `Absence.java` | Entity | Table absences (student_id, course_id, teacher_id, absence_date, absence_time, session_type, status, is_synced) |
| `Course.java` | Entity | Table courses (course_code, course_name, credits, level_id, branch_id, semester) |
| `Teacher.java` | Entity | Table teachers (employee_number, first_name, last_name, email, department, user_id) |
| `AbsenceDao.java` | DAO | insert, getAbsencesByStudent(), getAbsencesByCourse(), getAbsencesByDate(), updateSyncStatus() |
| `CourseDao.java` | DAO | getAllCourses(), getCoursesByLevel() |
| `TeacherDao.java` | DAO | getTeacherByUserId(), getAllTeachers() |
| `AbsenceRepository.java` | Repository | Logique métier absences, validation, synchronisation différée |

#### **🛠️ Utils**
| **Fichier** | **Description** |
|------------|-----------------|
| Extension `DateUtils.java` | Méthodes formatage heures (HH:mm) |

#### **📦 Enums**
| **Fichier** | **Description** |
|------------|-----------------|
| `AbsenceStatus.java` | Enum (UNJUSTIFIED, JUSTIFIED, EXCUSED) |
| `SessionType.java` | Enum (COURS, TD, TP) |

#### **📊 Views SQL**
| **Fichier** | **Description** |
|------------|-----------------|
| Requêtes dans DAO | Vue v_absence_statistics (total, justifiées, injustifiées par étudiant) |

### **Points de Coordination avec Personne A**

#### **1. Dépendance : Student.java**
**B** utilise l'entité `Student` créée par **A**  
Foreign Key: `absence.student_id → students.student_id`

#### **2. Navigation depuis StudentDetail**
Dans `StudentDetailFragment` (**A**), bouton "Voir Absences"  
→ Navigation vers `AbsenceListFragment` (**B**) avec argument `studentId`

#### **3. Seed Data Courses & Teachers**
**B** ajoute dans `DatabaseCallback` :
- 10-15 cours de test (INF101, MAT201, etc.)
- 5 enseignants de test

---

## 🎓 FONCTIONNALITÉ B2 : PARCOURS ACADÉMIQUE

### **Description**
Gestion du parcours universitaire : dossier académique, historique des niveaux, choix de filière, affectation automatique, répartition en groupes.

### **Responsabilités**
- ✅ Consultation dossier académique complet
- ✅ Historique des niveaux franchis
- ✅ Formulaire choix de filières (système de vœux)
- ✅ Affichage affectation finale
- ✅ Consultation groupe pédagogique

### **Fichiers à Créer/Modifier**

#### **📱 UI Layer (View)**
| **Fichier** | **Type** | **Description** |
|------------|----------|-----------------|
| `AcademicInfoFragment.java` | Fragment | Dossier académique détaillé |
| `BranchChoiceFragment.java` | Fragment | Formulaire choix filières (3 vœux) |
| `fragment_academic_info.xml` | Layout | Timeline historique, GPA, crédits |
| `fragment_branch_choice.xml` | Layout | Liste filières avec priorités (1, 2, 3) |

#### **🎨 ViewModel Layer**
| **Fichier** | **Description** |
|------------|-----------------|
| `AcademicViewModel.java` | Gestion parcours, calculs GPA, historique |

#### **💾 Data Layer (Model)**
| **Fichier** | **Type** | **Description** |
|------------|----------|-----------------|
| `AcademicRecord.java` | Entity | Table academic_records (student_id, academic_year, current_cycle, cumulative_gpa, total_credits, progression_status) |
| `LevelHistory.java` | Entity | Table level_history (record_id, level_id, academic_year, result, average) |
| `BranchChoice.java` | Entity | Table branch_choices (student_id, branch_id, priority, academic_year, status, submission_date) |
| `AcademicRecordDao.java` | DAO | getRecordByStudent(), updateGPA(), updateCredits() |
| `LevelHistoryDao.java` | DAO | getHistoryByRecord(), insertHistory() |
| `BranchChoiceDao.java` | DAO | getChoicesByStudent(), submitChoices(), updateStatus() |
| `AcademicRepository.java` | Repository | Logique parcours, calculs moyennes, validation progression |

#### **🛠️ Utils**
| **Fichier** | **Description** |
|------------|-----------------|
| Extension `ValidationUtils.java` | Validation choix filières (unicité, contraintes) |

#### **📦 Enums**
| **Fichier** | **Description** |
|------------|-----------------|
| `ProgressionStatus.java` | Enum (NORMAL, REDOUBLEMENT, ABANDON) |
| `ChoiceStatus.java` | Enum (PENDING, ACCEPTED, REJECTED) |
| `Cycle.java` | Enum (PREPARATORY, ENGINEERING) |

### **Points de Coordination avec Personne A**

#### **1. Relation 1:1 Student ↔ AcademicRecord**
**Trigger SQL (DatabaseCallback) :**  
Lors de création d'un étudiant (**A**), création automatique de son `AcademicRecord` (**B**)

```sql
CREATE TRIGGER trg_create_academic_record
AFTER INSERT ON students
BEGIN
    INSERT INTO academic_records (student_id, academic_year, current_cycle, current_year)
    SELECT NEW.student_id, '2024-2025', 'PREPARATORY', 1;
END;
```

#### **2. Dépendance Branch et Level**
**B** utilise les entités `Branch` et `Level` créées par **A**

#### **3. Navigation depuis StudentDetail**
Bouton "Parcours Académique" dans `StudentDetailFragment` (**A**)  
→ Navigation vers `AcademicInfoFragment` (**B**)

---

## 👤 FONCTIONNALITÉ B3 : PROFIL ÉTUDIANT (CONSULTATION ET MODIFICATION)

### **Description**
Interface permettant à l'étudiant de consulter et modifier son profil personnel (informations autorisées uniquement).

### **Responsabilités**
- ✅ Consultation profil complet (infos perso, académique, contact)
- ✅ Modification des informations autorisées (téléphone, adresse, photo)
- ✅ Changement de mot de passe
- ✅ Affichage récapitulatif (filière, niveau, groupe, absences)

### **Fichiers à Créer/Modifier**

#### **📱 UI Layer (View)**
| **Fichier** | **Type** | **Description** |
|------------|----------|-----------------|
| `ProfileFragment.java` | Fragment | Profil complet avec sections (infos, académique, contact) |
| `ProfileEditFragment.java` | Fragment | Formulaire modification infos autorisées |
| `fragment_profile.xml` | Layout | CardViews sectionnées, photo profil |
| `fragment_profile_edit.xml` | Layout | Formulaire édition |

#### **🎨 ViewModel Layer**
| **Fichier** | **Description** |
|------------|-----------------|
| `ProfileViewModel.java` | Gestion profil, LiveData, validation modifications |

#### **💾 Data Layer (Model)**
| **Fichier** | **Description** |
|------------|-----------------|
| Extension `StudentRepository` | Méthodes updateProfile(), updatePhone(), updateAddress(), updatePhoto() |
| Extension `UserRepository` | changePassword() avec BCrypt |

#### **🛠️ Utils**
| **Fichier** | **Description** |
|------------|-----------------|
| Extension `ValidationUtils.java` | Validation téléphone, adresse |

### **Points de Coordination avec Personne A**

#### **1. Session Utilisateur**
**B** utilise `PreferencesManager` (**A**) pour récupérer `userId` de l'étudiant connecté

#### **2. Permissions selon Rôle**
Si rôle = STUDENT → Modification limitée (téléphone, adresse)  
Si rôle = SCOLARITE → Modification complète (géré par **A**)

#### **3. Entités partagées**
**B** utilise `User` (**A**) et `Student` (**A**)

---

# 🔗 POINTS DE SYNCHRONISATION ET INTERFACES PARTAGÉES

## 📋 Contrats d'Interface entre A et B

### **1. ISessionManager (Créé par A, Utilisé par B)**
```java
package ma.ensa.mobile.studentmanagement.utils;

public interface ISessionManager {
    int getUserId();
    String getUsername();
    String getRoleCode();
    boolean isLoggedIn();
    boolean hasRole(String roleCode);
}
```

**Implémentation :** `PreferencesManager implements ISessionManager`

---

### **2. IStudentProvider (Créé par A, Utilisé par B)**
```java
package ma.ensa.mobile.studentmanagement.data.repository;

import androidx.lifecycle.LiveData;
import ma.ensa.mobile.studentmanagement.data.local.entity.Student;

public interface IStudentProvider {
    LiveData<Student> getStudentById(int studentId);
    Student getStudentByIdSync(int studentId);
    LiveData<Student> getStudentByApogee(String apogeeNumber);
}
```

**Implémentation :** `StudentRepository implements IStudentProvider`

---

### **3. Navigation Arguments (Partagé entre A et B)**

#### **Constants.java (Créé par A)**
```java
public class Constants {
    // Navigation Keys
    public static final String ARG_STUDENT_ID = "student_id";
    public static final String ARG_ABSENCE_ID = "absence_id";
    public static final String ARG_APOGEE_NUMBER = "apogee_number";
    
    // User Roles
    public static final String ROLE_ADMIN = "1";
    public static final String ROLE_SCOLARITE = "2";
    public static final String ROLE_APOGEE = "3";
    public static final String ROLE_TEACHER = "4";
    public static final String ROLE_STUDENT = "5";
}
```

**Usage par B :** Pour récupérer `studentId` lors de la navigation

---

## 🗓️ CHRONOLOGIE DE DÉVELOPPEMENT

### **Phase 1 : Fondations (Semaine 1-2)**
**A et B en parallèle**

| **Jour** | **Personne A** | **Personne B** |
|---------|---------------|---------------|
| J1-2 | Configuration projet, build.gradle | Configuration projet, build.gradle |
| J3-4 | Entités User, Role + DAOs | Entités Absence, Course, Teacher |
| J5-7 | SplashActivity, LoginActivity | Setup fragments vides |
| J8-10 | AppDatabase + Callback (seed data) | Ajouter entités à AppDatabase |

**Livrable :** Authentification fonctionnelle + Base de données complète

---

### **Phase 2 : Fonctionnalités Cœur (Semaine 3-4)**
**A et B en parallèle**

| **Période** | **Personne A** | **Personne B** |
|------------|---------------|---------------|
| Semaine 3 | CRUD Étudiants (List + Create) | Gestion Absences (List + Record) |
| Semaine 4 | CRUD Étudiants (Detail + Edit) | Parcours Académique |

**Livrable :** CRUD étudiants + Absences + Parcours académique

---

### **Phase 3 : Finitions (Semaine 5-6)**
**A et B en parallèle**

| **Période** | **Personne A** | **Personne B** |
|------------|---------------|---------------|
| Semaine 5 | Dashboard + Statistiques | Profil Étudiant |
| Semaine 6 | Tests + Optimisations | Tests + Optimisations |

**Livrable :** Application complète testée

---

## 🔄 RÉUNIONS DE SYNCHRONISATION

### **Daily Standup (15 min, chaque matin)**
- Qu'ai-je fait hier ?
- Que vais-je faire aujourd'hui ?
- Ai-je des blocages ?

### **Sync Technique (2x par semaine, 1h)**
**Objectifs :**
- Revue de code croisée
- Validation des interfaces partagées
- Résolution des conflits Git
- Vérification de la cohérence des données

**Points à couvrir :**
1. État d'avancement de chaque fonctionnalité
2. Revue des interfaces (ISessionManager, IStudentProvider)
3. Validation seed data (cohérence entre tables)
4. Test d'intégration (navigation A → B)
5. Planification prochaines 48h

---

## 📊 TABLEAU RÉCAPITULATIF DÉTAILLÉ

### **PERSONNE A - CHARGE DE TRAVAIL**

| **Fonctionnalité** | **Complexité** | **Fichiers** | **Lignes estimées** | **Durée** |
|-------------------|---------------|-------------|-------------------|----------|
| **A1. Authentification** | ⭐⭐⭐⭐ | 12 fichiers | ~1200 lignes | 6 jours |
| **A2. CRUD Étudiants** | ⭐⭐⭐⭐⭐ | 18 fichiers | ~2500 lignes | 10 jours |
| **A3. Dashboard** | ⭐⭐⭐ | 3 fichiers | ~400 lignes | 3 jours |
| **Tests unitaires** | ⭐⭐ | 5 fichiers | ~300 lignes | 2 jours |
| **TOTAL** | - | **38 fichiers** | **~4400 lignes** | **21 jours** |

---

### **PERSONNE B - CHARGE DE TRAVAIL**

| **Fonctionnalité** | **Complexité** | **Fichiers** | **Lignes estimées** | **Durée** |
|-------------------|---------------|-------------|-------------------|----------|
| **B1. Absences** | ⭐⭐⭐⭐ | 14 fichiers | ~1600 lignes | 7 jours |
| **B2. Parcours Académique** | ⭐⭐⭐⭐ | 12 fichiers | ~1400 lignes | 6 jours |
| **B3. Profil Étudiant** | ⭐⭐⭐ | 6 fichiers | ~700 lignes | 4 jours |
| **Tests unitaires** | ⭐⭐ | 4 fichiers | ~250 lignes | 2 jours |
| **TOTAL** | - | **36 fichiers** | **~3950 lignes** | **19 jours** |

---

## 🎯 CRITÈRES DE VALIDATION PAR FONCTIONNALITÉ

### **Personne A**

#### **A1. Authentification ✅**
- [ ] Connexion avec admin/admin123 réussie
- [ ] Verrouillage après 5 tentatives échouées
- [ ] Session persistante (fermeture/réouverture app)
- [ ] Déconnexion fonctionnelle
- [ ] SplashScreen affiche 2 secondes puis redirige

#### **A2. CRUD Étudiants ✅**
- [ ] Liste affiche tous les étudiants actifs
- [ ] Recherche par APOGÉE fonctionnelle
- [ ] Création étudiant avec validation
- [ ] Modification sans perte de données
- [ ] Archivage met is_archived = 1
- [ ] Détail étudiant affiche toutes les infos

#### **A3. Dashboard ✅**
- [ ] Statistiques par filière correctes
- [ ] Statistiques par niveau correctes
- [ ] Graphiques s'affichent correctement

---

### **Personne B**

#### **B1. Absences ✅**
- [ ] Enregistrement absence avec date/heure/matière
- [ ] Liste filtrée par étudiant
- [ ] Statistiques absences correctes
- [ ] Mode hors-ligne (is_synced = 0)

#### **B2. Parcours Académique ✅**
- [ ] Dossier académique créé automatiquement
- [ ] Historique niveaux affiché en timeline
- [ ] Choix filières (3 vœux) enregistrés
- [ ] GPA calculé correctement

#### **B3. Profil Étudiant ✅**
- [ ] Affichage profil complet
- [ ] Modification téléphone/adresse fonctionnelle
- [ ] Changement mot de passe sécurisé
- [ ] Photo profil uploadable

---

## 🛠️ OUTILS ET CONVENTIONS

### **Gestion de Version (Git/GitHub)**

#### **Branches**
```
main (production)
├── dev (développement)
│   ├── feature/auth (A)
│   ├── feature/crud-students (A)
│   ├── feature/dashboard (A)
│   ├── feature/absences (B)
│   ├── feature/academic (B)
│   └── feature/profile (B)
```

#### **Commits**
**Convention :** `[TYPE] Description courte`

**Types :**
- `[FEAT]` : Nouvelle fonctionnalité
- `[FIX]` : Correction de bug
- `[REFACTOR]` : Refactoring code
- `[STYLE]` : Formatage, indentation
- `[DOCS]` : Documentation
- `[TEST]` : Ajout tests

**Exemples :**
```bash
[FEAT] Add login screen with BCrypt authentication
[FIX] Resolve null pointer in StudentAdapter
[REFACTOR] Extract validation logic to ValidationUtils
```

---

### **Code Review**

**Règles :**
- ✅ Chaque Pull Request doit être reviewée par l'autre développeur
- ✅ Maximum 500 lignes par PR
- ✅ Tests unitaires obligatoires
- ✅ Respect des conventions de nommage

**Checklist PR :**
- [ ] Code compile sans erreur
- [ ] Tests unitaires passent
- [ ] Respect architecture MVVM
- [ ] Commentaires Javadoc sur méthodes publiques
- [ ] Pas de code commenté (supprimer)
- [ ] Strings externalisés dans strings.xml

---

## 📚 LIVRABLES FINAUX

### **Personne A**
1. ✅ Code source fonctionnalités A1, A2, A3
2. ✅ Tests unitaires (minimum 70% coverage)
3. ✅ Documentation technique (Javadoc)
4. ✅ Guide utilisateur : Authentification + CRUD
5. ✅ Seed data pour tests

### **Personne B**
1. ✅ Code source fonctionnalités B1, B2, B3
2. ✅ Tests unitaires (minimum 70% coverage)
3. ✅ Documentation technique (Javadoc)
4. ✅ Guide utilisateur : Absences + Profil
5. ✅ Seed data pour tests

### **Commun**
1. ✅ Application APK testée et fonctionnelle
2. ✅ Documentation architecture (diagrammes UML)
3. ✅ Rapport final de projet
4. ✅ Vidéo démo (5 minutes)
5. ✅ Présentation PowerPoint (20 slides)

