package com.temah.lam.service;

import com.temah.common.alarm.dispatcher.AlarmDispatcher;
import com.temah.common.alarm.dto.AlarmDto;
import com.temah.lam.feign.FeignAlarmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class AlarmService {

    private static final Logger logger = LoggerFactory.getLogger(AlarmService.class);

    private final AlarmDispatcher alarmDispatcher;

    @Value("${alarm.consumer.feign.enable:false}")
    private boolean feignPushEnable = false;

    private FeignAlarmService feignAlarmService;

    public AlarmService(AlarmDispatcher alarmDispatcher, FeignAlarmService feignAlarmService) {
        this.alarmDispatcher = alarmDispatcher;
        this.feignAlarmService = feignAlarmService;
    }

    private void pushAlarm(AlarmDto alarm) {
        try {
            // 处理端接收的是Alarm List
            List<AlarmDto> alarms = Collections.singletonList(alarm);
            if (feignPushEnable) {
                logger.debug("使用Feign API推送告警. {}", alarms);
                feignAlarmService.newAlarm(alarms);
            } else {
                alarmDispatcher.pushAlarms(alarms);
            }
        } catch (Exception e) {
            logger.error("推送告警出错. {}", alarm, e);
        }
    }

    public void createCpuOverLimitAlarm(String host, Date date, double ratio, double limit) {
        AlarmDto alarmDto = AlarmDto.builder()
                .setEventTime(date)
                .setManagedObject(String.format("OSI_SYSTEM '%s'", host))
                .setAlarmType("EquipmentAlarm")
                .setAdditionalText(String.format("CPU Over limit;host=%s;ratio=%.2f;limit=%.2f;", host, ratio, limit))
                .build();

        logger.info("创建主机CPU超限告警. {}", alarmDto);
        pushAlarm(alarmDto);
    }

    public void createMemoryOverLimitAlarm(String host, Date date, double ratio, double limit) {
        AlarmDto alarmDto = AlarmDto.builder()
                .setEventTime(date)
                .setManagedObject(String.format("OSI_SYSTEM '%s'", host))
                .setAlarmType("EquipmentAlarm")
                .setAdditionalText(String.format("Memory Over limit;host=%s;ratio=%.2f;limit=%.2f;", host, ratio, limit))
                .build();

        logger.info("创建主机内存占用超限告警. {}", alarmDto);
        pushAlarm(alarmDto);
    }
}
