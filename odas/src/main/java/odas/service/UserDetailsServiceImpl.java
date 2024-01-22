package odas.service;

import odas.model.User;
import odas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.temporal.ChronoUnit;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        if (!isAccountNonLocked(user)) {
            throw new LockedException("Wait a minute. User account is locked.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    public boolean isAccountNonLocked(User user) {
        if (user.getLockTime() == null) return true;

        long lockDuration = ChronoUnit.SECONDS.between(user.getLockTime(), LocalDateTime.now());
        long LOCK_TIME_DURATION = 60;
        boolean locked = lockDuration < LOCK_TIME_DURATION;

        if (!locked) {
            user.setFailedAttemptCount(0);
            user.setLockTime(null);
            userRepository.save(user);
        }

        return !locked;
    }
}
