package com.exercise.cs209aspring.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exercise.cs209aspring.entity.GitCommit;
import com.exercise.cs209aspring.mapper.GitCommitMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RequestMapping("/")
@RestController
public class DeveloperController {

    @Resource
    private GitCommitMapper commitBaseMapper;


    @GetMapping("/{owner}/{repo}/developerCount")
    public long developerCount(@PathVariable("owner") String owner,
                               @PathVariable("repo") String repo){
        return commitBaseMapper.selectCount(
                new QueryWrapper<GitCommit>().select("distinct commit_author_name")
                .eq("repo_owner", owner)
                .eq("repo_name", repo));
    }

    @GetMapping("/{owner}/{repo}/mostActive")
    public String mostActive(@PathVariable("owner") String owner,
                               @PathVariable("repo") String repo){
        List<Map<String, Object>> re = commitBaseMapper.selectMaps(
                new QueryWrapper<GitCommit>().select("commit_author_name author, count(*) cnt")
                        .eq("repo_owner", owner)
                        .eq("repo_name", repo)
                        .groupBy("author")
                        .orderByDesc("cnt")
        );

        return re.get(0).get("author").toString();
    }

    @GetMapping("/{owner}/{repo}/mostActiveTop")
    public List<Map<String, Object>> mostActiveTop(@PathVariable("owner") String owner,
                             @PathVariable("repo") String repo,
                             @RequestParam("top") String top){
        int t = Integer.parseInt(top);

        List<Map<String, Object>> re = commitBaseMapper.selectMaps(
                new QueryWrapper<GitCommit>().select("commit_author_name author, count(*) cnt")
                        .eq("repo_owner", owner)
                        .eq("repo_name", repo)
                        .groupBy("author")

                        .orderByDesc("cnt")
        );

        return re.subList(0, t);
    }
}
