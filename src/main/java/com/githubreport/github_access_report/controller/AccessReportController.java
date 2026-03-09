package com.githubreport.github_access_report.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.githubreport.github_access_report.model.RepoAccess;
import com.githubreport.github_access_report.service.AccessReportService;

@RestController
public class AccessReportController {

    private final AccessReportService service;

    public AccessReportController(AccessReportService service) {
        this.service = service;
    }

    @GetMapping("/api/access-report/{org}")
    public Map<String, List<RepoAccess>> getAccessReport(
            @PathVariable String org) {

        return service.generateReport(org);
    }
}