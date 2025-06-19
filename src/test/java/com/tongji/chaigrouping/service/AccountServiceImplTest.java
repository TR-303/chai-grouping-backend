import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tongji.chaigrouping.dto.AccountInfoDto;
import com.tongji.chaigrouping.entity.User;
import com.tongji.chaigrouping.exception.InvalidAccountInfoException;
import com.tongji.chaigrouping.exception.InvalidUserException;
import com.tongji.chaigrouping.mapper.UserMapper;
import com.tongji.chaigrouping.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceImplTest {
    private UserMapper userMapper;
    private AccountServiceImpl service;

    @BeforeEach
    void setUp() {
        userMapper = mock(UserMapper.class);
        service = new AccountServiceImpl();
        service.userMapper = userMapper;
    }

    @Test
    void testGetAccountInfoNotFound() {
        when(userMapper.selectById(1)).thenReturn(null);
        assertThrows(InvalidUserException.class, () -> service.getAccountInfo(1));
    }

    @Test
    void testUpdateAccountInfoDuplicateUsername() {
        User user = new User();
        when(userMapper.selectById(1)).thenReturn(user);
        when(userMapper.exists(any(QueryWrapper.class))).thenReturn(true);
        AccountInfoDto dto = new AccountInfoDto();
        dto.setUsername("u");
        assertThrows(InvalidAccountInfoException.class, () -> service.updateAccountInfo(1,dto));
    }

    @Test
    void testUpdateAccountInfoSuccess() {
        User user = new User();
        when(userMapper.selectById(1)).thenReturn(user);
        when(userMapper.exists(any(QueryWrapper.class))).thenReturn(false);
        AccountInfoDto dto = new AccountInfoDto();
        dto.setUsername("u");
        service.updateAccountInfo(1,dto);
        verify(userMapper).updateById(user);
    }
}
