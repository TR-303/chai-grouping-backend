package com.tongji.chaigrouping.aiservice;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MoonshotAiUtils {

    private static final String API_KEY = "sk-wev6Wy5U0Nc3AUipEBDKati1F1GyERnwz4R64YTkQxMijlnB";
    private static final String CHAT_COMPLETION_URL = "https://api.moonshot.cn/v1/chat/completions";

    private static final String initPrompt1 = """
            你要为用户寻找合适他的项目小组：
            输入一个 json 对象，格式类似于如下:
            //////////
            {
                "applicator": {
                    "school": "同济大学",
                    "grade": "大一",
                    "skillDescription": "擅长写前端，以及精美的界面设计"
                },
                "groups": [
                    {
                        "group_id": 7,
                        "name": "软件工程期末项目开发",
                        "description": "开发选题是一个基于微服务架构的校园社交平台"
                        "members": [
                            {
                                "school": "复旦大学",
                                "grade": "大二",
                                "skillDescription": "对微服务有很多经验"
                            },
                            {
                                "school": "交通大学",
                                "grade": "大二",
                                "skillDescription": "擅长java技术栈"
                            }
                        ]
                    },
                    {
                        "group_id": 99,
                        "name": "软件工程大项目",
                        "description": "二手交易平台"
                        "members": [
                            {
                                "school": "同济大学",
                                "grade": "大一",
                                "skillDescription": "擅长java spring boot"
                            },
                            {
                                "school": "同济大学",
                                "grade": "大一",
                                "skillDescription": "全能！"
                            }
                        ]
                    },
                    ......
                ]
            }
            //////////
            你应该观察每个小组的描述，和每个小组中已有成员的描述，然后给出适合 applicator 的小组的建议。
            好的匹配规则应该是：applicator 的学校和年级应该与小组中成员的相似，applicator 的技能描述应该与小组中成员的互补，让小组的技能更加全面，覆盖小组目标所需要的技能。
            你返回的必须是json对象，格式类似于如下：
            //////////
            {
                "suggestions":[
                    {
                        "group_id": 99,
                        "reason": "这个小组的成员都是同济大学大一的学生，你们三人的技能包括了前端、后端，可以很好地完成这个项目"
                        "rating": 0.96
                    }，
                    {
                        "group_id": 7,
                        "reason": "你们三人的技能全面，可以很好地完成这个微服务项目"
                        "rating": 0.92
                    },
                    ......
                ]
            }
            //////////
            你应返回最匹配的至多三个组，以及理由和0-1之间的匹配度打分rating，按照 rating 从高到低排序。
            """;

    private static final String initPrompt2 = """
            你要为任务分配合适的负责人：
            输入一个 json 对象，格式类似于如下:
            //////////
            {
                "description": "开发一个基于Spring Boot的微服务应用",
                "candidates": [
                    {
                        "user_id": 1,
                        "skill_description": "擅长Java和Spring Boot"
                    },
                    {
                        "user_id": 2,
                        "skill_description": "熟悉前端开发，擅长React"
                    }
                ]
            }
            //////////
            你应该观察任务的描述，以及每个候选人的技能描述，然后给出适合任务的负责人的建议。
            好的匹配规则应该是：候选人的技能描述应该与任务的描述相符，如果你觉得没有能很好匹配的候选人，你可以选择不推荐任何人。
            你返回的必须是json对象，格式类似于如下：
            //////////
            {
                "user_id": 1,
                "reason": "这个任务需要擅长Java和Spring Boot，此人正好符合要求"
            }
            //////////
            如果不推荐，你返回空对象。
            你返回最推荐的那个人，以及理由。
            """;

    @Autowired
    private RestTemplate restTemplate;  // 注入 RestTemplate

    @SneakyThrows
    private String requestMoonshot(String initPrompt, String questionPrompt) {
        String url = CHAT_COMPLETION_URL;
        String apiKey = API_KEY;

        JSONObject requestBody = JSONUtil.createObj()
                .set("model", "moonshot-v1-8k")
                .set("messages", CollUtil.newArrayList(
                        JSONUtil.createObj().set("role", "system").set("content", initPrompt),
                        JSONUtil.createObj().set("role", "user").set("content", questionPrompt)
                ))
                .set("temperature", 0.3)
                .set("response_format", JSONUtil.createObj().set("type", "json_object"));

        System.out.println(requestBody.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        return response.getBody();
    }

    public List<MatchGroupResponseItem> matchGroup(MatchGroupRequest request) {
        String questionPrompt = JSONUtil.toJsonStr(request);
        String response = requestMoonshot(initPrompt1, questionPrompt);
        JSONObject jsonObject = JSONUtil.parseObj(response);
        String content = jsonObject.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getStr("content");
        JSONArray suggestions = JSONUtil.parseObj(content).getJSONArray("suggestions");
        return suggestions.stream()
                .map(obj -> JSONUtil.toBean((JSONObject) obj, MatchGroupResponseItem.class))
                .collect(Collectors.toList());
    }

    public TaskMatchResponse matchTask(TaskMatchRequest request) {
        String questionPrompt = JSONUtil.toJsonStr(request);
        String response = requestMoonshot(initPrompt2, questionPrompt);
        JSONObject jsonObject = JSONUtil.parseObj(response);
        String content = jsonObject.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getStr("content");
        return JSONUtil.toBean(JSONUtil.parseObj(content), TaskMatchResponse.class);
    }
}
