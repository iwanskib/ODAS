package odas.controller;

import odas.model.Account;
import odas.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/create-account")
    public String createAccount(@RequestParam String accountNumber, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        if (!accountNumber.matches("\\d{9}")) {
            model.addAttribute("message", "Account number must be a 9-digit number.");
        } else {
            String message = accountService.createAccountForUser(username, accountNumber);
            model.addAttribute("message", message);
        }

        Account account = accountService.getAccountDetailsByUsername(username);
        if (account != null) {
            model.addAttribute("accountNumber", account.getAccountNumber());
            model.addAttribute("balance", account.getBalance());
        }

        return "dashboard";
    }
}
