package com.exercise.cs209aspring.entity;

import javax.persistence.*;

@Entity
@Table(name = "git_hotword", schema = "main")
public class GitHotword {
    @Basic
    @Column(name = "type")
    private String type;
    @Basic
    @Column(name = "word")
    private String word;
    @Basic
    @Column(name = "repo_owner")
    private String repoOwner;
    @Basic
    @Column(name = "repo_name")
    private String repoName;
    @Id
    private Long id;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getRepoOwner() {
        return repoOwner;
    }

    public void setRepoOwner(String repoOwner) {
        this.repoOwner = repoOwner;
    }

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

        GitHotword that = (GitHotword) o;

        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (word != null ? !word.equals(that.word) : that.word != null) return false;
        if (repoOwner != null ? !repoOwner.equals(that.repoOwner) : that.repoOwner != null) return false;
        if (repoName != null ? !repoName.equals(that.repoName) : that.repoName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (word != null ? word.hashCode() : 0);
        result = 31 * result + (repoOwner != null ? repoOwner.hashCode() : 0);
        result = 31 * result + (repoName != null ? repoName.hashCode() : 0);
        return result;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
