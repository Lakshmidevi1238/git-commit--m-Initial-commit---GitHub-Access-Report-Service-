package com.githubreport.github_access_report.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import org.springframework.stereotype.Service;

import com.githubreport.github_access_report.github.GithubClient;
import com.githubreport.github_access_report.model.Collaborator;
import com.githubreport.github_access_report.model.Repository;
import com.githubreport.github_access_report.model.RepoAccess;

@Service
public class AccessReportService {

    private final GithubClient githubClient;

    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    public AccessReportService(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    public Map<String, List<RepoAccess>> generateReport(String org) {

        List<Repository> repos = githubClient.getOrganizationRepositories(org);

        Map<String, List<RepoAccess>> userRepoMap = new ConcurrentHashMap<>();

        List<CompletableFuture<Void>> futures = repos.stream()
                .map(repo -> CompletableFuture.runAsync(() -> {

                	List<Collaborator> collaborators;

                	try {
                	    collaborators = githubClient.getRepoCollaborators(org, repo.getName());
                	} catch (Exception e) {
                	    System.out.println("Skipping repo (no permission): " + repo.getName());
                	    return;
                	}

                    for (Collaborator collaborator : collaborators) {

                        String username = collaborator.getLogin();

                        String permission = getPermission(collaborator);

                        RepoAccess access =
                                new RepoAccess(repo.getName(), permission);

                        userRepoMap
                                .computeIfAbsent(username, k -> new ArrayList<>())
                                .add(access);
                    }

                }, executor))
                .toList();

        CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        ).join();

        return userRepoMap;
    }

    private String getPermission(Collaborator collaborator) {

        if (collaborator.getPermissions().isAdmin()) {
            return "admin";
        }

        if (collaborator.getPermissions().isPush()) {
            return "write";
        }

        return "read";
    }
}