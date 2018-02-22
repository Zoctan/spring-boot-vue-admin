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
        final String admin = this.passwordEncoder.encode("123456");
        final String user = this.passwordEncoder.encode("test");
        System.err.println("123456 password = " + admin);
        System.err.println("test password = " + user);
    }
}