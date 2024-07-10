package com.example.BankingApp.Repository;

import com.example.BankingApp.model.Transaction;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends CassandraRepository<Transaction, UUID> {
    @Query("SELECT * FROM transactions WHERE email = ?0 ALLOW FILTERING")
    List<Transaction> getStatementForUser(String email);
}
