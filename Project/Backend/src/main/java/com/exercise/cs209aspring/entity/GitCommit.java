package com.exercise.cs209aspring.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "git_commit", schema = "main")
public class GitCommit {
    private String commitSha;
    private String commitAuthorName;
    private Date commitDate;
    private String repoOwner;
    private String repoName;
    private String dayTimeLabel;

    private String weekTimeLabel;

    @Id
    @Column(name = "commit_sha")
    public String getCommitSha() {
        return commitSha;
    }

    public void setCommitSha(String commitSha) {
        this.commitSha = commitSha;
    }

    @Basic
    @Column(name = "commit_author_name")
    public String getCommitAuthorName() {
        return commitAuthorName;
    }

    public void setCommitAuthorName(String commitAuthorName) {
        this.commitAuthorName = commitAuthorName;
    }

    @Basic
    @Column(name = "commit_date")
    public Date getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(Date commitDate) {
        this.commitDate = commitDate;
    }

    @Basic
    @Column(name = "repo_owner")
    public String getRepoOwner() {
        return repoOwner;
    }

    public void setRepoOwner(String repoOwner) {
        this.repoOwner = repoOwner;
    }

    @Basic
    @Column(name = "repo_name")
    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    @Basic
    @Column(name = "daytime_label")
    public String getDayTimeLabel() {
        return dayTimeLabel;
    }

    public void setDayTimeLabel(String dayTimeLabel) {
        this.dayTimeLabel = dayTimeLabel;
    }

    @Basic
    @Column(name = "weektime_label")
    public String getWeekTimeLabel() {
        return weekTimeLabel;
    }

    public void setWeekTimeLabel(String weekTimeLabel) {
        this.weekTimeLabel = weekTimeLabel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GitCommit gitCommit = (GitCommit) o;

        if (commitSha != null ? !commitSha.equals(gitCommit.commitSha) : gitCommit.commitSha != null) return false;
        if (commitAuthorName != null ? !commitAuthorName.equals(gitCommit.commitAuthorName) : gitCommit.commitAuthorName != null)
            return false;
        if (commitDate != null ? !commitDate.equals(gitCommit.commitDate) : gitCommit.commitDate != null) return false;
        if (repoOwner != null ? !repoOwner.equals(gitCommit.repoOwner) : gitCommit.repoOwner != null) return false;
        if (repoName != null ? !repoName.equals(gitCommit.repoName) : gitCommit.repoName != null) return false;
        if (dayTimeLabel != null ? !dayTimeLabel.equals(gitCommit.dayTimeLabel) : gitCommit.dayTimeLabel != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = commitSha != null ? commitSha.hashCode() : 0;
        result = 31 * result + (commitAuthorName != null ? commitAuthorName.hashCode() : 0);
        result = 31 * result + (commitDate != null ? commitDate.hashCode() : 0);
        result = 31 * result + (repoOwner != null ? repoOwner.hashCode() : 0);
        result = 31 * result + (repoName != null ? repoName.hashCode() : 0);
        return result;
    }
}
