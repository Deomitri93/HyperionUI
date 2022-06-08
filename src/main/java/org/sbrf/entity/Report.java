package org.sbrf.entity;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.sbrf.util.Utils.parseSQLToFindParameters;


@Entity
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "report_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "sqlQuery")
    byte[] sqlQuery;

    @Lob
    @Column(name = "xlsTemplate")
    byte[] xlsTemplate;

    @ElementCollection
    @CollectionTable(
            name = "report_parameters",
            joinColumns = @JoinColumn(name = "report_id", referencedColumnName = "report_id")
    )
    @Column(name = "parameters")
    List<String> reportParameters;

    public Report() {

    }

    public Report(String name, byte[] sqlQuery, byte[] xlsTemplate) {
        this.name = name;
        this.sqlQuery = sqlQuery;
        this.xlsTemplate = xlsTemplate;
        this.reportParameters = parseSQLToFindParameters(new String(sqlQuery));
    }


    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getSqlQuery() {
        return sqlQuery;
    }

    public void setSqlQuery(byte[] sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public byte[] getXlsTemplate() {
        return xlsTemplate;
    }

    public void setXlsTemplate(byte[] xlsTemplate) {
        this.xlsTemplate = xlsTemplate;
    }

    public List<String> getReportParameters() {
        return reportParameters;
    }

    public void setReportParameters(List<String> reportParameters) {
        this.reportParameters = reportParameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Report)) return false;

        Report report = (Report) o;

        if (!Objects.equals(name, report.name)) return false;
        if (!Arrays.equals(sqlQuery, report.sqlQuery)) return false;
        if (!Arrays.equals(xlsTemplate, report.xlsTemplate)) return false;
        return Objects.equals(reportParameters, report.reportParameters);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(sqlQuery);
        result = 31 * result + Arrays.hashCode(xlsTemplate);
        result = 31 * result + (reportParameters != null ? reportParameters.hashCode() : 0);
        return result;
    }
}
