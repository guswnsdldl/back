create table tbl_reply (
                           id bigint unsigned auto_increment primary key,
                           reply_content varchar(255) not null,
                           member_id bigint unsigned not null,
                           work_id bigint unsigned not null,
                           created_date datetime default current_timestamp,
                           star int not null,
                           constraint fk_reply_member foreign key (member_id)
                               references tbl_member(id),
                           constraint fk_reply_work foreign key (work_id)
                               references tbl_work(id)
);