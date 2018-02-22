import com.zoctan.api.Application;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * jasypt 用于加密配置文件
 * https://github.com/ulisesbocchio/jasypt-spring-boot
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class JasyptStringEncryptor {

    @Autowired
    private StringEncryptor stringEncryptor;

    @Test
    public void encode() throws Exception {
        final String username = this.stringEncryptor.encrypt("root");
        final String password = this.stringEncryptor.encrypt("root");

        System.err.println("username = " + username);
        System.err.println("password = " + password);
    }
}