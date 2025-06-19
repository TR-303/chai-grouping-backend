import com.tongji.chaigrouping.dto.AccountInfoDto;
import com.tongji.chaigrouping.dto.ResumeDto;
import com.tongji.chaigrouping.entity.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    void testResumeGetSet() {
        User user = new User();
        ResumeDto resume = new ResumeDto();
        resume.setSchool("A");
        resume.setGrade("B");
        resume.setSkillDescription("C");
        user.setResume(resume);

        ResumeDto returned = user.getResume();
        assertEquals("A", returned.getSchool());
        assertEquals("B", returned.getGrade());
        assertEquals("C", returned.getSkillDescription());
    }

    @Test
    void testAccountInfoGetSet() {
        User user = new User();
        AccountInfoDto dto = new AccountInfoDto();
        dto.setUsername("u");
        dto.setProfile("p");
        user.setAccountInfo(dto);

        AccountInfoDto out = user.getAccountInfo();
        assertEquals("u", out.getUsername());
        assertEquals("p", out.getProfile());
    }
}
