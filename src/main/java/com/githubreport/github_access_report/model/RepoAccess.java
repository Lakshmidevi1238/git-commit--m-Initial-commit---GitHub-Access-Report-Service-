package com.githubreport.github_access_report.model;

public class RepoAccess {

    private String repoName;
    private String permission;

    public RepoAccess() {}

    public RepoAccess(String repoName, String permission) {
        this.repoName = repoName;
        this.permission = permission;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}