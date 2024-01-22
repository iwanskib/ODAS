package odas.controller;

import odas.model.User;
import odas.repository.UserRepository;
import odas.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.security.Principal;
import java.util.regex.Pattern;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("confirmPassword") String confirmPassword,
                               @RequestParam("email") String email, Model model) {
        if (userRepository.existsByUsername(username)) {
            model.addAttribute("error", "Forbidden username!");
            return "login";
        }
        if (!isValidUsername(username)) {
            model.addAttribute("error", "Username has to be 4-20 letters long.");
            return "login";
        }
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords don't match!");
            return "login";
        }
        if (!isPasswordValid(password) || !isPasswordValid(confirmPassword)) {
            model.addAttribute("error", "Password does not meet the security requirements. 8 character minimum, one lower letter, one upper letter, one digit and one special sign (@#$%^&+=) needed.");
            return "login";
        }
        if (!isValidEmail(email)) {
            model.addAttribute("error", "Wrong e-mail formula.");
            return "login";
        }
        User newUser = new User(username, passwordEncoder.encode(password), email);
        userRepository.save(newUser);
        return "redirect:/login";
    }
    private boolean isValidUsername(String username) {
        String usernamePattern = "^[a-zA-Z]{4,20}$";
        return Pattern.matches(usernamePattern, username);
    }
    private boolean isPasswordValid(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,60}$";
        return Pattern.matches(passwordPattern, password);
    }
    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return Pattern.matches(emailPattern, email);
    }
    @PostMapping("/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Principal principal, Model model) {
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (user == null || !passwordService.matches(currentPassword, user.getPassword())) {
            model.addAttribute("passwordChangeError", "Invalid current password.");
            return "dashboard";
        }
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("passwordChangeError", "New passwords do not match.");
            return "dashboard";
        }
        if (!isPasswordValid(newPassword)) {
            model.addAttribute("passwordChangeError", "New password does not meet the security requirements. " +
                    "8 character minimum, one lower letter, one upper letter, one digit and one special sign needed.");
            return "dashboard";
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        model.addAttribute("passwordChangeError", "Password changed.");
        return "dashboard";
    }
}
