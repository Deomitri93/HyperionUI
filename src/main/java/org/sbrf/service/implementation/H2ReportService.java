package org.sbrf.service.implementation;

import org.sbrf.entity.Report;
import org.sbrf.repository.ReportRepository;
import org.sbrf.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class H2ReportService implements IReportService {
    @Autowired
    private ReportRepository reportRepository;

    @Override
    public void saveReport(Report report) {
        reportRepository.save(report);
    }

    @Override
    public Report getReportByName(String reportName) {
        return reportRepository.findByName(reportName);
    }

    @Override
    public Report getReportById(Long reportId) {
//        return reportRepository.getOne(reportId);
        Report report = reportRepository.findById(reportId).orElse(null);
        if(report == null){
            System.out.printf("\n\nreport == null\n\n\n");
        } else {
            System.out.printf("\n\nreport != null\n\n\n ");
        }
        return report;
    }

    @Override
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    @Override
    public void deleteById(Long reportId) {
        reportRepository.deleteById(reportId);
    }
}
