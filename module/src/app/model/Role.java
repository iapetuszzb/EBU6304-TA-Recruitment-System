package app.model;

public enum Role {
    TA,
    MO,
    ADMIN;

    public static Role from(String s) {
        if (s == null) return null;
        return Role.valueOf(s.trim().toUpperCase());
    }
}

