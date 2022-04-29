package org.sbrf.repository;

import org.sbrf.entity.Report;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReportRepository extends CrudRepository<Report, Long> {
    List<Report> findAll();

    List<Report> findByName(String name);
}
