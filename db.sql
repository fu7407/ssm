DROP TABLE IF EXISTS `user_t`;  
  
CREATE TABLE `user_t` (  
  `id` int(11) NOT NULL AUTO_INCREMENT,  
  `user_name` varchar(40) NOT NULL,  
  `password` varchar(255) NOT NULL,  
  `age` int(4) NOT NULL,  
  PRIMARY KEY (`id`)  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;  
  
/*Data for the table `user_t` */  
  
insert  into `user_t`(`id`,`user_name`,`password`,`age`) values (1,'admin','123456',24)

/*红包表 */ 
create table T_RED_PACKET(
id int(12) not null auto_increment,
user_id int(12)  not null,
amount decimal (16 , 2) not null,
send_date timestamp not null,
total int(12) not null,
unit_amount decimal(12) not null ,
stock int(12) not null,
version int(12) default 0 not null,
note varchar (256) null,
primary key clustered (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*用户抢红包表 */ 
create table T_USER_RED_PACKET(
id int(12) not null auto_increment,
red_packet_id int(12)  not null,
user_id int(12) not null,
amount decimal(16,2) not null,
grab_time timestamp not null,
note varchar (256) null,
primary key clustered (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into T_RED_PACKET(user_id,amount,send_date, total, unit_amount,stock, note) values(1, 200000.00, now(), 20000, 10.00, 20000, '20万元金额,2万个小红包,每个10元');

select a.`amount`,a.`stock`,a.id from t_red_packet a where id=1
union all
select sum(b.amount),count(*),Max(user_id) from t_user_red_packet b where b.red_packet_id=1;


select min(b.grab_time),count(*),Max(b.grab_time) from t_user_red_packet b where b.red_packet_id=1;
