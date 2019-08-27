import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

public class PwdTest {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Test
    public void test1(){
        String encode = bCryptPasswordEncoder.encode("123");
        System.out.println(encode);
    }
}
