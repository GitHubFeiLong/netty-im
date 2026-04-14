import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author cfl 2026/04/13
 */
public class PasswordTest {

    @Test
    void testGenPassword() {
        String password = "123456";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String encode = passwordEncoder.encode(password);
        System.out.println(encode);
        assertTrue(passwordEncoder.matches(password, encode));
    }
}
