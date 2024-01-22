package odas.service;

import odas.model.User;
import odas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public boolean verifyPassword(String username, String rawPassword) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            return passwordEncoder.matches(rawPassword, user.getPassword());
        }
        return false;
    }

    public boolean changeUserPassword(User user, String newPassword) {
        String hashedNewPassword = encode(newPassword);
        user.setPassword(hashedNewPassword);
        userRepository.save(user);
        return true;
    }
}