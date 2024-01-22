package odas.security;

import jakarta.servlet.ServletException;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;

public class CustomLoginValidationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("/login".equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod())) {

            String username = request.getParameter("username");
            String password = request.getParameter("password");

            if (!isValidUsername(username) || !isValidPassword(password)) {
                // HoneyPot?
                if (!Objects.equals(username, "")) {
                    Random random = new Random();
                    StringBuilder sb = new StringBuilder();
                    char randomChar = username.charAt(0);
                    sb.append(randomChar);
                    randomChar = (char) ('a' + random.nextInt(26));
                    sb.append(randomChar);
                    sb.append('*');
                    randomChar = (char) ('a' + random.nextInt(26));
                    sb.append(randomChar);
                    randomChar = (char) ('a' + random.nextInt(26));
                    sb.append(randomChar);
                    String randomWord = sb.toString();
                    int randomNumber = new Random().nextInt(5) + 1;
                    if (randomNumber == 1) {
                        request.getSession().setAttribute("accountLockedMessage", "Mail has been sent to " + randomWord + "@gmail.com");
                    }
                }
                // End
                request.getSession().setAttribute("loginErrorMessage", "Invalid username or password.");
                response.sendRedirect("/login");
                return;
            }

        }

        filterChain.doFilter(request, response);
    }

    private boolean isValidUsername(String username) {
        String usernamePattern = "^[a-zA-Z]{4,20}$";
        return Pattern.matches(usernamePattern, username);
    }

    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,60}$";
        return Pattern.matches(passwordPattern, password);
    }
}

