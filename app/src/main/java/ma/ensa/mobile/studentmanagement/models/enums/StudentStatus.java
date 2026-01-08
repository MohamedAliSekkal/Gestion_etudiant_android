package ma.ensa.mobile.studentmanagement.models.enums;

public enum StudentStatus {
    ACTIVE("Active", "Actif"),
    ARCHIVED("Archived", "Archivé"),
    GRADUATED("Graduated", "Diplômé");

    private final String nameEn;
    private final String nameFr;

    StudentStatus(String nameEn, String nameFr) {
        this.nameEn = nameEn;
        this.nameFr = nameFr;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getNameFr() {
        return nameFr;
    }
}