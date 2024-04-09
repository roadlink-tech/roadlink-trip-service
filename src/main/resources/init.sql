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


-- INSERTS
INSERT INTO tripservice_db.trip (arrival_latitude, arrival_longitude, available_seats, departure_latitude,
                                 departure_longitude, arrival_estimated_arrival_time, departure_estimated_arrival_time,
                                 arrival_city, arrival_country, arrival_full_address, arrival_house_number,
                                 arrival_street, departure_city, departure_country, departure_full_address,
                                 departure_house_number, departure_street, driver_id, id, status, vehicle_id)
VALUES (-31.42056375510204, -64.19165538979591, 4, -34.54025770408163, -58.47450726734694,
        ADDDATE(CURDATE(), INTERVAL 1 DAY) + INTERVAL '00:00:00' HOUR_SECOND,
        ADDDATE(CURDATE(), INTERVAL 1 DAY) + INTERVAL '17:00:00' HOUR_SECOND, 'Cordoba', 'Argentina',
        'Montevideo 377, Cordoba', '377', 'Montevideo', 'Buenos Aires', 'Argentina',
        'Avenida Cabildo 4853, Buenos Aires', '4853', 'Avenida Cabildo', '123e4567-e89b-12d3-a456-426614174004',
        '1a4eee4f-3c6a-478d-b55a-cf4911e5b7b2', 'NOT_STARTED', 'b85df607-16cf-4da2-8f2e-51baa90a1748');

INSERT INTO tripservice_db.trip_jpaentity_meeting_points (latitude, longitude, estimated_arrival_time, city, country,
                                                          full_address, house_number, street, trip_jpaentity_id)
VALUES (-34.4569996, -58.9131929, ADDDATE(CURDATE(), INTERVAL 1 DAY) + INTERVAL '19:00:00' HOUR_SECOND, 'Pilar',
        'Argentina', 'Pedro Lagrave 600, Pilar', '600', 'Pedro Lagrave', '1a4eee4f-3c6a-478d-b55a-cf4911e5b7b2');

INSERT INTO tripservice_db.section (arrival_latitude, arrival_longitude, booked_seats, departure_latitude,
                                    departure_longitude, distance_in_meters, initial_amount_of_seats,
                                    arrival_estimated_arrival_time, departure_estimated_arrival_time, trip_id,
                                    arrival_city, arrival_country, arrival_full_address, arrival_house_number,
                                    arrival_street, departure_city, departure_country, departure_full_address,
                                    departure_house_number, departure_street, driver_id, id, vehicle_id)
VALUES (-34.4569996, -58.9131929, 0, -34.54025770408163, -58.47450726734694, 0, 4,
        ADDDATE(CURDATE(), INTERVAL 1 DAY) + INTERVAL '19:00:00' HOUR_SECOND,
        ADDDATE(CURDATE(), INTERVAL 1 DAY) + INTERVAL '17:00:00' HOUR_SECOND, '1a4eee4f-3c6a-478d-b55a-cf4911e5b7b2',
        'Pilar', 'Argentina', 'Pedro Lagrave 600, Pilar', '600', 'Pedro Lagrave', 'Buenos Aires', 'Argentina',
        'Avenida Cabildo 4853, Buenos Aires', '4853', 'Avenida Cabildo', '123e4567-e89b-12d3-a456-426614174004',
        '475b78e8-7f52-45cc-bb88-d0d417d04394', 'b85df607-16cf-4da2-8f2e-51baa90a1748');

INSERT INTO tripservice_db.section (arrival_latitude, arrival_longitude, booked_seats, departure_latitude,
                                    departure_longitude, distance_in_meters, initial_amount_of_seats,
                                    arrival_estimated_arrival_time, departure_estimated_arrival_time, trip_id,
                                    arrival_city, arrival_country, arrival_full_address, arrival_house_number,
                                    arrival_street, departure_city, departure_country, departure_full_address,
                                    departure_house_number, departure_street, driver_id, id, vehicle_id)
VALUES (-31.42056375510204, -64.19165538979591, 0, -34.4569996, -58.9131929, 0, 4,
        ADDDATE(CURDATE(), INTERVAL 1 DAY) + INTERVAL '00:00:00' HOUR_SECOND,
        CURDATE() + INTERVAL '19:00:00' HOUR_SECOND,
        '1a4eee4f-3c6a-478d-b55a-cf4911e5b7b2', 'Cordoba', 'Argentina',
        'Montevideo 377, Cordoba', '377', 'Montevideo', 'Pilar', 'Argentina',
        'Pedro Lagrave 600, Pilar', '600', 'Pedro Lagrave',
        '123e4567-e89b-12d3-a456-426614174004', 'b48fc155-f397-4a3d-b8c3-322392e907e6',
        'b85df607-16cf-4da2-8f2e-51baa90a1748');

--
INSERT INTO tripservice_db.trip (arrival_latitude, arrival_longitude, available_seats, departure_latitude,
                                 departure_longitude, arrival_estimated_arrival_time, departure_estimated_arrival_time,
                                 arrival_city, arrival_country, arrival_full_address, arrival_house_number,
                                 arrival_street, departure_city, departure_country, departure_full_address,
                                 departure_house_number, departure_street, driver_id, id, status, vehicle_id)
VALUES (-41.1166129, -71.4049374, 4, -34.62819801818182, -58.39189034949495,
        DATE_ADD(CURRENT_DATE, INTERVAL 20 DAY) + INTERVAL '15:00' HOUR_MINUTE,
        DATE_ADD(CURRENT_DATE, INTERVAL 19 DAY) + INTERVAL '15:00' HOUR_MINUTE,
        'San Carlos de Bariloche', 'Argentina',
        'Avenida Exequiel Bustillo 1500, San Carlos de Bariloche', '1500', 'Avenida Exequiel Bustillo', 'Buenos Aires',
        'Argentina', 'Combate de los Pozos 1682, Buenos Aires', '1682', 'Combate de los Pozos',
        '123e4567-e89b-12d3-a456-426614174004', '705b6389-b1be-4215-b7eb-a3093d96eb3d', 'NOT_STARTED',
        'b85df607-16cf-4da2-8f2e-51baa90a1748');

INSERT INTO tripservice_db.trip_jpaentity_meeting_points (latitude, longitude, estimated_arrival_time, city, country,
                                                          full_address, house_number, street, trip_jpaentity_id)
VALUES (-38.04862065306122, -57.56475636734694, DATE_ADD(CURRENT_DATE, INTERVAL 20 DAY) + INTERVAL '21:00' HOUR_MINUTE, 'Mar del Plata', 'Argentina',
        'Eduardo Carasa 3998, Mar del Plata', '3998', 'Eduardo Carasa', '705b6389-b1be-4215-b7eb-a3093d96eb3d');
INSERT INTO tripservice_db.trip_jpaentity_meeting_points (latitude, longitude, estimated_arrival_time, city, country,
                                                          full_address, house_number, street, trip_jpaentity_id)
VALUES (-37.378776110204086, -64.60683112857143,  DATE_ADD(CURRENT_DATE, INTERVAL 20 DAY) + INTERVAL '04:02' HOUR_MINUTE, 'Municipio de General Acha', 'Argentina',
        'Padre Buodo 1035, Municipio de General Acha', '1035', 'Padre Buodo', '705b6389-b1be-4215-b7eb-a3093d96eb3d');

INSERT INTO tripservice_db.section (arrival_latitude, arrival_longitude, booked_seats, departure_latitude,
                                    departure_longitude, distance_in_meters, initial_amount_of_seats,
                                    arrival_estimated_arrival_time, departure_estimated_arrival_time, trip_id,
                                    arrival_city, arrival_country, arrival_full_address, arrival_house_number,
                                    arrival_street, departure_city, departure_country, departure_full_address,
                                    departure_house_number, departure_street, driver_id, id, vehicle_id)
VALUES (-38.04862065306122, -57.56475636734694, 0, -34.62819801818182, -58.39189034949495, 0, 4,
        DATE_ADD(CURRENT_DATE, INTERVAL 19 DAY) + INTERVAL '21:00' HOUR_MINUTE,
        DATE_ADD(CURRENT_DATE, INTERVAL 19 DAY) + INTERVAL '15:00' HOUR_MINUTE,
        '705b6389-b1be-4215-b7eb-a3093d96eb3d',
        'Mar del Plata', 'Argentina', 'Eduardo Carasa 3998, Mar del Plata', '3998', 'Eduardo Carasa', 'Buenos Aires',
        'Argentina', 'Combate de los Pozos 1682, Buenos Aires', '1682', 'Combate de los Pozos',
        '123e4567-e89b-12d3-a456-426614174004', '498ea12e-5ad5-4b0c-a587-6acfeb0ddbd3',
        'b85df607-16cf-4da2-8f2e-51baa90a1748');
--         '2024-04-22 21:00:00.000000', '2024-04-22 15:00:00.000000',
INSERT INTO tripservice_db.section (arrival_latitude, arrival_longitude, booked_seats, departure_latitude,
                                    departure_longitude, distance_in_meters, initial_amount_of_seats,
                                    arrival_estimated_arrival_time, departure_estimated_arrival_time, trip_id,
                                    arrival_city, arrival_country, arrival_full_address, arrival_house_number,
                                    arrival_street, departure_city, departure_country, departure_full_address,
                                    departure_house_number, departure_street, driver_id, id, vehicle_id)
VALUES (-37.378776110204086, -64.60683112857143, 0, -38.04862065306122, -57.56475636734694, 0, 4,
        DATE_ADD(CURRENT_DATE, INTERVAL 20 DAY) + INTERVAL '04:02' HOUR_MINUTE,
        DATE_ADD(CURRENT_DATE, INTERVAL 19 DAY) + INTERVAL '21:00' HOUR_MINUTE,
        '705b6389-b1be-4215-b7eb-a3093d96eb3d',
        'Municipio de General Acha', 'Argentina', 'Padre Buodo 1035, Municipio de General Acha', '1035', 'Padre Buodo',
        'Mar del Plata', 'Argentina', 'Eduardo Carasa 3998, Mar del Plata', '3998', 'Eduardo Carasa',
        '123e4567-e89b-12d3-a456-426614174004', 'b9d9bfa1-929d-4c28-811f-e3aaa0e371a8',
        'b85df607-16cf-4da2-8f2e-51baa90a1748');
--        '2024-04-23 04:02:00.000000','2024-04-22 21:00:00.000000',
INSERT INTO tripservice_db.section (arrival_latitude, arrival_longitude, booked_seats, departure_latitude,
                                    departure_longitude, distance_in_meters, initial_amount_of_seats,
                                    arrival_estimated_arrival_time, departure_estimated_arrival_time, trip_id,
                                    arrival_city, arrival_country, arrival_full_address, arrival_house_number,
                                    arrival_street, departure_city, departure_country, departure_full_address,
                                    departure_house_number, departure_street, driver_id, id, vehicle_id)
VALUES (-41.1166129, -71.4049374, 0, -37.378776110204086, -64.60683112857143, 0, 4,
        DATE_ADD(CURRENT_DATE, INTERVAL 20 DAY) + INTERVAL '15:00' HOUR_MINUTE,
        DATE_ADD(CURRENT_DATE, INTERVAL 20 DAY) + INTERVAL '04:02' HOUR_MINUTE,
        '705b6389-b1be-4215-b7eb-a3093d96eb3d', 'San Carlos de Bariloche', 'Argentina',
        'Avenida Exequiel Bustillo 1500, San Carlos de Bariloche', '1500', 'Avenida Exequiel Bustillo',
        'Municipio de General Acha', 'Argentina', 'Padre Buodo 1035, Municipio de General Acha', '1035', 'Padre Buodo',
        '123e4567-e89b-12d3-a456-426614174004', 'ecd15ada-6123-4b0d-bd8e-1ee7523b76c7',
        'b85df607-16cf-4da2-8f2e-51baa90a1748');
--        '2024-04-23 15:00:00.000000', '2024-04-23 04:02:00.000000',