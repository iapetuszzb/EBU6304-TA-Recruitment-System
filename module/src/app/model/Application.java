package app.model;

public class Application {
    private final String applicationId;
    private final String jobId;
    private final String taUsername;
    private ApplicationStatus status;
    private final String createdAtIso;

    public Application(String applicationId, String jobId, String taUsername, ApplicationStatus status, String createdAtIso) {
        this.applicationId = applicationId;
        this.jobId = jobId;
        this.taUsername = taUsername;
        this.status = status;
        this.createdAtIso = createdAtIso;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getJobId() {
        return jobId;
    }

    public String getTaUsername() {
        return taUsername;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public String getCreatedAtIso() {
        return createdAtIso;
    }
}

