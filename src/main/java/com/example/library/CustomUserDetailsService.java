package com.example.library;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Ищем пользователя в базе через ваш репозиторий
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Возвращаем специальный объект UserDetails, который понятен Spring Security
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password("{noop}" + user.getPassword()) // {noop} означает, что пароль хранится в чистом виде без шифрования (для простоты)
                .roles(user.getRole())
                .build();
    }
}


