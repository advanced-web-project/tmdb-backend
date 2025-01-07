package com.movie.tdmb.service;
import com.movie.tdmb.dto.SignUpDto;
import com.movie.tdmb.model.User;
import com.movie.tdmb.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser() {
        // Arrange
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setUsername("testuser");
        signUpDto.setEmail("testuser@example.com");
        signUpDto.setPassword("password");
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password");
        when(userRepository.save(any(User.class))).thenReturn(user);
        User registeredUser = authService.registerUser(signUpDto);
        // Assert
        assertEquals("testuser", registeredUser.getUsername());
        assertEquals("testuser@example.com", registeredUser.getEmail());

    }
}