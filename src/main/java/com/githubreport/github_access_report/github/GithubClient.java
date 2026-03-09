package com.githubreport.github_access_report.github;



import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.githubreport.github_access_report.model.Collaborator;
import com.githubreport.github_access_report.model.Repository;



@Component
public class GithubClient {

    private final WebClient webClient;

    @Value("${github.token}")
    private String token;

    public GithubClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<Repository> getOrganizationRepositories(String org) {

        Repository[] repos = webClient.get()
                .uri("/orgs/{org}/repos?per_page=100", org)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(Repository[].class)
                .block();

        return Arrays.asList(repos);
        
    }
    public List<Collaborator> getRepoCollaborators(String org, String repo) {

        Collaborator[] collaborators = webClient.get()
                .uri("/repos/{org}/{repo}/collaborators?affiliation=all&per_page=100", org, repo)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(Collaborator[].class)
                .block();

        return Arrays.asList(collaborators);
    }

}