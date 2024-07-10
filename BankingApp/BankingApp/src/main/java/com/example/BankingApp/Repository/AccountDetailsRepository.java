package com.example.BankingApp.Repository;

import com.example.BankingApp.model.AccountDetails;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface AccountDetailsRepository extends CassandraRepository<AccountDetails, String> {
}
