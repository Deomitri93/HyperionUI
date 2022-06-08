package org.sbrf.repository;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sbrf.entity.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportRepositoryMigrationTest {
    private static final String CUSTOMERS_DEPOSITS_REPORT_NAME = "CustomersDeposits";
    private static final String VSP_OPERATIONS_REPORT_NAME = "VSPOperations";

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private Flyway flyway;

    public ReportRepositoryMigrationTest() {
    }

    @Test
    public void whenAllMigrationsStatusSuccess_thenReturnsCorrectResult() {
        for (MigrationInfo migrationInfo : flyway.info().all()) {
            System.out.printf("migrationInfo.getState(): %s, expectedState: %s", migrationInfo.getState(), MigrationState.SUCCESS);
            assertEquals(migrationInfo.getState(), MigrationState.SUCCESS);
        }
    }

    @Test
    public void whenCountCorrect_thenReturnsCorrectResult() {
        long entitiesExpected = 2;
        long entitiesFact = reportRepository.count();

        assertEquals(String.format("Number of entities in repository: Expected - %d, Fact: - %d", entitiesExpected, entitiesFact), entitiesExpected, entitiesFact);
    }
}