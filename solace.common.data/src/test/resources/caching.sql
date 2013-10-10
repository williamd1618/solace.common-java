create table Parent 
( 
	id bigint identity primary key, 
	effective_date timestamp not null,
	create_date timestamp not null,
	last_update_date timestamp not null,
	version bigint default 0,
	person_id bigint not null
);


create table Person
(
	id bigint identity primary key,
	age int not null,
	birth_date timestamp not null,
	first_name varchar(255) not null,
	last_name varchar(255) not null,
	friend_id bigint,
	create_date timestamp not null,
	last_update_date timestamp not null,
	version bigint default 0
);

create table ParentChild
(
	parent_id bigint not null,
	child_id bigint not null
);

ALTER TABLE ParentChild ADD PRIMARY KEY (parent_id, child_id);

alter table parent add foreign key (person_id) references person(id);
alter table parentchild add foreign key (parent_id) references parent(id);
alter table parentchild add foreign key (child_id) references person(id);
alter table person add foreign key (friend_id) references person(id);
