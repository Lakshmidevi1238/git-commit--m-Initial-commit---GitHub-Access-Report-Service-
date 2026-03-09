package com.githubreport.github_access_report.model;


public class Collaborator {

    private String login;
    private Permissions permissions;

    public Collaborator() {}

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }
}
