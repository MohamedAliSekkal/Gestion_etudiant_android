-- ============================================================================
-- SCRIPT SQL - BASE DE DONNÉES SQLITE
-- Module : Gestion des Étudiants - ESMS
-- Établissement : ENSA Tétouan
-- Version : 1.0
-- Date : 2025
-- ============================================================================
-- 
-- Description : Ce script crée la structure complète de la base de données
--               locale SQLite pour le module de gestion des étudiants.
--               Conforme à l'architecture Android Room.
--
-- Tables : 12 tables principales
-- - students          : Informations des étudiants
-- - academic_records  : Dossiers académiques
-- - level_history     : Historique des niveaux
-- - branches          : Filières d'enseignement
-- - levels            : Niveaux d'étude
-- - groups            : Groupes pédagogiques
-- - branch_choices    : Choix de filières
-- - absences          : Absences étudiants
-- - courses           : Matières/Modules
-- - teachers          : Enseignants
-- - users             : Utilisateurs système
-- - roles             : Rôles et permissions
--
-- ============================================================================

-- ============================================================================
-- CONFIGURATION INITIALE
-- ============================================================================

-- Active les contraintes de clés étrangères (IMPORTANT pour SQLite)
PRAGMA foreign_keys = ON;

-- Active le mode Write-Ahead Logging pour de meilleures performances
PRAGMA journal_mode = WAL;

-- ============================================================================
-- SUPPRESSION DES TABLES EXISTANTES (si réinitialisation nécessaire)
-- ============================================================================

DROP TABLE IF EXISTS absences;
DROP TABLE IF EXISTS branch_choices;
DROP TABLE IF EXISTS level_history;
DROP TABLE IF EXISTS academic_records;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS teachers;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS levels;
DROP TABLE IF EXISTS branches;
DROP TABLE IF EXISTS roles;

-- ============================================================================
-- TABLE : roles
-- Description : Rôles et permissions des utilisateurs du système
-- ============================================================================

CREATE TABLE IF NOT EXISTS roles (
    role_id INTEGER PRIMARY KEY AUTOINCREMENT,
    role_code TEXT NOT NULL UNIQUE CHECK(role_code IN ('ADMIN', 'SCOLARITE', 'APOGEE', 'TEACHER', 'STUDENT')),
    role_name TEXT NOT NULL,
    role_name_en TEXT NOT NULL,
    role_name_fr TEXT NOT NULL,
    description TEXT,
    priority_level INTEGER NOT NULL CHECK(priority_level BETWEEN 1 AND 5),
    is_active INTEGER NOT NULL DEFAULT 1 CHECK(is_active IN (0, 1)),
    created_at INTEGER NOT NULL DEFAULT (strftime('%s', 'now')),
    updated_at INTEGER NOT NULL DEFAULT (strftime('%s', 'now'))
);

-- Index pour recherche rapide par code
CREATE INDEX IF NOT EXISTS idx_roles_code ON roles(role_code);

-- ============================================================================
-- TABLE : users
-- Description : Comptes utilisateurs pour l'authentification
-- ============================================================================

CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    email TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    role_id INTEGER NOT NULL,
    full_name TEXT NOT NULL,
    phone TEXT,
    is_active INTEGER NOT NULL DEFAULT 1 CHECK(is_active IN (0, 1)),
    is_locked INTEGER NOT NULL DEFAULT 0 CHECK(is_locked IN (0, 1)),
    failed_login_attempts INTEGER NOT NULL DEFAULT 0,
    last_login INTEGER,
    auth_token TEXT,
    token_expiry INTEGER,
    created_at INTEGER NOT NULL DEFAULT (strftime('%s', 'now')),
    updated_at INTEGER NOT NULL DEFAULT (strftime('%s', 'now')),
    
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Index pour optimisation des requêtes
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role_id);
CREATE INDEX IF NOT EXISTS idx_users_active ON users(is_active);

-- ============================================================================
-- TABLE : branches
-- Description : Filières d'enseignement (GI, GSTR, ML, GC, etc.)
-- ============================================================================

CREATE TABLE IF NOT EXISTS branches (
    branch_id INTEGER PRIMARY KEY AUTOINCREMENT,
    branch_code TEXT NOT NULL UNIQUE,
    branch_name TEXT NOT NULL,
    branch_name_en TEXT NOT NULL,
    branch_name_fr TEXT NOT NULL,
    cycle TEXT NOT NULL CHECK(cycle IN ('PREPARATORY', 'ENGINEERING')),
    capacity INTEGER NOT NULL DEFAULT 30,
    duration_years INTEGER NOT NULL CHECK(duration_years IN (2, 3)),
    description TEXT,
    is_active INTEGER NOT NULL DEFAULT 1 CHECK(is_active IN (0, 1)),
    created_at INTEGER NOT NULL DEFAULT (strftime('%s', 'now')),
    updated_at INTEGER NOT NULL DEFAULT (strftime('%s', 'now'))
);

-- Index pour recherche par code et cycle
CREATE INDEX IF NOT EXISTS idx_branches_code ON branches(branch_code);
CREATE INDEX IF NOT EXISTS idx_branches_cycle ON branches(cycle);
CREATE INDEX IF NOT EXISTS idx_branches_active ON branches(is_active);

-- ============================================================================
-- TABLE : levels
-- Description : Niveaux d'étude (CP1, CP2, CI1, CI2, CI3)
-- ============================================================================

CREATE TABLE IF NOT EXISTS levels (
    level_id INTEGER PRIMARY KEY AUTOINCREMENT,
    level_code TEXT NOT NULL UNIQUE,
    level_name TEXT NOT NULL,
    level_name_en TEXT NOT NULL,
    level_name_fr TEXT NOT NULL,
    year_number INTEGER NOT NULL CHECK(year_number BETWEEN 1 AND 5),
    cycle TEXT NOT NULL CHECK(cycle IN ('PREPARATORY', 'ENGINEERING')),
    required_credits INTEGER NOT NULL DEFAULT 60,
    is_active INTEGER NOT NULL DEFAULT 1 CHECK(is_active IN (0, 1)),
    created_at INTEGER NOT NULL DEFAULT (strftime('%s', 'now')),
    updated_at INTEGER NOT NULL DEFAULT (strftime('%s', 'now'))
);

-- Index pour tri et recherche
CREATE INDEX IF NOT EXISTS idx_levels_code ON levels(level_code);
CREATE INDEX IF NOT EXISTS idx_levels_year ON levels(year_number);
CREATE INDEX IF NOT EXISTS idx_levels_cycle ON levels(cycle);

-- ============================================================================
-- TABLE : groups
-- Description : Groupes pédagogiques (TD, TP, Sections)
-- ============================================================================

CREATE TABLE IF NOT EXISTS groups (
    group_id INTEGER PRIMARY KEY AUTOINCREMENT,
    group_code TEXT NOT NULL UNIQUE,
    group_name TEXT NOT NULL,
    group_type TEXT NOT NULL CHECK(group_type IN ('TD', 'TP', 'COURS', 'SECTION')),
    branch_id INTEGER,
    level_id INTEGER NOT NULL,
    capacity INTEGER NOT NULL DEFAULT 30,
    current_size INTEGER NOT NULL DEFAULT 0,
    academic_year TEXT NOT NULL,
    is_active INTEGER NOT NULL DEFAULT 1 CHECK(is_active IN (0, 1)),
    created_at INTEGER NOT NULL DEFAULT (strftime('%s', 'now')),
    updated_at INTEGER NOT NULL DEFAULT (strftime('%s', 'now')),
    
    FOREIGN KEY (branch_id) REFERENCES branches(branch_id) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (level_id) REFERENCES levels(level_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    
    CHECK (current_size <= capacity)
);

-- Index pour recherche et jointures
CREATE INDEX IF NOT EXISTS idx_groups_code ON groups(group_code);
CREATE INDEX IF NOT EXISTS idx_groups_branch ON groups(branch_id);
CREATE INDEX IF NOT EXISTS idx_groups_level ON groups(level_id);
CREATE INDEX IF NOT EXISTS idx_groups_year ON groups(academic_year);

-- ============================================================================
-- TABLE : teachers
-- Description : Enseignants de l'établissement
-- ============================================================================

CREATE TABLE IF NOT EXISTS teachers (
    teacher_id INTEGER PRIMARY KEY AUTOINCREMENT,
    employee_number TEXT NOT NULL UNIQUE,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    phone TEXT,
    department TEXT,
    specialization TEXT,
    user_id INTEGER,
    is_active INTEGER NOT NULL DEFAULT 1 CHECK(is_active IN (0, 1)),
    created_at INTEGER NOT NULL DEFAULT (strftime('%s', 'now')),
    updated_at INTEGER NOT NULL DEFAULT (strftime('%s', 'now')),
    
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE
);

-- Index pour recherche rapide
CREATE INDEX IF NOT EXISTS idx_teachers_employee ON teachers(employee_number);
CREATE INDEX IF NOT EXISTS idx_teachers_email ON teachers(email);
CREATE INDEX IF NOT EXISTS idx_teachers_user ON teachers(user_id);

-- ============================================================================
-- TABLE : courses
-- Description : Matières/Modules d'enseignement
-- ============================================================================

CREATE TABLE IF NOT EXISTS courses (
    course_id INTEGER PRIMARY KEY AUTOINCREMENT,
    course_code TEXT NOT NULL UNIQUE,
    course_name TEXT NOT NULL,
    course_name_en TEXT NOT NULL,
    course_name_fr TEXT NOT NULL,
    credits INTEGER NOT NULL CHECK(credits BETWEEN 1 AND 10),
    level_id INTEGER NOT NULL,
    branch_id INTEGER,
    semester INTEGER NOT NULL CHECK(semester IN (1, 2)),
    coefficient REAL NOT NULL DEFAULT 1.0,
    is_active INTEGER NOT NULL DEFAULT 1 CHECK(is_active IN (0, 1)),
    created_at INTEGER NOT NULL DEFAULT (strftime('%s', 'now')),
    updated_at INTEGER NOT NULL DEFAULT (strftime('%s', 'now')),
    
    FOREIGN KEY (level_id) REFERENCES levels(level_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (branch_id) REFERENCES branches(branch_id) ON DELETE SET NULL ON UPDATE CASCADE
);

-- Index pour recherche et filtre
CREATE INDEX IF NOT EXISTS idx_courses_code ON courses(course_code);
CREATE INDEX IF NOT EXISTS idx_courses_level ON courses(level_id);
CREATE INDEX IF NOT EXISTS idx_courses_branch ON courses(branch_id);
CREATE INDEX IF NOT EXISTS idx_courses_semester ON courses(semester);

-- ============================================================================
-- TABLE : students
-- Description : Informations complètes des étudiants
-- ============================================================================

CREATE TABLE IF NOT EXISTS students (
    student_id INTEGER PRIMARY KEY AUTOINCREMENT,
    apogee_number TEXT NOT NULL UNIQUE,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    birth_date INTEGER NOT NULL,
    birth_place TEXT NOT NULL,
    nationality TEXT NOT NULL,
    gender TEXT NOT NULL CHECK(gender IN ('M', 'F')),
    email TEXT NOT NULL UNIQUE,
    phone TEXT,
    address TEXT,
    cne TEXT UNIQUE,
    cin TEXT UNIQUE,
    photo_url TEXT,
    branch_id INTEGER,
    level_id INTEGER NOT NULL,
    group_id INTEGER,
    status TEXT NOT NULL DEFAULT 'ACTIVE' CHECK(status IN ('ACTIVE', 'ARCHIVED', 'GRADUATED')),
    origin TEXT NOT NULL CHECK(origin IN ('CONCOURS', 'CNC', 'TRANSFER')),
    enrollment_date INTEGER NOT NULL,
    archive_date INTEGER,
    created_by INTEGER NOT NULL,
    updated_by INTEGER,
    created_at INTEGER NOT NULL DEFAULT (strftime('%s', 'now')),
    updated_at INTEGER NOT NULL DEFAULT (strftime('%s', 'now')),
    is_archived INTEGER NOT NULL DEFAULT 0 CHECK(is_archived IN (0, 1)),
    
    FOREIGN KEY (branch_id) REFERENCES branches(branch_id) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (level_id) REFERENCES levels(level_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (group_id) REFERENCES groups(group_id) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (updated_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE
);

-- Index critiques pour performance
CREATE INDEX IF NOT EXISTS idx_students_apogee ON students(apogee_number);
CREATE INDEX IF NOT EXISTS idx_students_email ON students(email);
CREATE INDEX IF NOT EXISTS idx_students_branch ON students(branch_id);
CREATE INDEX IF NOT EXISTS idx_students_level ON students(level_id);
CREATE INDEX IF NOT EXISTS idx_students_group ON students(group_id);
CREATE INDEX IF NOT EXISTS idx_students_status ON students(status);
CREATE INDEX IF NOT EXISTS idx_students_archived ON students(is_archived);
CREATE INDEX IF NOT EXISTS idx_students_name ON students(last_name, first_name);

-- ============================================================================
-- TABLE : academic_records
-- Description : Dossier académique de chaque étudiant (relation 1:1)
-- ============================================================================

CREATE TABLE IF NOT EXISTS academic_records (
    record_id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id INTEGER NOT NULL UNIQUE,
    academic_year TEXT NOT NULL,
    current_cycle TEXT NOT NULL CHECK(current_cycle IN ('PREPARATORY', 'ENGINEERING')),
    current_year INTEGER NOT NULL CHECK(current_year BETWEEN 1 AND 5),
    cumulative_gpa REAL NOT NULL DEFAULT 0.0 CHECK(cumulative_gpa BETWEEN 0.0 AND 20.0),
    total_credits INTEGER NOT NULL DEFAULT 0,
    progression_status TEXT NOT NULL DEFAULT 'NORMAL' CHECK(progression_status IN ('NORMAL', 'REDOUBLEMENT', 'ABANDON')),
    last_updated INTEGER NOT NULL DEFAULT (strftime('%s', 'now')),
    created_at INTEGER NOT NULL DEFAULT (strftime('%s', 'now')),
    updated_at INTEGER NOT NULL DEFAULT (strftime('%s', 'now')),
    
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Index pour jointure avec students
CREATE INDEX IF NOT EXISTS idx_academic_student ON academic_records(student_id);
CREATE INDEX IF NOT EXISTS idx_academic_year ON academic_records(academic_year);
CREATE INDEX IF NOT EXISTS idx_academic_cycle ON academic_records(current_cycle);

-- ============================================================================
-- TABLE : level_history
-- Description : Historique complet du parcours de l'étudiant niveau par niveau
-- ============================================================================

CREATE TABLE IF NOT EXISTS level_history (
    history_id INTEGER PRIMARY KEY AUTOINCREMENT,
    record_id INTEGER NOT NULL,
    level_id INTEGER NOT NULL,
    academic_year TEXT NOT NULL,
    start_date INTEGER NOT NULL,
    end_date INTEGER,
    result TEXT CHECK(result IN ('PASS', 'FAIL', 'REDOUBLEMENT')),
    average REAL CHECK(average BETWEEN 0.0 AND 20.0),
    remarks TEXT,
    created_at INTEGER NOT NULL DEFAULT (strftime('%s', 'now')),
    
    FOREIGN KEY (record_id) REFERENCES academic_records(record_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (level_id) REFERENCES levels(level_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Index pour requêtes sur historique
CREATE INDEX IF NOT EXISTS idx_history_record ON level_history(record_id);
CREATE INDEX IF NOT EXISTS idx_history_level ON level_history(level_id);
CREATE INDEX IF NOT EXISTS idx_history_year ON level_history(academic_year);

-- ============================================================================
-- TABLE : branch_choices
-- Description : Choix de filières effectués par les étudiants (système de vœux)
-- ============================================================================

CREATE TABLE IF NOT EXISTS branch_choices (
    choice_id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id INTEGER NOT NULL,
    branch_id INTEGER NOT NULL,
    priority INTEGER NOT NULL CHECK(priority BETWEEN 1 AND 5),
    academic_year TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT 'PENDING' CHECK(status IN ('PENDING', 'ACCEPTED', 'REJECTED')),
    submission_date INTEGER NOT NULL,
    decision_date INTEGER,
    created_at INTEGER NOT NULL DEFAULT (strftime('%s', 'now')),
    updated_at INTEGER NOT NULL DEFAULT (strftime('%s', 'now')),
    
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (branch_id) REFERENCES branches(branch_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    
    UNIQUE(student_id, branch_id, academic_year),
    UNIQUE(student_id, priority, academic_year)
);

-- Index pour recherche de choix
CREATE INDEX IF NOT EXISTS idx_choices_student ON branch_choices(student_id);
CREATE INDEX IF NOT EXISTS idx_choices_branch ON branch_choices(branch_id);
CREATE INDEX IF NOT EXISTS idx_choices_year ON branch_choices(academic_year);
CREATE INDEX IF NOT EXISTS idx_choices_status ON branch_choices(status);

-- ============================================================================
-- TABLE : absences
-- Description : Enregistrement des absences des étudiants
-- ============================================================================

CREATE TABLE IF NOT EXISTS absences (
    absence_id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id INTEGER NOT NULL,
    course_id INTEGER NOT NULL,
    teacher_id INTEGER NOT NULL,
    absence_date INTEGER NOT NULL,
    absence_time TEXT NOT NULL,
    duration_hours INTEGER NOT NULL DEFAULT 1,
    session_type TEXT NOT NULL CHECK(session_type IN ('COURS', 'TD', 'TP')),
    status TEXT NOT NULL DEFAULT 'UNJUSTIFIED' CHECK(status IN ('UNJUSTIFIED', 'JUSTIFIED', 'EXCUSED')),
    justification TEXT,
    is_synced INTEGER NOT NULL DEFAULT 0 CHECK(is_synced IN (0, 1)),
    recorded_by INTEGER NOT NULL,
    created_at INTEGER NOT NULL DEFAULT (strftime('%s', 'now')),
    updated_at INTEGER NOT NULL DEFAULT (strftime('%s', 'now')),
    
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (teacher_id) REFERENCES teachers(teacher_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (recorded_by) REFERENCES users(user_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Index critiques pour requêtes fréquentes
CREATE INDEX IF NOT EXISTS idx_absences_student ON absences(student_id);
CREATE INDEX IF NOT EXISTS idx_absences_course ON absences(course_id);
CREATE INDEX IF NOT EXISTS idx_absences_teacher ON absences(teacher_id);
CREATE INDEX IF NOT EXISTS idx_absences_date ON absences(absence_date);
CREATE INDEX IF NOT EXISTS idx_absences_status ON absences(status);
CREATE INDEX IF NOT EXISTS idx_absences_synced ON absences(is_synced);

-- ============================================================================
-- VUES (VIEWS) - Pour simplifier les requêtes fréquentes
-- ============================================================================

-- Vue : Liste complète des étudiants avec informations liées
CREATE VIEW IF NOT EXISTS v_students_full AS
SELECT 
    s.student_id,
    s.apogee_number,
    s.first_name,
    s.last_name,
    s.first_name || ' ' || s.last_name AS full_name,
    s.email,
    s.phone,
    s.gender,
    datetime(s.birth_date, 'unixepoch') AS birth_date_formatted,
    s.nationality,
    b.branch_name,
    b.branch_code,
    l.level_name,
    l.level_code,
    g.group_name,
    s.status,
    s.origin,
    datetime(s.enrollment_date, 'unixepoch') AS enrollment_date_formatted,
    s.is_archived,
    ar.cumulative_gpa,
    ar.total_credits,
    ar.progression_status
FROM students s
LEFT JOIN branches b ON s.branch_id = b.branch_id
LEFT JOIN levels l ON s.level_id = l.level_id
LEFT JOIN groups g ON s.group_id = g.group_id
LEFT JOIN academic_records ar ON s.student_id = ar.student_id;

-- Vue : Statistiques d'absences par étudiant
CREATE VIEW IF NOT EXISTS v_absence_statistics AS
SELECT 
    s.student_id,
    s.apogee_number,
    s.first_name || ' ' || s.last_name AS student_name,
    COUNT(a.absence_id) AS total_absences,
    SUM(CASE WHEN a.status = 'UNJUSTIFIED' THEN 1 ELSE 0 END) AS unjustified_absences,
    SUM(CASE WHEN a.status = 'JUSTIFIED' THEN 1 ELSE 0 END) AS justified_absences,
    SUM(a.duration_hours) AS total_hours_missed
FROM students s
LEFT JOIN absences a ON s.student_id = a.student_id
GROUP BY s.student_id;

-- Vue : Étudiants actifs avec leurs groupes
CREATE VIEW IF NOT EXISTS v_active_students AS
SELECT 
    s.student_id,
    s.apogee_number,
    s.first_name,
    s.last_name,
    s.email,
    b.branch_name,
    l.level_name,
    g.group_name,
    g.group_type
FROM students s
LEFT JOIN branches b ON s.branch_id = b.branch_id
LEFT JOIN levels l ON s.level_id = l.level_id
LEFT JOIN groups g ON s.group_id = g.group_id
WHERE s.is_archived = 0 AND s.status = 'ACTIVE';

-- ============================================================================
-- TRIGGERS - Pour automatisation et intégrité des données
-- ============================================================================

-- Trigger : Met à jour automatically updated_at lors d'une modification
CREATE TRIGGER IF NOT EXISTS trg_students_updated_at
AFTER UPDATE ON students
FOR EACH ROW
BEGIN
    UPDATE students SET updated_at = strftime('%s', 'now') WHERE student_id = NEW.student_id;
END;

CREATE TRIGGER IF NOT EXISTS trg_users_updated_at
AFTER UPDATE ON users
FOR EACH ROW
BEGIN
    UPDATE users SET updated_at = strftime('%s', 'now') WHERE user_id = NEW.user_id;
END;

CREATE TRIGGER IF NOT EXISTS trg_academic_records_updated_at
AFTER UPDATE ON academic_records
FOR EACH ROW
BEGIN
    UPDATE academic_records SET updated_at = strftime('%s', 'now') WHERE record_id = NEW.record_id;
END;

-- Trigger : Synchronise is_archived avec status
CREATE TRIGGER IF NOT EXISTS trg_students_archive_status
AFTER UPDATE OF status ON students
FOR EACH ROW
WHEN NEW.status IN ('ARCHIVED', 'GRADUATED')
BEGIN
    UPDATE students 
    SET is_archived = 1, 
        archive_date = CASE WHEN archive_date IS NULL THEN strftime('%s', 'now') ELSE archive_date END
    WHERE student_id = NEW.student_id;
END;

-- Trigger : Incrémente current_size du groupe lors d'ajout d'étudiant
CREATE TRIGGER IF NOT EXISTS trg_increment_group_size
AFTER UPDATE OF group_id ON students
FOR EACH ROW
WHEN NEW.group_id IS NOT NULL AND (OLD.group_id IS NULL OR OLD.group_id != NEW.group_id)
BEGIN
    UPDATE groups SET current_size = current_size + 1 WHERE group_id = NEW.group_id;
    UPDATE groups SET current_size = current_size - 1 WHERE group_id = OLD.group_id AND OLD.group_id IS NOT NULL;
END;

-- Trigger : Crée automatiquement un academic_record lors de création étudiant
CREATE TRIGGER IF NOT EXISTS trg_create_academic_record
AFTER INSERT ON students
FOR EACH ROW
BEGIN
    INSERT INTO academic_records (
        student_id, 
        academic_year, 
        current_cycle, 
        current_year
    )
    SELECT 
        NEW.student_id,
        strftime('%Y', 'now') || '-' || strftime('%Y', 'now', '+1 year'),
        l.cycle,
        l.year_number
    FROM levels l
    WHERE l.level_id = NEW.level_id;
END;

-- ============================================================================
-- DONNÉES INITIALES (SEED DATA)
-- ============================================================================

-- Insertion des rôles système
INSERT OR IGNORE INTO roles (role_code, role_name, role_name_en, role_name_fr, description, priority_level) VALUES
('ADMIN', 'Administrateur', 'Administrator', 'Administrateur', 'Administration complète du système', 1),
('SCOLARITE', 'Service Scolarité', 'Registry Service', 'Service Scolarité', 'Gestion administrative des étudiants', 2),
('APOGEE', 'Service APOGÉE', 'APOGÉE Service', 'Service APOGÉE', 'Gestion académique et affectations', 2),
('TEACHER', 'Enseignant', 'Teacher', 'Enseignant', 'Gestion des absences et consultation', 3),
('STUDENT', 'Étudiant', 'Student', 'Étudiant', 'Consultation et modification profil', 4);

-- Insertion des niveaux d'étude
INSERT OR IGNORE INTO levels (level_code, level_name, level_name_en, level_name_fr, year_number, cycle, required_credits) VALUES
('CP1', 'Cycle Préparatoire 1', 'Preparatory Cycle 1', 'Cycle Préparatoire 1', 1, 'PREPARATORY', 60),
('CP2', 'Cycle Préparatoire 2', 'Preparatory Cycle 2', 'Cycle Préparatoire 2', 2, 'PREPARATORY', 60),
('CI1', 'Cycle Ingénieur 1', 'Engineering Cycle 1', 'Cycle Ingénieur 1', 3, 'ENGINEERING', 60),
('CI2', 'Cycle Ingénieur 2', 'Engineering Cycle 2', 'Cycle Ingénieur 2', 4, 'ENGINEERING', 60),
('CI3', 'Cycle Ingénieur 3', 'Engineering Cycle 3', 'Cycle Ingénieur 3', 5, 'ENGINEERING', 60);

-- Insertion des filières (cycle ingénieur uniquement)
INSERT OR IGNORE INTO branches (branch_code, branch_name, branch_name_en, branch_name_fr, cycle, capacity, duration_years, description) VALUES
('GI', 'Génie Informatique', 'Computer Engineering', 'Génie Informatique', 'ENGINEERING', 40, 3, 'Formation en développement logiciel et systèmes informatiques'),
('GSTR', 'Génie des Systèmes de Télécommunications et Réseaux', 'Telecommunications and Networks Engineering', 'Génie des Systèmes de Télécommunications et Réseaux', 'ENGINEERING', 35, 3, 'Formation en réseaux et télécommunications'),
('ML', 'Management de la Chaîne Logistique', 'Supply Chain Management', 'Management de la Chaîne Logistique', 'ENGINEERING', 30, 3, 'Formation en gestion de la supply chain'),
('GM', 'Génie Mécatronique', 'Mechatronics Engineering', 'Génie Mécatronique', 'ENGINEERING', 30, 3, 'Formation en systèmes mécatroniques'),
('GC', 'Génie Civil', 'Civil Engineering', 'Génie Civil', 'ENGINEERING', 35, 3, 'Formation en génie civil et BTP'),
('GCSE', 'Génie Cybersécurité et Systèmes Embarqués', 'Cybersecurity and Embedded Systems Engineering', 'Génie Cybersécurité et Systèmes Embarqués', 'ENGINEERING', 30, 3, 'Formation en sécurité informatique et systèmes embarqués'),
('BDAI', 'Big Data et Intelligence Artificielle', 'Big Data and Artificial Intelligence', 'Big Data et Intelligence Artificielle', 'ENGINEERING', 35, 3, 'Formation en science des données et IA');

-- Insertion d'un utilisateur admin par défaut (mot de passe: admin123 - À CHANGER EN PRODUCTION)
INSERT OR IGNORE INTO users (username, email, password_hash, role_id, full_name, phone) VALUES
('admin', 'admin@ensa.ac.ma', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 1, 'Administrateur Système', '+212600000000');

-- ============================================================================
-- REQUÊTES UTILES POUR LES DÉVELOPPEURS
-- ============================================================================

-- Compter le nombre d'étudiants par filière
-- SELECT b.branch_name, COUNT(s.student_id) as total_students
-- FROM branches b
-- LEFT JOIN students s ON b.branch_id = s.branch_id AND s.is_archived = 0
-- GROUP BY b.branch_id;

-- Lister les étudiants avec leurs absences
-- SELECT s.apogee_number, s.first_name, s.last_name, COUNT(a.absence_id) as total_absences
-- FROM students s
-- LEFT JOIN absences a ON s.student_id = a.student_id
-- GROUP BY s.student_id
-- ORDER BY total_absences DESC;

-- Trouver les groupes avec place disponible
-- SELECT g.group_code, g.group_name, g.capacity, g.current_size, (g.capacity - g.current_size) as places_available
-- FROM groups g
-- WHERE g.is_active = 1 AND g.current_size < g.capacity;

-- Statistiques par niveau
-- SELECT l.level_name, COUNT(s.student_id) as total_students, AVG(ar.cumulative_gpa) as avg_gpa
-- FROM levels l
-- LEFT JOIN students s ON l.level_id = s.level_id AND s.is_archived = 0
-- LEFT JOIN academic_records ar ON s.student_id = ar.student_id
-- GROUP BY l.level_id;

-- ============================================================================
-- FIN DU SCRIPT
-- ============================================================================

-- Vérification de l'intégrité
PRAGMA integrity_check;

-- Afficher les statistiques de la base
-- SELECT name, type FROM sqlite_master WHERE type IN ('table', 'view') ORDER BY type, name;