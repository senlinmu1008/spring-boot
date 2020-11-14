DROP TABLE IF EXISTS spring_schedule_cron;
CREATE TABLE spring_schedule_cron (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    bean_name varchar(128) NOT NULL COMMENT '定时任务beanName',
    cron_expression varchar(16) NOT NULL COMMENT 'cron表达式',
    task_desc varchar(64) NOT NULL COMMENT '任务描述',
    status tinyint DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    app_id varchar(16) COMMENT '应用id',
    PRIMARY KEY (id)
)COMMENT = '定时任务表';
ALTER TABLE spring_schedule_cron ADD UNIQUE (bean_name);

insert into spring_schedule_cron values (1, 'scheduleTask1', '*/5 * * * * ?', '定时任务描述1', 1, 'test');
insert into spring_schedule_cron values (2, 'scheduleTask2', '*/6 * * * * ?', '定时任务描述2', 1, 'test');
insert into spring_schedule_cron values (3, 'scheduleTask3', '*/7 * * * * ?', '定时任务描述3', 1, 'test');