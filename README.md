# TemAH_cloud项目说明

TemAH_cloud是我在学习Spring各组件过程中产生的技术验证项目[TemAH_boot](https://github.com/lehn126/TemAH_boot)的clould版本，加入了Spring clould提供的服务注册与发现，负载均衡，Feign服务接口调用等功能。TemAH_cloud项目中包含的主要功能模块为：

* 模块TemAH_lam用于本地或远程主机的负载监控任务管理及相关告警的创建和分发。

* 模块TemAH_adapter用于从Kafka（已完成）或MQ（待添加）拉取已生成的告警并做告警格式标准化处理和分发。

* 模块TemAH_ahfm用于接收模块TemAH_lam或TemAH_adapter分发的告警，并进行数据库存储和告警信息管理。

* 模块TemAH_monitor是一个基于SpringBoot Admin Server的服务状态监控中心。

* 模块TemAH_eureka_server是基于Spring eureka server的服务注册中心。

* 模块TemAH_common，包含一些供各模块内使用的通用代码。

* 模块TemAH_common_redis，包含一些使用Redis可能会用到的通用代码。

* 模块TemAH_common_kafka，包含一些使用Kafka可能会用到的通用代码。

* 模块TemAH_common_alarm，包含告警处理会使用到的一些通用代码。

* 模块TemAH_demo_jwt，一个使用JWT token和自定义Aspect AOP注解进行token验证的demo。

项目中涉及告警处理的主要功能由模块TemAH_lam和TemAH_ahfm完成（需要搭配部署服务注册中心如TemAH_eureka_server或Nacos）。因此在极简部署模式下仅需要部署模块TemAH_lam和TemAH_ahfm即可完成设备监控任务管理和告警相关处理。同时可以在不改动项目源代码仅修改Yaml配置的情况下加添加Kafka/MQ和模块TemAH_adapter来进行高并发情景下的异步消息处理。

下面是各主要功能模块的简介和使用到的技术说明。

## TemAH_lam

用于动态创建和管理本地（Windows或Linux）及远程（Linux）主机的负载监控任务。完成主机CPU及内存负载状态的监控，并在负载超过设定的阙值时创建和分发监控告警。

主要配置:

```
alarm:
    consumer:
      address: "temah.ahfm" # 最终告警消费者AHFM的地址
      restful:
        enable: false
        uri: http://${alarm.consumer.address}/alarm/new
      kafka: # 发送告警到Kafka时才需要配
        enable: true
        topic: topic_alarm # 发送告警到Kafka使用的topic
      feign:
        enable: false # 直接使用feign发送告警到消费者AHFM时打开
```

在极简化部署时配置`alarm.consumer.kafka.enable`为`false`并设置`alarm.consumer.feign.enable`为`true`将产生的告警直接发送到模块TemAH_ahfm进行存储和管理。
在搭配模块TemAH_adapter和Kafka使用时配置`alarm.consumer.kafka.enable`为`true`并设置`alarm.consumer.feign.enable`为`false`将产生的告警分发至Kafka的指定topic供后续处理。

涉及技术:

| 名称                   | 版本       |
| -------------------- | -------- |
| Springboot           | 2.7.14   |
| Springcloud          | 2021.0.8 |
| Spring eureka client |          |
| Spring loadbalancer  |          |
| Spring openfeign     |          |
| Spring actuator      |          |
| jsch                 | 0.1.55   |
| mybatis              | 2.3.1    |
| spring-kafka         | 2.9.10   |

可以无缝搭配Vue开发的前端项目[TemAH_web_vue](https://github.com/lehn126/TemAH_web_vue2)进行监控任务管理。

## TemAH_adapter

用于对不同来源格式的告警进行标准化转换及在高负载高并发情景下使用Kafka/MQ进行削峰和异步告警消息处理和分发。

主要配置：

```
alarm:
  adapter: # 只有从Kafka收告警的adapter端点才需要配
    kafka:
      topic: topic_alarm # 接收告警使用的topic
      group: group_alarm # 接收告警使用的group
  consumer:
    address: "temah.ahfm" # AHFM服务的地址
    restful:
      enable: false
      uri: http://${alarm.consumer.address}/alarm/new
    feign: # 使用Feign调用服务接口分发转换后的告警
      enable: true
    kafka: # 发送adapter转换后的告警到Kafka时才需要配
      enable: false
      topic: topic_ahfm # 发送adapter转换后的告警到Kafka使用的topic
```

如果需要将adapter作标准化格式转换后的告警再次分发到Kafka指定的topic供其他资源进行消费，需要将配置`alarm.consumer.feign.enable`设置为`false`并将配置`alarm.consumer.kafka.enable`设置为`true`。

涉及技术:

| 名称                   | 版本       |
| -------------------- | -------- |
| Springboot           | 2.7.14   |
| Springcloud          | 2021.0.8 |
| Spring eureka client |          |
| Spring loadbalancer  |          |
| Spring openfeign     |          |
| Spring actuator      |          |
| spring-kafka         | 2.9.10   |

### TemAH_ahfm

用于接收从模块TemAH_lam（极简化部署）或TemAH_adapter（搭配Kafka使用）分发过来的设备告警并提供简单的告警管理服务（入库，查询，修改，状态变更）。

涉及技术:

| 名称                   | 版本       |
| -------------------- | -------- |
| Springboot           | 2.7.14   |
| Springcloud          | 2021.0.8 |
| Spring eureka client |          |
| Spring loadbalancer  |          |
| Spring openfeign     |          |
| Spring actuator      |          |
| mybatis              | 2.3.1    |

可以无缝搭配Vue开发的Web前端项目[TemAH_web_vue](https://github.com/lehn126/TemAH_web_vue2)进行告警信息管理。
