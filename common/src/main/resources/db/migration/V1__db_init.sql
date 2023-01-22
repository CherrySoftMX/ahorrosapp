create table daily_savings
(
    daily_saving_id bigint not null auto_increment,
    amount          decimal(19, 2),
    date            date,
    description     varchar(255),
    piggy_bank_id   bigint,
    primary key (daily_saving_id)
) engine = InnoDB;

create table piggy_banks
(
    piggy_bank_id  bigint      not null auto_increment,
    borrowedAmount decimal(19, 2),
    createdAt      datetime(6) not null,
    initialAmount  decimal(19, 2),
    name           varchar(255),
    endSavings     date,
    startSavings   date,
    owner_user_id  bigint,
    primary key (piggy_bank_id)
) engine = InnoDB;

create table piggy_banks_AUD
(
    piggy_bank_id  bigint  not null,
    REV            integer not null,
    REVTYPE        tinyint,
    borrowedAmount decimal(19, 2),
    createdAt      datetime(6),
    initialAmount  decimal(19, 2),
    name           varchar(255),
    endSavings     date,
    startSavings   date,
    owner_user_id  bigint,
    primary key (piggy_bank_id, REV)
) engine = InnoDB;

create table REVINFO
(
    REV      integer not null auto_increment,
    REVTSTMP bigint,
    primary key (REV)
) engine = InnoDB;

create table users
(
    user_id  bigint       not null auto_increment,
    password varchar(255) not null,
    username varchar(255) not null,
    primary key (user_id)
) engine = InnoDB;

create table users_AUD
(
    user_id  bigint  not null,
    REV      integer not null,
    REVTYPE  tinyint,
    username varchar(255),
    primary key (user_id, REV)
) engine = InnoDB;

alter table users
    add constraint UK_r43af9ap4edm43mmtq01oddj6 unique (username);

alter table daily_savings
    add constraint fk_piggy_bank_id
        foreign key (piggy_bank_id)
            references piggy_banks (piggy_bank_id);

alter table piggy_banks
    add constraint fk_user_id
        foreign key (owner_user_id)
            references users (user_id);

alter table piggy_banks_AUD
    add constraint FK61xnwv4aowvmbikgdpwy6sdaf
        foreign key (REV)
            references REVINFO (REV);

alter table users_AUD
    add constraint FKinrdywgyurfk2ojrfkard4ejn
        foreign key (REV)
            references REVINFO (REV);
