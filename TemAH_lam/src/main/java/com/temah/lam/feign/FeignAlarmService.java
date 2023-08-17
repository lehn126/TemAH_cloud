package com.temah.lam.feign;

import com.temah.common.web.RestResult;
import com.temah.lam.dto.AlarmDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("${lam.push.address}") //远程服务注册名称(spring.application.name)
public interface FeignAlarmService {


    // Rest接口路径
    @PostMapping(value = "/alarm/new", consumes = MediaType.APPLICATION_JSON_VALUE)
    RestResult newAlarm(@RequestBody AlarmDto alarm);
}
