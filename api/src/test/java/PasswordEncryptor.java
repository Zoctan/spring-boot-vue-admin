import com.zoctan.api.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PasswordEncryptor {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void encode() throws Exception {
        System.err.println("admin123 password = " + this.passwordEncoder.encode("admin123"));
        System.err.println("user123 password = " + this.passwordEncoder.encode("user123"));
        System.err.println("test123 password = " + this.passwordEncoder.encode("test123"));
    }
}