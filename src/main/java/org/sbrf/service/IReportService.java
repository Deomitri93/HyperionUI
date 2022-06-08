package org.sbrf.service;

import org.sbrf.entity.Report;

import java.util.List;

public interface IReportService {
    void saveReport(Report report);

    Report getReportByName(String reportName);

    Report getReportById(Long reportId);

    List<Report> getAllReports();

    void deleteById(Long reportId);
}
