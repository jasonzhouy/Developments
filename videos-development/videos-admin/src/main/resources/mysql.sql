
#定时任务

set time_zone = '+8:00';
set GLOBAL event_scheduler = 1;

use com_zymonster_videos;

# 如果原来存在该名字的任务计划则先删除
drop event if exists delete_to_sdmp;

# 设置分隔符为 '$$' ，mysql默认的语句分隔符为 ';' ，这样在后续的 create 到 end 这段代码都会看成是一条语句来执行
DELIMITER $$
# 创建计划任务，设置第一次执行时间为'2014-07-30 10:00:00'，并且每天执行一次
create event delete_to_sdmp
on schedule every 1 hour starts timestamp '2019-05-10 14:59:59'
#on schedule every 1 SECOND
do

# 开始该计划任务要做的事
begin


-- do something 编写你的计划任务要做的事
	create table temp as(select id from ban_list where end_time < NOW());
	delete from ban_list where id in (select id from temp);
	drop table temp;
-- 结束计划任务
end $$

# 将语句分割符设置回 ';'
DELIMITER ;

关闭事件：
ALTER EVENT upload_to_sdmp DISABLE;

开启事件：
ALTER EVENT upload_to_sdmp ENABLE;

select * from mysql.event


注意：真实的开发环境中，会遇到mysql服务重启或者断电的情况，此时则会出现事件调度器被关闭的情况，所有事件都不在起作用，要想解决这个办法，则需要在mysql.ini文件中加入event_scheduler = ON; 的语句
