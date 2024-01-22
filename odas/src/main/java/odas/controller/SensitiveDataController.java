package odas.controller;

import odas.dto.SensitiveDataDTO;
import odas.service.SensitiveDataService;
import odas.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@Controller
public class SensitiveDataController {

    @Autowired
    private SensitiveDataService sensitiveDataService;

    @Autowired
    private PasswordService passwordService;

    @GetMapping("/view-sensitive-data")
    public String showSensitiveDataForm(Model model) {
        return "dashboard";
    }


    @PostMapping("/view-sensitive-data")
    public String viewSensitiveData(@RequestParam("encryptionKey") String encryptionKey, Model model) {
        if (isKeyValid(encryptionKey)) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            if (username != null && auth.isAuthenticated()) {
                SensitiveDataDTO sensitiveData = sensitiveDataService.getSensitiveData(username, encryptionKey);
                if (sensitiveData != null) {
                    model.addAttribute("sensitiveData", sensitiveData);
                } else {
                    model.addAttribute("error", "Incorrect encryption key or sensitive data not found.");
                }
            }
            else {
                model.addAttribute("error", "There is no user with this sensitive data.");
            }
        }
        else {
            model.addAttribute("error", "Incorrect encryption key or sensitive data not found.");
        }
        return "dashboard";
    }
    private boolean isKeyValid(String key) {
        String keyPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?=\\S+$).{8,}$";
        return Pattern.matches(keyPattern, key);
    }

}