package com.subrat.app.demo.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.subrat.app.demo.entities.DbFile;

public interface DbFileRepository extends JpaRepository<DbFile, Long> {

}
