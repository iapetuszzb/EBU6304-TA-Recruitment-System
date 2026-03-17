package app.model;

public enum ApplicationStatus {
    SUBMITTED,
    SHORTLISTED,
    REJECTED;

    public static ApplicationStatus from(String s) {
        if (s == null) return null;
        return ApplicationStatus.valueOf(s.trim().toUpperCase());
    }
}

