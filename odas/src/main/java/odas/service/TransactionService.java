package odas.service;

import odas.model.Account;
import odas.model.Transaction;
import odas.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> findTransactionsByAccount(Account account) {
        return transactionRepository.findByAccountId(account.getId());
    }

}
