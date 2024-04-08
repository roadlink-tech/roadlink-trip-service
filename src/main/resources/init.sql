create table section
(
    arrival_latitude                 double null,
    arrival_longitude                double null,
    booked_seats                     int null,
    departure_latitude               double null,
    departure_longitude              double null,
    distance_in_meters               double null,
    initial_amount_of_seats          int null,
    arrival_estimated_arrival_time   datetime(6) null,
    departure_estimated_arrival_time datetime(6) null,
    trip_id                          varchar(36)  not null,
    arrival_city                     varchar(255) null,
    arrival_country                  varchar(255) null,
    arrival_full_address             varchar(255) null,
    arrival_house_number             varchar(255) null,
    arrival_street                   varchar(255) null,
    departure_city                   varchar(255) null,
    departure_country                varchar(255) null,
    departure_full_address           varchar(255) null,
    departure_house_number           varchar(255) null,
    departure_street                 varchar(255) null,
    driver_id                        varchar(255) null,
    id                               varchar(255) not null
        primary key,
    vehicle_id                       varchar(255) null
);

create index section_trip_id_idx
    on section (trip_id);

create table trip
(
    arrival_latitude                 double null,
    arrival_longitude                double null,
    available_seats                  int null,
    departure_latitude               double null,
    departure_longitude              double null,
    arrival_estimated_arrival_time   datetime(6) null,
    departure_estimated_arrival_time datetime(6) null,
    arrival_city                     varchar(255) null,
    arrival_country                  varchar(255) null,
    arrival_full_address             varchar(255) null,
    arrival_house_number             varchar(255) null,
    arrival_street                   varchar(255) null,
    departure_city                   varchar(255) null,
    departure_country                varchar(255) null,
    departure_full_address           varchar(255) null,
    departure_house_number           varchar(255) null,
    departure_street                 varchar(255) null,
    driver_id                        varchar(255) null,
    id                               varchar(255) not null
        primary key,
    status                           varchar(255) null,
    vehicle_id                       varchar(255) null
);

create table trip_jpaentity_meeting_points
(
    latitude               double       not null,
    longitude              double       not null,
    estimated_arrival_time datetime(6) null,
    city                   varchar(255) null,
    country                varchar(255) null,
    full_address           varchar(255) null,
    house_number           varchar(255) null,
    street                 varchar(255) null,
    trip_jpaentity_id      varchar(255) not null,
    constraint FK9nssgp3e6v7ahud484nvhbin8
        foreign key (trip_jpaentity_id) references trip (id)
);

create table trip_leg_sections
(
    arrival_latitude                 double null,
    arrival_longitude                double null,
    departure_latitude               double null,
    departure_longitude              double null,
    distance_in_meters               double null,
    arrival_estimated_arrival_time   datetime(6) null,
    departure_estimated_arrival_time datetime(6) null,
    arrival_city                     varchar(255) null,
    arrival_country                  varchar(255) null,
    arrival_full_address             varchar(255) null,
    arrival_house_number             varchar(255) null,
    arrival_street                   varchar(255) null,
    departure_city                   varchar(255) null,
    departure_country                varchar(255) null,
    departure_full_address           varchar(255) null,
    departure_house_number           varchar(255) null,
    departure_street                 varchar(255) null,
    id                               varchar(255) not null
        primary key
);

create table trip_plan
(
    id           char(36)    not null
        primary key,
    passenger_id varchar(36) not null
);

create table trip_leg
(
    driver_id              char(36) null,
    id                     char(36) not null
        primary key,
    trip_id                char(36) null,
    trip_plan_jpaentity_id char(36) null,
    vehicle_id             char(36) null,
    status                 varchar(255) null,
    constraint FKdwev0hn4by96xrxgc48x86s09
        foreign key (trip_plan_jpaentity_id) references trip_plan (id)
);

create table trip_leg_trip_leg_sections
(
    trip_leg_jpaentity_id char(36)     not null,
    sections_id           varchar(255) not null,
    constraint FKa8stdp7cpu34mohsd6b5t8g8r
        foreign key (sections_id) references trip_leg_sections (id),
    constraint FKch1ifvya8p4x7uh8uicylfx7r
        foreign key (trip_leg_jpaentity_id) references trip_leg (id)
);

create table trip_plan_solicitudes
(
    id char(36) not null
        primary key
);

create table trip_leg_solicitudes
(
    id                                 char(36) not null
        primary key,
    trip_plan_solicitudes_jpaentity_id char(36) null,
    authorizer_id                      varchar(255) null,
    passenger_id                       varchar(255) null,
    status                             varchar(255) null,
    constraint FKa8loenylwy3vopr51sfklo67v
        foreign key (trip_plan_solicitudes_jpaentity_id) references trip_plan_solicitudes (id)
);

create table trip_leg_solicitudes_section
(
    trip_leg_solicitude_jpaentity_id char(36)     not null,
    sections_id                      varchar(255) not null,
    constraint FK2wyyw4ywfssrwp14rppubotdd
        foreign key (trip_leg_solicitude_jpaentity_id) references trip_leg_solicitudes (id),
    constraint FK5vysfm96udyifvd3o6ry4bah1
        foreign key (sections_id) references section (id)
);
