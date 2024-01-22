package odas.service;

import jakarta.transaction.Transactional;
import odas.model.Account;
import odas.model.Transaction;
import odas.model.User;
import odas.repository.AccountRepository;
import odas.repository.TransactionRepository;
import odas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;

    public Account getAccountDetailsByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return accountRepository.findByUserId(user.getId()).orElse(null);
        } else {
            return null;
        }
    }
    public String createAccountForUser(String username, String accountNumber) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (accountRepository.findByUserId(user.getId()).isPresent()) {
                return "You have an account already.";
            } else if (accountRepository.existsByAccountNumber(accountNumber)) {
                return "There is already an account with that number.";
            } else {
                Account newAccount = new Account(accountNumber, new BigDecimal("1000.00"), user);
                accountRepository.save(newAccount);
                return "Your account has been created successfully.";
            }
        } else {
            return "User not found.";
        }
    }
    @Transactional
    public String transferFunds(String sourceUsername, String targetAccountNumber, BigDecimal amount, String title) {

        Account sourceAccount = getAccountDetailsByUsername(sourceUsername);

        if (sourceAccount == null || !sourceAccount.getAccountNumber().matches("\\d{9}")) {
            return "Source account not found.";
        }
        if (!targetAccountNumber.matches("\\d{9}")) {
            return "Error: Target Account Number must be a 9-digit number.";
        }
        Optional<Account> targetAccountOptional = accountRepository.findByAccountNumber(targetAccountNumber);
        if (!targetAccountOptional.isPresent()) {
            return "Target account not found.";
        }
        Account targetAccount = targetAccountOptional.get();
        if (sourceAccount.getAccountNumber().equals(targetAccount.getAccountNumber())) {
            return "Cannot transfer to the same account.";
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return "Error: The transaction amount must be greater than 0.";
        }
        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            return "Insufficient funds.";
        }
        if (title == null || title.length() < 1 || title.length() > 50) {
            return "Error: Transfer title must be between 1 and 50 characters.";
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        targetAccount.setBalance(targetAccount.getBalance().add(amount));

        Transaction debitTransaction = new Transaction(sourceAccount, amount.negate(), title, targetAccountNumber);
        transactionRepository.save(debitTransaction);

        Transaction creditTransaction = new Transaction(targetAccount, amount, title, sourceAccount.getAccountNumber());
        transactionRepository.save(creditTransaction);

        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        return "Transaction completed successfully.";
    }
}
