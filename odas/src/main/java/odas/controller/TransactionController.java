package odas.controller;

import odas.model.Account;
import odas.service.AccountService;
import odas.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;


@Controller
public class TransactionController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/transactions")
    public String showTransactionsPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account account = accountService.getAccountDetailsByUsername(username);
        if (account != null) {
            model.addAttribute("transactions", transactionService.findTransactionsByAccount(account));
            model.addAttribute("balance", account.getBalance());
        }
        if (!model.containsAttribute("message")) {
            model.addAttribute("message", "");
        }
        return "transactions";
    }

    @PostMapping("/process-transaction")
    public String processTransaction(
            @RequestParam("targetAccountNumber") String targetAccountNumber,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("title") String title,
            RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        if (username != null && auth.isAuthenticated()) {
            String result = accountService.transferFunds(username, targetAccountNumber, amount, title);
            redirectAttributes.addFlashAttribute("message", result);
        }
        else {
            redirectAttributes.addFlashAttribute("message", "You don't have access to make transaction here.");
        }

        return "redirect:/transactions";
    }
}
