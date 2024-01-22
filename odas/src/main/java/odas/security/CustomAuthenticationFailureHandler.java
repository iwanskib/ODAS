package odas.security;

import odas.model.User;
import odas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final int MAX_FAILED_ATTEMPTS = 3;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");

        User user = userRepository.findByUsername(username).orElse(null);
        request.getSession().setAttribute("loginErrorMessage", "Invalid username or password.");

        if (user != null) {
            int failedAttempts = user.getFailedAttemptCount();
            if (failedAttempts < MAX_FAILED_ATTEMPTS - 1) {
                user.setFailedAttemptCount(failedAttempts + 1);
            } else {
                user.setLockTime(LocalDateTime.now());
                String obscuredEmail = obscureEmail(user.getEmail());
                request.getSession().setAttribute("accountLockedMessage", "Mail has been sent to " + obscuredEmail);
            }
            userRepository.save(user);
        }

        super.onAuthenticationFailure(request, response, exception);
    }
    private String obscureEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex < 2) {
            return email;
        }
        return email.substring(0, 2) + "*" + email.substring(atIndex - 2);
    }
}
