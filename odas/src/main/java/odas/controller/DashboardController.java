package odas.controller;

import odas.model.Account;
import odas.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account account = accountService.getAccountDetailsByUsername(username);

        if (account != null) {
            model.addAttribute("accountNumber", account.getAccountNumber());
            model.addAttribute("balance", account.getBalance());
        } else {
            model.addAttribute("message", "You do not have an account yet.");
        }

        return "dashboard";
    }
}
