package com.exercise.cs209aspring.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "git_issue", schema = "main")
public class GitIssue {
    private Integer issueId;
    private Date createAt;
    private String title;
    private Date closedAt;
    private String creatorLogin;
    private String state;
    private String repoOwner;
    private String repoName;

    @Id
    @Column(name = "issue_id")
    public Integer getIssueId() {
        return issueId;
    }

    public void setIssueId(Integer issueId) {
        this.issueId = issueId;
    }

    @Basic
    @Column(name = "create_at")
    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "closed_at")
    public Date getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Date closedAt) {
        this.closedAt = closedAt;
    }

    @Basic
    @Column(name = "creator_login")
    public String getCreatorLogin() {
        return creatorLogin;
    }

    public void setCreatorLogin(String creatorLogin) {
        this.creatorLogin = creatorLogin;
    }

    @Basic
    @Column(name = "state")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GitIssue gitIssue = (GitIssue) o;

        if (issueId != null ? !issueId.equals(gitIssue.issueId) : gitIssue.issueId != null) return false;
        if (createAt != null ? !createAt.equals(gitIssue.createAt) : gitIssue.createAt != null) return false;
        if (title != null ? !title.equals(gitIssue.title) : gitIssue.title != null) return false;
        if (closedAt != null ? !closedAt.equals(gitIssue.closedAt) : gitIssue.closedAt != null) return false;
        if (creatorLogin != null ? !creatorLogin.equals(gitIssue.creatorLogin) : gitIssue.creatorLogin != null)
            return false;
        if (state != null ? !state.equals(gitIssue.state) : gitIssue.state != null) return false;
        if (repoOwner != null ? !repoOwner.equals(gitIssue.repoOwner) : gitIssue.repoOwner != null) return false;
        if (repoName != null ? !repoName.equals(gitIssue.repoName) : gitIssue.repoName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = issueId != null ? issueId.hashCode() : 0;
        result = 31 * result + (createAt != null ? createAt.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (closedAt != null ? closedAt.hashCode() : 0);
        result = 31 * result + (creatorLogin != null ? creatorLogin.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (repoOwner != null ? repoOwner.hashCode() : 0);
        result = 31 * result + (repoName != null ? repoName.hashCode() : 0);
        return result;
    }
}
