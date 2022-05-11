package org.sbrf.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sbrf.entity.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportRepositoryIntegrationTest {
    private static final String CUSTOMERS_DEPOSITS_REPORT_NAME = "CustomersDeposits";
    private static final String VSP_OPERATIONS_REPORT_NAME = "VSPOperations";

    private static final String CUSTOMERS_DEPOSITS_REPORT_SQL_QUERY_PATH = "classpath:sqlQueries/CustomersDeposits.sql";
    private static final String VSP_OPERATIONS_REPORT_SQL_QUERY_PATH = "classpath:sqlQueries/VSPOperations.sql";
    private static final String VSP_OPERATIONS_REPORT_XLS_TEMPLATE = "classpath:xlsTemplates/VSPOperations.xlsx";

    @Autowired
    private ReportRepository reportRepository;

    @Value(CUSTOMERS_DEPOSITS_REPORT_SQL_QUERY_PATH)
    private Resource customersDepositsReportSQLQueryResource;

    @Value(VSP_OPERATIONS_REPORT_SQL_QUERY_PATH)
    private Resource vspOperationsReportSQLQueryResource;

    @Value(VSP_OPERATIONS_REPORT_XLS_TEMPLATE)
    private Resource vspOperationsReportXLSTemplateResource;

    private List<Report> reports;

    private void initReports() {
        try {
            reports = new ArrayList<>();

            reports.add(new Report(
                    CUSTOMERS_DEPOSITS_REPORT_NAME,
                    Files.readAllBytes(customersDepositsReportSQLQueryResource.getFile().toPath()),
                    null));
            reports.add(new Report(
                    VSP_OPERATIONS_REPORT_NAME,
                    Files.readAllBytes(vspOperationsReportSQLQueryResource.getFile().toPath()),
                    Files.readAllBytes(vspOperationsReportXLSTemplateResource.getFile().toPath())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() {
        initReports();
//
//        reports.forEach(report -> reportRepository.save(report));
    }

    @After
    public void tearDown() {
//        reportRepository.deleteAll();
    }

    @Test
    public void whenFindByName_thenReturnsCorrectResult() {
        assertEquals(String.format("Couldn't get by name report %s", CUSTOMERS_DEPOSITS_REPORT_NAME), 1, reportRepository.findByName(CUSTOMERS_DEPOSITS_REPORT_NAME).size());
        assertEquals(String.format("Couldn't get by name report %s", VSP_OPERATIONS_REPORT_NAME), 1, reportRepository.findByName(VSP_OPERATIONS_REPORT_NAME).size());
    }

    @Test
    public void whenFindAll_thenReturnsCorrectResult() {
        long entitiesExpected = 2;
        long entitiesFact = reportRepository.count();

        assertEquals(String.format("Number of entities in repository: Expected - %d, Fact: - %d", entitiesExpected, entitiesFact), entitiesExpected, entitiesFact);
    }

    @Test
    public void whenEntitiesSaved_thenReturnsCorrectResult() {
        List<String> reportsFromRepositoryNames = new ArrayList<>();

        reportRepository.findAll().forEach(report -> reportsFromRepositoryNames.add(report.getName()));
        reports.forEach(report -> assertTrue(String.format("Repository doesn't contain report with name %s", report.getName()), reportsFromRepositoryNames.contains(report.getName())));
    }
}