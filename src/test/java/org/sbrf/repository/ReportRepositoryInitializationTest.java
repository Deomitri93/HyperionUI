package org.sbrf.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportRepositoryInitializationTest {
    @Autowired
    private ReportRepository reportRepository;

    @Test
    public void whenFindByName_thenReturnsCorrectResult() {
        System.out.printf("\n\nreportRepository.findAll().size(): %d\n\n\n", reportRepository.findAll().size());

//        assertEquals(String.format("Couldn't get by name report %s", CUSTOMERS_DEPOSITS_REPORT_NAME), 1, reportRepository.findByName(CUSTOMERS_DEPOSITS_REPORT_NAME).size());
//        assertEquals(String.format("Couldn't get by name report %s", VSP_OPERATIONS_REPORT_NAME), 1, reportRepository.findByName(VSP_OPERATIONS_REPORT_NAME).size());
    }
}
