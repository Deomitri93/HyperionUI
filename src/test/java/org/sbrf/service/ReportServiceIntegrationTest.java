package org.sbrf.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.sbrf.entity.Report;
import org.sbrf.repository.ReportRepository;
import org.sbrf.service.implementation.H2ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class ReportServiceIntegrationTest {
    private static final String TEST_REPORT_NAME = "TestReport";
    private static final String ANOTHER_TEST_REPORT_NAME = "AnotherTestReport";
    private static final String WRONG_REPORT_NAME = "wrongName";

    private static final long TEST_REPORT_ID = 1L;
    private static final long ANOTHER_TEST_REPORT_ID = 2L;
    private static final long WRONG_REPORT_ID = -99L;

    private static final String CUSTOMERS_DEPOSITS_REPORT_SQL_QUERY_PATH = "classpath:sqlQueries/CustomersDeposits.sql";
    private static final String VSP_OPERATIONS_REPORT_SQL_QUERY_PATH = "classpath:sqlQueries/VSPOperations.sql";
    private static final String VSP_OPERATIONS_REPORT_XLS_TEMPLATE = "classpath:xlsTemplates/VSPOperations.xlsx";

    @Value(CUSTOMERS_DEPOSITS_REPORT_SQL_QUERY_PATH)
    private Resource customersDepositsReportSQLQueryResource;

    @Value(VSP_OPERATIONS_REPORT_SQL_QUERY_PATH)
    private Resource vspOperationsReportSQLQueryResource;

    @Value(VSP_OPERATIONS_REPORT_XLS_TEMPLATE)
    private Resource vspOperationsReportXLSTemplateResource;

    @TestConfiguration
    static class ReportServiceImplTestContextConfiguration {
        @Bean
        public IReportService h2ReportService() {
            return new H2ReportService();
        }
    }

    @Autowired
    private IReportService reportService;

    @MockBean
    private ReportRepository reportRepository;

    @Before
    public void setUp() {
        try {
            Report testReport = new Report(
                    TEST_REPORT_NAME,
                    Files.readAllBytes(vspOperationsReportSQLQueryResource.getFile().toPath()),
                    Files.readAllBytes(vspOperationsReportXLSTemplateResource.getFile().toPath()));
            testReport.setId(TEST_REPORT_ID);

            Report anotherTestReport = new Report(
                    ANOTHER_TEST_REPORT_NAME,
                    Files.readAllBytes(customersDepositsReportSQLQueryResource.getFile().toPath()),
                    null);
            anotherTestReport.setId(ANOTHER_TEST_REPORT_ID);

            List<Report> allReports = Arrays.asList(testReport, anotherTestReport);

            Mockito.when(reportRepository.findByName(testReport.getName())).thenReturn(testReport);
            Mockito.when(reportRepository.findByName(anotherTestReport.getName())).thenReturn(anotherTestReport);
            Mockito.when(reportRepository.findByName(WRONG_REPORT_NAME)).thenReturn(null);
            Mockito.when(reportRepository.findById(testReport.getId())).thenReturn(Optional.of(testReport));
            Mockito.when(reportRepository.findAll()).thenReturn(allReports);
            Mockito.when(reportRepository.findById(WRONG_REPORT_ID)).thenReturn(Optional.empty());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void whenValidName_thenEmployeeShouldBeFound() {
        Report found = reportService.getReportByName(TEST_REPORT_NAME);

        assertThat(found.getName()).
                withFailMessage(String.format("Could not find report by name '%s'", TEST_REPORT_NAME)).
                isEqualTo(TEST_REPORT_NAME);
    }


    @Test
    public void whenInValidName_thenEmployeeShouldNotBeFound() {
        Report found = reportService.getReportByName(WRONG_REPORT_NAME);

        assertThat(found).
                withFailMessage(String.format("Found report with name '%s", WRONG_REPORT_NAME)).
                isNull();

        Mockito.
                verify(reportRepository, VerificationModeFactory.times(1)).
                findByName(WRONG_REPORT_NAME);
        Mockito.reset(reportRepository);
    }


    @Test
    public void whenValidId_thenEmployeeShouldBeFound() {
        Report found = reportService.getReportById(TEST_REPORT_ID);

        assertThat(found).
                withFailMessage(String.format("Could not find report by id %d", TEST_REPORT_ID)).
                isNotNull();

        assertThat(found.getName()).
                withFailMessage(String.format("Could not find report by id %d", TEST_REPORT_ID)).
                isEqualTo(TEST_REPORT_NAME);

        Mockito.
                verify(reportRepository, VerificationModeFactory.times(1)).
                findById(Mockito.anyLong());
        Mockito.reset(reportRepository);
    }


    @Test
    public void whenInValidId_thenEmployeeShouldNotBeFound() {
        Report found = reportService.getReportById(WRONG_REPORT_ID);

        assertThat(found).
                withFailMessage(String.format("Found report by wrong id: %d", WRONG_REPORT_ID)).
                isNull();

        Mockito.
                verify(reportRepository, VerificationModeFactory.times(1)).
                findById(Mockito.anyLong());
        Mockito.reset(reportRepository);
    }


    @Test
    public void whenGetAll_thenReturnTwoRecords() {
        try {
            Report testReport = new Report(
                    TEST_REPORT_NAME,
                    Files.readAllBytes(vspOperationsReportSQLQueryResource.getFile().toPath()),
                    Files.readAllBytes(vspOperationsReportXLSTemplateResource.getFile().toPath()));

            Report anotherTestReport = new Report(
                    ANOTHER_TEST_REPORT_NAME,
                    Files.readAllBytes(customersDepositsReportSQLQueryResource.getFile().toPath()),
                    null);

            List<Report> allReports = reportService.getAllReports();
            assertThat(allReports).
                    withFailMessage(String.format("'getAllReports()' returned %d reports instead of %d", allReports.size(), 2)).
                    hasSize(2);

            assertThat(allReports).
                    withFailMessage(String.format("'getAllReports()' didn't return report %s", testReport.getName())).
                    extracting(Report::getName).
                    contains(testReport.getName());

            assertThat(allReports).
                    withFailMessage(String.format("'getAllReports()' didn't return report %s", anotherTestReport.getName())).
                    extracting(Report::getName).
                    contains(anotherTestReport.getName());

            Mockito.
                    verify(reportRepository, VerificationModeFactory.times(1)).
                    findAll();
            Mockito.reset(reportRepository);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
