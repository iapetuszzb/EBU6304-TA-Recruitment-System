package app.model;

public class TaProfile {
    private final String username;
    private String fullName;
    private String email;
    private String skills; // comma separated
    private String resumePath; // file path under data/resumes

    public TaProfile(String username, String fullName, String email, String skills, String resumePath) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.skills = skills;
        this.resumePath = resumePath;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getResumePath() {
        return resumePath;
    }

    public void setResumePath(String resumePath) {
        this.resumePath = resumePath;
    }
}

