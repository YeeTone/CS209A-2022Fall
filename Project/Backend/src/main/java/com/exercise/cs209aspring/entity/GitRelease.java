package com.exercise.cs209aspring.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "git_release", schema = "main", catalog = "")
public class GitRelease {
    private Integer releaseId;
    private Date publishedAt;
    private String authorLogin;
    private String releaseName;
    private String repoOwner;
    private String repoName;

    @Id
    @Column(name = "release_id")
    public Integer getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(Integer releaseId) {
        this.releaseId = releaseId;
    }

    @Basic
    @Column(name = "published_at")
    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    @Basic
    @Column(name = "author_login")
    public String getAuthorLogin() {
        return authorLogin;
    }

    public void setAuthorLogin(String authorLogin) {
        this.authorLogin = authorLogin;
    }

    @Basic
    @Column(name = "release_name")
    public String getReleaseName() {
        return releaseName;
    }

    public void setReleaseName(String releaseName) {
        this.releaseName = releaseName;
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

        GitRelease that = (GitRelease) o;

        if (releaseId != null ? !releaseId.equals(that.releaseId) : that.releaseId != null) return false;
        if (publishedAt != null ? !publishedAt.equals(that.publishedAt) : that.publishedAt != null) return false;
        if (authorLogin != null ? !authorLogin.equals(that.authorLogin) : that.authorLogin != null) return false;
        if (releaseName != null ? !releaseName.equals(that.releaseName) : that.releaseName != null) return false;
        if (repoOwner != null ? !repoOwner.equals(that.repoOwner) : that.repoOwner != null) return false;
        if (repoName != null ? !repoName.equals(that.repoName) : that.repoName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = releaseId != null ? releaseId.hashCode() : 0;
        result = 31 * result + (publishedAt != null ? publishedAt.hashCode() : 0);
        result = 31 * result + (authorLogin != null ? authorLogin.hashCode() : 0);
        result = 31 * result + (releaseName != null ? releaseName.hashCode() : 0);
        result = 31 * result + (repoOwner != null ? repoOwner.hashCode() : 0);
        result = 31 * result + (repoName != null ? repoName.hashCode() : 0);
        return result;
    }
}
