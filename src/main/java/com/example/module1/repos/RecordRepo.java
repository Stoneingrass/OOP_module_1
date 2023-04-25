package com.example.module1.repos;

import com.example.module1.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepo extends JpaRepository<Record, Long> {
}
