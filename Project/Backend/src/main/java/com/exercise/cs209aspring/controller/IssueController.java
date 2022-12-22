package com.exercise.cs209aspring.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.exercise.cs209aspring.entity.GitHotword;
import com.exercise.cs209aspring.entity.GitIssue;
import com.exercise.cs209aspring.mapper.GitHotWordMapper;
import com.exercise.cs209aspring.mapper.GitIssueMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RequestMapping("/")
@RestController
public class IssueController {
    @Resource
    private GitIssueMapper issueBaseMapper;

    @Resource
    private GitHotWordMapper gitHotWordMapper;

    @GetMapping("/{owner}/{repo}/issueCount")
    public long issueCount(@PathVariable("owner") String owner,
                           @PathVariable("repo") String repo,
                           @RequestParam("state") String state) {
        return issueBaseMapper.selectCount(
                new QueryWrapper<GitIssue>()
                        .eq("repo_owner", owner)
                        .eq("repo_name", repo)
                        .eq("state", state));

    }

    @GetMapping("/{owner}/{repo}/issueCountTop")
    public List<Map<String, Object>> issueSubmitterTop
            (@PathVariable("owner") String owner,
             @PathVariable("repo") String repo,
             @RequestParam("top") String top) {
        int t = Integer.parseInt(top);
        List<Map<String, Object>> re = issueBaseMapper.selectMaps(
                new QueryWrapper<GitIssue>().select(
                                "creator_login creator, count(*) cnt"
                        ).eq("repo_owner", owner)
                        .eq("repo_name", repo)
                        .groupBy("creator")
                        .orderByDesc("cnt")
        );

        return re.subList(0, t);
    }

    @GetMapping("/{owner}/{repo}/issueResolutionTimeTop")
    public List<Map<String, Object>> issueResolutionTimeTop
            (@PathVariable("owner") String owner,
             @PathVariable("repo") String repo,
             @RequestParam("top") String top) {
        int t = Integer.parseInt(top);
        List<Map<String, Object>> re = issueBaseMapper.selectMaps(
                new QueryWrapper<GitIssue>().select(
                                "issue_id, (closed_at - create_at) / 3600 / 1000 / 24 as days"
                        ).eq("repo_owner", owner)
                        .eq("repo_name", repo)
                        .eq("state", "closed")
                        .isNotNull("closed_at")
                        .orderByDesc("days")
        );

        return re.subList(0, t);
    }

    @GetMapping("/{owner}/{repo}/issueResolutionTimeBetween")
    public long issueResolutionTimeBetween
            (@PathVariable("owner") String owner,
             @PathVariable("repo") String repo,
             @RequestParam("daysLower") String daysLower,
             @RequestParam("daysUpper") String daysUpper) {
        long upper = Long.parseLong(daysUpper), lower = Long.parseLong(daysLower);
        return issueBaseMapper.selectMaps(
                new QueryWrapper<GitIssue>().select(
                                "issue_id, (closed_at - create_at) / 3600 / 1000 / 24 as days"
                        ).eq("repo_owner", owner)
                        .eq("repo_name", repo)
                        .eq("state", "closed")
                        .isNotNull("closed_at")
                        .between("days", lower, upper)
        ).size();
    }

    @GetMapping("/{owner}/{repo}/varIssueResolutionTime")
    public Map<String, Object> varIssueResolutionTime(@PathVariable("owner") String owner,
                                         @PathVariable("repo") String repo){
        return issueBaseMapper.selectMaps(
                new QueryWrapper<GitIssue>().select(
                        "variance((closed_at - create_at) / 3600 / 1000 / 24) as variance"
                ).isNotNull("closed_at")
                        .eq("repo_owner", owner)
                        .eq("repo_name", repo)
        ).get(0);
    }

    @GetMapping("/{owner}/{repo}/rangeIssueResolutionTime")
    public Map<String, Object> rangeIssueResolutionTime(@PathVariable("owner") String owner,
                                                      @PathVariable("repo") String repo){
        return issueBaseMapper.selectMaps(
                new QueryWrapper<GitIssue>().select(
                                "max((closed_at - create_at) / 3600 / 1000 / 24) - min((closed_at - create_at) / 3600 / 1000 / 24) as range"
                        ).isNotNull("closed_at")
                        .eq("repo_owner", owner)
                        .eq("repo_name", repo)
        ).get(0);
    }

    @GetMapping("/{owner}/{repo}/avgIssueResolutionTime")
    public Map<String, Object> avgIssueResolutionTime(@PathVariable("owner") String owner,
                                                      @PathVariable("repo") String repo){
        return issueBaseMapper.selectMaps(
                new QueryWrapper<GitIssue>().select(
                                "avg((closed_at - create_at) / 3600 / 1000 / 24) as average"
                        ).isNotNull("closed_at")
                        .eq("repo_owner", owner)
                        .eq("repo_name", repo)
        ).get(0);
    }

    @GetMapping("/{owner}/{repo}/issueCountTimeBetween")
    public long issueCountBetween
            (@PathVariable("owner") String owner,
             @PathVariable("repo") String repo,
             @RequestParam("lower") String lower,
             @RequestParam("upper") String upper) {
        long l = Long.parseLong(lower), u = Long.parseLong(upper);
        return issueBaseMapper.selectCount(
                new QueryWrapper<GitIssue>()
                        .eq("repo_owner", owner)
                        .eq("repo_name", repo)
                        .between("create_at", l, u)
        );
    }

    @GetMapping("/{owner}/{repo}/gitHotWordTop")
    public List<Map<String, Object>> gitHotWordTop
            (@PathVariable("owner") String owner,
             @PathVariable("repo") String repo,
             @RequestParam("top") String top) {
        int t = Integer.parseInt(top);
        List<Map<String, Object>> re = gitHotWordMapper.selectMaps(
                new QueryWrapper<GitHotword>().select(
                                "word name, count(*) value"
                        ).eq("repo_owner", owner)
                        .eq("repo_name", repo)
                        .groupBy("word")
                        .orderByDesc("value")
        );

        return re.subList(0, t);
    }


}
