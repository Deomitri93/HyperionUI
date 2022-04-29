package org.sbrf.entity;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

import static org.sbrf.util.Utils.parseSQLToFindParameters;


@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    @Column(name = "reportParameters")
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

    public void setId(Long id) {
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
}
