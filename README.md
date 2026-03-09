# GitHub Access Report Service – Design Explanation

## Introduction

Organizations often require visibility into which users have access to which repositories.
This project implements a backend service that integrates with the GitHub API to generate a structured access report for repositories within a given organization.

The system retrieves repositories, determines user access permissions, aggregates the data, and exposes a REST API endpoint that returns the results in JSON format.

---

# System Design

The application follows a layered architecture consisting of:

* Controller Layer
* Service Layer
* GitHub API Client
* Model Layer
* Configuration Layer

The controller exposes the REST API endpoint, the service layer performs aggregation logic, and the GitHub client communicates with the GitHub REST API.

---

# Authentication Strategy

The application authenticates with GitHub using a Personal Access Token (PAT).

This token is configured in the application configuration file and is included in all requests through the Authorization header.

This approach provides a simple and secure mechanism for accessing GitHub organization data.

---

# Repository Retrieval Strategy

Repositories belonging to the organization are retrieved using the GitHub REST API endpoint:

```
GET /orgs/{org}/repos
```

To reduce the number of API calls, the request retrieves up to 100 repositories per page.

---

# Access Detection Strategy

For each repository, the collaborators endpoint is used:

```
GET /repos/{org}/{repo}/collaborators
```

This endpoint provides a list of users with repository access along with their permission levels.

The application extracts the relevant permission information and maps it to simplified roles (admin, write, read).

---

# Aggregation Strategy

The retrieved information is aggregated into a structure that maps users to repositories they can access.

The resulting structure is:

```
Map<String, List<RepoAccess>>
```

This enables efficient representation of user access across multiple repositories.

---

# Scaling Strategy

Organizations may contain hundreds of repositories and thousands of collaborators.

To ensure scalability, the application avoids sequential API calls.

Instead, repository collaborator requests are executed concurrently using a thread pool and CompletableFuture.

This approach significantly reduces total execution time while controlling the number of simultaneous requests.

---

# Future Improvements

Potential improvements include:

* full pagination support for repository and collaborator retrieval
* rate limit monitoring using GitHub API headers
* conditional request caching using ETags
* GitHub App authentication
* centralized exception handling

---

# Conclusion

This implementation successfully retrieves repository access information from GitHub, aggregates user permissions, and exposes the results through a REST API.

The system is designed to scale for organizations with large numbers of repositories while maintaining clean architecture and maintainable code.


Sample Output Tested in Postman: <img width="854" height="835" alt="image" src="https://github.com/user-attachments/assets/53e57f46-128f-407e-a6b1-c86b3d8fc846" />



