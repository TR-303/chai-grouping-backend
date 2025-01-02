package com.tongji.chaigrouping.commonutils.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.tongji.chaigrouping.commonutils.dto.AccountInfoDto;
import com.tongji.chaigrouping.commonutils.dto.ResumeDto;
import lombok.Data;

import static com.baomidou.mybatisplus.annotation.IdType.AUTO;

@Data
public class User {
    @TableId(type = AUTO)
    private Integer userId;
    private String username;
    private String password;
    private String profile;
    private String school;
    private String grade;
    private String skillDescription;

    public ResumeDto getResume() {
        ResumeDto resumeDto = new ResumeDto();
        resumeDto.setSchool(school);
        resumeDto.setGrade(grade);
        resumeDto.setSkillDescription(skillDescription);
        return resumeDto;
    }

    public void setResume(ResumeDto resumeDto) {
        school = resumeDto.getSchool();
        grade = resumeDto.getGrade();
        skillDescription = resumeDto.getSkillDescription();
    }

    public AccountInfoDto getAccountInfo(){
        AccountInfoDto accountInfoDto = new AccountInfoDto();
        accountInfoDto.setUsername(username);
        accountInfoDto.setProfile(profile);
        return accountInfoDto;
    }

    public void setAccountInfo(AccountInfoDto accountInfoDto){
        username = accountInfoDto.getUsername();
        profile = accountInfoDto.getProfile();
    }
}
