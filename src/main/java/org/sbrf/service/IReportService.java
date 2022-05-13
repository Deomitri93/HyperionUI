package org.sbrf.service;

import org.sbrf.entity.Report;

import java.util.List;

public interface IReportService {
    void saveReport(Report report);

    Report getReportByName(String reportName);

    List<Report> getAllReports();
}
