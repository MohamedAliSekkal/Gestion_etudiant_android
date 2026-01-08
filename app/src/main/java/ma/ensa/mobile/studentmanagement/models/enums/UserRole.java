package ma.ensa.mobile.studentmanagement.models.enums;

public enum UserRole {
    ADMIN("ADMIN", "Administrateur"),
    SCOLARITE("SCOLARITE", "Service Scolarité"),
    APOGEE("APOGEE", "Service APOGÉE"),
    TEACHER("TEACHER", "Enseignant"),
    STUDENT("STUDENT", "Étudiant");

    private final String code;
    private final String displayName;

    UserRole(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }
}