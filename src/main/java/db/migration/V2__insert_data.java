package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.io.IOException;
import java.nio.file.Files;


public class V2__insert_data extends BaseJavaMigration {
    private static final String CUSTOMERS_DEPOSITS_REPORT_NAME = "CustomersDeposits";
    private static final String VSP_OPERATIONS_REPORT_NAME = "VSPOperations";

    private static final String CUSTOMERS_DEPOSITS_REPORT_SQL_QUERY_PATH = "sqlQueries/CustomersDeposits.sql";
    private static final String VSP_OPERATIONS_REPORT_SQL_QUERY_PATH = "sqlQueries/VSPOperations.sql";
    private static final String VSP_OPERATIONS_REPORT_XLS_TEMPLATE = "xlsTemplates/VSPOperations.xlsx";

    private static final String INSERT_REPORT_PARAMETERS = "INSERT INTO report_parameters (report_id, parameters) VALUES (?, ?);";
    private static final String INSERT_REPORT = "INSERT INTO reports (name, sql_query, xls_template, report_id) VALUES (?, ?, ?, ?);";

    @Override
    public void migrate(Context context) {
        byte[] customersDepositsReportSQLQuery = null;
        byte[] VSPOperationsReportSQLQuery = null;
        byte[] VSPOperationsReportXLSTemplate = null;

        try {
            customersDepositsReportSQLQuery = Files.readAllBytes((new ClassPathResource(CUSTOMERS_DEPOSITS_REPORT_SQL_QUERY_PATH)).getFile().toPath());
            VSPOperationsReportSQLQuery = Files.readAllBytes((new ClassPathResource(VSP_OPERATIONS_REPORT_SQL_QUERY_PATH)).getFile().toPath());
            VSPOperationsReportXLSTemplate = Files.readAllBytes((new ClassPathResource(VSP_OPERATIONS_REPORT_XLS_TEMPLATE)).getFile().toPath());
        } catch (IOException e) {
            System.err.println("Couldn't load file from resources");
            e.printStackTrace();
        }


        JdbcTemplate jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true));


        jdbcTemplate.update(INSERT_REPORT, CUSTOMERS_DEPOSITS_REPORT_NAME, customersDepositsReportSQLQuery, null, 1);

        jdbcTemplate.update(INSERT_REPORT_PARAMETERS, 1, "@ID_MEGA");
        jdbcTemplate.update(INSERT_REPORT_PARAMETERS, 1, "@Person_Minor");
        jdbcTemplate.update(INSERT_REPORT_PARAMETERS, 1, "@Person_Major");


        jdbcTemplate.update(INSERT_REPORT, VSP_OPERATIONS_REPORT_NAME, VSPOperationsReportSQLQuery, VSPOperationsReportXLSTemplate, 2);

        jdbcTemplate.update(INSERT_REPORT_PARAMETERS, 2, "@beg_day");
        jdbcTemplate.update(INSERT_REPORT_PARAMETERS, 2, "@snd_day");
        jdbcTemplate.update(INSERT_REPORT_PARAMETERS, 2, "@ID_MEGA");
        jdbcTemplate.update(INSERT_REPORT_PARAMETERS, 2, "@BranchNo");
        jdbcTemplate.update(INSERT_REPORT_PARAMETERS, 2, "@Office");
    }
}
