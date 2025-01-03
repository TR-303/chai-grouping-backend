package chaigrouping;

import com.tongji.chaigrouping.aiservice.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@SpringBootTest(classes = MoonshotTest.class)
@ContextConfiguration(classes = AiServiceConfig.class)
public class MoonshotTest {
    @Autowired
    private MoonshotAiUtils moonshotAiUtils;

    @Test
    public void testMatchGroup() {
        MatchGroupRequest matchGroupRequest = new MatchGroupRequest(
                new MatchGroupRequest.ResumeItem("同济大学", "大一", "擅长写C#后端"),
                List.of(
                        new MatchGroupRequest.GroupItem(7, "软件工程期末项目开发", "开发选题是一个基于微服务架构的校园社交平台",
                                List.of(new MatchGroupRequest.ResumeItem("复旦大学", "大二", "对微服务有很多经验"),
                                        new MatchGroupRequest.ResumeItem("交通大学", "大二", "擅长java技术栈"))),
                        new MatchGroupRequest.GroupItem(99, ".NET框架课程大项目", "二手交易平台",
                                List.of(new MatchGroupRequest.ResumeItem("同济大学", "大一", "擅长管理数据库"),
                                        new MatchGroupRequest.ResumeItem("同济大学", "大一", "会微服务"))
                        )
                ));
        List<MatchGroupResponseItem> result = moonshotAiUtils.matchGroup(matchGroupRequest);
        for (MatchGroupResponseItem item : result) {
            System.out.println(item.getGroup_id() + " " + item.getReason() + " " + item.getRating());
        }
    }

    @Test
    public void testMatchTask() {
        TaskMatchRequest taskMatchRequest = new TaskMatchRequest("部署我们的项目到云端",
                List.of(new TaskMatchRequest.CandidateItem(1, "擅长vue框架"),
                        new TaskMatchRequest.CandidateItem(2, "擅长微服务的部署")));
        TaskMatchResponse result = moonshotAiUtils.matchTask(taskMatchRequest);
        System.out.println(result.getUser_id() + " " + result.getReason());
    }

}
