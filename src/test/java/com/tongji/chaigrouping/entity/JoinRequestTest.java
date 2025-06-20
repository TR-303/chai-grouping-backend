package com.tongji.chaigrouping.entity;

import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class JoinRequestTest {
    @Test
    void testAllArgsConstructor() {
        Date now = new Date();
        JoinRequest jr = new JoinRequest(1,2,3,now,"desc","PENDING");
        assertEquals(1, jr.getJoinRequestId());
        assertEquals(2, jr.getUserId());
        assertEquals(3, jr.getGroupId());
        assertEquals(now, jr.getCreationTime());
        assertEquals("desc", jr.getDescription());
        assertEquals("PENDING", jr.getState());
    }
}
