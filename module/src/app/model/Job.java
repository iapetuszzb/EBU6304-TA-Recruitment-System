package app.model;

public class Job {
    private final String jobId;
    private String title;
    private String description;
    private String requiredSkills; // comma separated
    private final String moUsername;
    private boolean open;

    public Job(String jobId, String title, String description, String requiredSkills, String moUsername, boolean open) {
        this.jobId = jobId;
        this.title = title;
        this.description = description;
        this.requiredSkills = requiredSkills;
        this.moUsername = moUsername;
        this.open = open;
    }

    public String getJobId() {
        return jobId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(String requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public String getMoUsername() {
        return moUsername;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}

