package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class V1__create_tables extends BaseJavaMigration {
    private static final String CREATE_SEQUENCE = "CREATE SEQUENCE hibernate_sequence START WITH 1 INCREMENT BY 1";
    private static final String CREATE_REPORT_PARAMETERS_TABLE = "CREATE TABLE report_parameters (" +
            "   report_id BIGINT NOT NULL," +
            "   parameters VARCHAR(255)" +
            ");";
    private static final String CREATE_REPORTS_TABLE = "CREATE TABLE reports (" +
            "   report_id BIGINT NOT NULL," +
            "   name VARCHAR(255)," +
            "   sql_query BLOB," +
            "   xls_template BLOB," +
            "   PRIMARY KEY (report_id)" +
            ");";
    private static final String ADD_CONSTRAINT = "ALTER TABLE report_parameters ADD CONSTRAINT FKk53e7d9p0k9j8hkavnhpjhm6a FOREIGN KEY (report_id) REFERENCES reports;";

    @Override
    public void migrate(Context context) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true));

        jdbcTemplate.execute(CREATE_SEQUENCE);
        jdbcTemplate.execute(CREATE_REPORT_PARAMETERS_TABLE);
        jdbcTemplate.execute(CREATE_REPORTS_TABLE);
        jdbcTemplate.execute(ADD_CONSTRAINT);
    }
}
