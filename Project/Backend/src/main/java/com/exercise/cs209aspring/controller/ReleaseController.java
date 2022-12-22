package com.exercise.cs209aspring.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.exercise.cs209aspring.entity.GitCommit;
import com.exercise.cs209aspring.entity.GitRelease;
import com.exercise.cs209aspring.mapper.GitCommitMapper;
import com.exercise.cs209aspring.mapper.GitReleaseMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RequestMapping("/")
@RestController
public class ReleaseController {

    @Resource
    private GitReleaseMapper gitReleaseMapper;

    @Resource
    private GitCommitMapper gitCommitMapper;

    @GetMapping("/{owner}/{repo}/releaseCount")
    public long releaseCount(@PathVariable("owner") String owner,
                           @PathVariable("repo") String repo) {
        return gitReleaseMapper.selectCount(
                new QueryWrapper<GitRelease>()
                        .eq("repo_owner", owner)
                        .eq("repo_name", repo));

    }

    @GetMapping("/{owner}/{repo}/dayTimeLabel")
    public long dayTimeLabelCount(@PathVariable("owner") String owner,
                                  @PathVariable("repo") String repo,
                                  @RequestParam("label") String label) {
        return gitCommitMapper.selectCount(
                new QueryWrapper<GitCommit>()
                        .eq("repo_owner", owner)
                        .eq("repo_name", repo)
                        .eq("daytime_label", label)
        );
    }

    @GetMapping("/{owner}/{repo}/weekTimeLabel")
    public long weekTimeLabelCount(@PathVariable("owner") String owner,
                                  @PathVariable("repo") String repo,
                                  @RequestParam("label") String label) {
        return gitCommitMapper.selectCount(
                new QueryWrapper<GitCommit>()
                        .eq("repo_owner", owner)
                        .eq("repo_name", repo)
                        .eq("weektime_label", label)
        );
    }

    @GetMapping("/{owner}/{repo}/releaseTimeStampTop")
    public List<Map<String, Object>> releaseTimeStampTop(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo,
            @RequestParam("top") String top) {
        int t = Integer.parseInt(top);
        return gitReleaseMapper.selectMaps(
                new QueryWrapper<GitRelease>()
                        .select("release_name, published_at")
                        .eq("repo_owner", owner)
                        .eq("repo_name", repo)
                        .orderByDesc("published_at")
                        ).subList(0, t);
    }

    @GetMapping("/{owner}/{repo}/commitBetween")
    public long commitBetween(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo,
            @RequestParam("lower") String lowerTimeStamp,
            @RequestParam("upper") String upperTimeStamp) {
        long l = Long.parseLong(lowerTimeStamp),
                u = Long.parseLong(upperTimeStamp);
        return gitCommitMapper.selectCount(
                new QueryWrapper<GitCommit>()
                        .eq("repo_owner", owner)
                        .eq("repo_name", repo)
                        .between("commit_date", l, u)
        );
    }

}
