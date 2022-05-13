package org.sbrf.service.implementation;

import org.sbrf.entity.Report;
import org.sbrf.repository.ReportRepository;
import org.sbrf.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class H2ReportService implements IReportService {
    @Autowired
    private ReportRepository reportRepository;

    @Override
    public void saveReport(Report report) {
        reportRepository.save(report);
    }

    @Override
    public Report getReportByName(String reportName) {
        return reportRepository.findByName(reportName).get(0);
    }

    @Override
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }
}
