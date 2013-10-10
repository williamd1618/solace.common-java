create table Person
(
	id bigint identity primary key,
	age int not null,
	first_name varchar(255) not null,
	last_name varchar(255) not null,
	friend_id bigint,
	create_date timestamp not null,
	last_update_date timestamp not null,
	version bigint
);


alter table person add foreign key (friend_id) references person(id);
