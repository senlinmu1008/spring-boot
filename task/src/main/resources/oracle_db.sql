DROP TABLE spring_schedule_cron;
CREATE TABLE spring_schedule_cron (
    id NUMBER NOT NULL,
    bean_name VARCHAR2(128) NOT NULL,--COMMENT '定时任务beanName'
    cron_expression VARCHAR2(16) NOT NULL,--COMMENT 'cron表达式'
    task_desc VARCHAR2(128) NOT NULL, --COMMENT '任务描述',
    enable INTEGER DEFAULT 1, --COMMENT '状态 0-禁用 1-启用'
    app_id VARCHAR2(16), --COMMENT '应用id'
    PRIMARY KEY (id)
);
COMMENT ON TABLE spring_schedule_cron IS '定时任务表';
CREATE UNIQUE INDEX spring_schedule_cron_index ON spring_schedule_cron(bean_name, app_id);

-- 主键序列
DROP sequence seq_spring_schedule_cron;
CREATE sequence seq_spring_schedule_cron
INCREMENT BY 1
START WITH 1
ORDER
CACHE 20;

insert into spring_schedule_cron values (seq_spring_schedule_cron.NEXTVAL, 'scheduleTask1', '*/3 * * * * ?', '定时任务描述1', 1, 'task');
insert into spring_schedule_cron values (seq_spring_schedule_cron.NEXTVAL, 'scheduleTask2', '*/5 * * * * ?', '定时任务描述2', 1, 'task');
insert into spring_schedule_cron values (seq_spring_schedule_cron.NEXTVAL, 'scheduleTask3', '*/7 * * * * ?', '定时任务描述3', 1, 'task');
