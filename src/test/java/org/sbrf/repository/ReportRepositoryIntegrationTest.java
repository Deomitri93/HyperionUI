package org.sbrf.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sbrf.entity.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;


@RunWith(SpringRunner.class)
@DataJpaTest(properties = {"spring.flyway.enabled=false", "spring.jpa.hibernate.ddl-auto=create"})
public class ReportRepositoryIntegrationTest {
    private static final String TEST_REPORT_NAME = "TestReport";
    private static final String TEST_ANOTHER_REPORT_NAME = "AnotherTestReport";

    private static final String CUSTOMERS_DEPOSITS_REPORT_SQL_QUERY_PATH = "classpath:sqlQueries/CustomersDeposits.sql";
    private static final String VSP_OPERATIONS_REPORT_SQL_QUERY_PATH = "classpath:sqlQueries/VSPOperations.sql";
    private static final String VSP_OPERATIONS_REPORT_XLS_TEMPLATE = "classpath:xlsTemplates/VSPOperations.xlsx";

    @Value(CUSTOMERS_DEPOSITS_REPORT_SQL_QUERY_PATH)
    private Resource customersDepositsReportSQLQueryResource;

    @Value(VSP_OPERATIONS_REPORT_SQL_QUERY_PATH)
    private Resource vspOperationsReportSQLQueryResource;

    @Value(VSP_OPERATIONS_REPORT_XLS_TEMPLATE)
    private Resource vspOperationsReportXLSTemplateResource;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void whenEntitySaved_thenReturnsCorrectResult() {
        try {
            Report testReport = new Report(
                    TEST_REPORT_NAME,
                    Files.readAllBytes(vspOperationsReportSQLQueryResource.getFile().toPath()),
                    Files.readAllBytes(vspOperationsReportXLSTemplateResource.getFile().toPath()));

            entityManager.persistAndFlush(testReport);

            Report foundReport = reportRepository.findByName(TEST_REPORT_NAME);

            assertThat(foundReport.getSqlQuery()).
                    withFailMessage(String.format("SQL query in report '%s' is not equal to saved one", TEST_REPORT_NAME)).
                    isEqualTo(testReport.getSqlQuery());
            assertThat(foundReport.getXlsTemplate()).
                    withFailMessage(String.format("XLS template in report '%s' is not equal to saved one", TEST_REPORT_NAME)).
                    isEqualTo(testReport.getXlsTemplate());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void whenEntityDeleted_thenReturnsCorrectResult() {
        try {
            Report testReport = new Report(
                    TEST_REPORT_NAME,
                    Files.readAllBytes(vspOperationsReportSQLQueryResource.getFile().toPath()),
                    Files.readAllBytes(vspOperationsReportXLSTemplateResource.getFile().toPath()));

            entityManager.persistAndFlush(testReport);
            long numberOfReportsBeforeDeletion = reportRepository.count();

            entityManager.remove(testReport);
            long numberOfReportsAfterDeletion = reportRepository.count();
            Report found = reportRepository.findByName(testReport.getName());

            assertThat(numberOfReportsBeforeDeletion).
                    withFailMessage(String.format("Number of reports before deletion (%d) is not equal to number of reports after deletion + 1 (%d)", numberOfReportsAfterDeletion, numberOfReportsAfterDeletion + 1)).
                    isEqualTo(numberOfReportsAfterDeletion + 1);
            assertThat(found).
                    withFailMessage(String.format("Found a report with name '%s' after deletion", testReport.getName())).
                    isNull();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void whenFindByName_thenReturnsCorrectResult() {
        try {
            Report testReport = new Report(
                    TEST_REPORT_NAME,
                    Files.readAllBytes(vspOperationsReportSQLQueryResource.getFile().toPath()),
                    Files.readAllBytes(vspOperationsReportXLSTemplateResource.getFile().toPath()));

            entityManager.persistAndFlush(testReport);

            Report found = reportRepository.findByName(testReport.getName());
            assertThat(found.getName()).
                    withFailMessage(String.format("Could not find report by name '%s'", TEST_REPORT_NAME)).
                    isEqualTo(testReport.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void whenFindById_thenReturnsCorrectResult() {
        try {
            Report testReport = new Report(
                    TEST_REPORT_NAME,
                    Files.readAllBytes(vspOperationsReportSQLQueryResource.getFile().toPath()),
                    Files.readAllBytes(vspOperationsReportXLSTemplateResource.getFile().toPath()));

            entityManager.persistAndFlush(testReport);


            Report found = reportRepository.findById(testReport.getId()).orElse(null);
            assertThat(found).
                    withFailMessage("Could not find any report by Id").
                    isNotNull();
            assertThat(found.getName()).
                    withFailMessage("Could not find valid report by Id").
                    isEqualTo(testReport.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void whenFindAll_thenReturnsCorrectResult() {
        try {
            Report testReport = new Report(
                    TEST_REPORT_NAME,
                    Files.readAllBytes(vspOperationsReportSQLQueryResource.getFile().toPath()),
                    Files.readAllBytes(vspOperationsReportXLSTemplateResource.getFile().toPath()));

            Report anotherTestReport = new Report(
                    TEST_ANOTHER_REPORT_NAME,
                    Files.readAllBytes(customersDepositsReportSQLQueryResource.getFile().toPath()),
                    null);

            entityManager.persist(testReport);
            entityManager.persist(anotherTestReport);
            entityManager.flush();

            List<Report> allReports = reportRepository.findAll();

            assertThat(allReports).
                    withFailMessage("Could not find all reports").
                    hasSize(2).
                    extracting(Report::getName).
                    containsOnly(testReport.getName(), anotherTestReport.getName());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
