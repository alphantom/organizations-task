insert into document_type (code, name) values (1, 'Свидетельство о рождении');
insert into document_type (code, name) values (2, 'Паспорт гражданина РФ');
insert into document_type (code, name) values (3, 'Паспорт иностранного гражданина');
insert into document_type (code, name) values (10, 'Вид на жительство иностранного гражданина');
insert into document_type (code, name) values (11, 'Разрешение на временное проживание');
insert into document_type (code, name) values (12, 'Загранпаспорт гражданина РФ');
insert into document_type (code, name) values (13, 'Удостоверение личности военнослужащего РФ');
insert into document_type (code, name) values (15, 'Временное удостоверение личности гражданина РФ');
insert into document_type (code, name) values (18, 'Паспорт гражданина СССР');
insert into document_type (code, name) values (91, 'Другое');

insert into country values(643, 'Российская Федерация');
insert into country values(554, 'Новая Зеландия');
insert into country values(392, 'Япония');
insert into country values(203, 'Чешская Республика');
insert into country values(112, 'Республика Беларусь');
insert into country values(528, 'Королевство Нидерландов');
insert into country values(804, 'Украина');
insert into country values(126, 'Литовская Республика');
insert into country values(840, 'Соединённые Штаты Америки');
insert into country values(826, 'Соединённое Королевство Великобритании и Северной Ирландии');

-- TEST DATA --

insert into organization (name, fullname, inn, kpp, address, phone)
    values ('Cianide and Happiness', 'LLC "Cianide and Happiness"', '2294516859', '345511225', '410015, Russia, Saratov, Lermontova st, 12', '89279236287');
insert into organization (name, fullname, inn, kpp, address, phone)
    values ('Doofenshmirtz Inc.', 'LLC "Doofenshmirtz"', '3334516859', '349911325', '9297, USA Danville, Polly Parkway 21', '89256236287');
insert into organization (name, fullname, inn, kpp, address, phone)
    values ('Chip-n-Dale and Co', 'LLC "Chip-n-Dale and Co"', '4444656859', '345511425', '454552, UK, York, Pavlova st, 12', '89859236287');

insert into office (name, address, phone)
    values ('Head office', '410015, Russia, Saratov, Lermontova st, 12', '89279236287');
insert into office (name, address, phone)
    values ('Head office', '9297, USA Danville, Polly Parkway 21', '89272298277');
insert into office (name, address, phone)
    values ('Station #1', '9297, USA Danville, Polly Parkway 22', '89271820098');
insert into office (name, address, phone)
    values ('Station #2', '9297, USA Danville, Polly Parkway 23', '89271919182');
insert into office (name, address, phone, active)
    values ('Station #3', '9297, USA Danville, Polly Parkway 24', '89275890981', false);
insert into office (name, address, phone, active)
    values ('EMBLNC', '454552, UK, York, Pavlova st, 12', '89270918570', false);

insert into organization_office (org_id, off_id)
    values (1, 1);
insert into organization_office (org_id, off_id)
    values (2, 2);
insert into organization_office (org_id, off_id)
    values (2, 3);
insert into organization_office (org_id, off_id)
    values (2, 4);
insert into organization_office (org_id, off_id)
    values (3, 5);


insert into person (first_name, last_name, position, phone, country_id, identified, off_id)
    values ('Aragorn', 'Strider', 'Director', '89271837495', 643, true, 1);
insert into person (first_name, last_name, position, phone, country_id, identified, off_id)
    values ('Boromir', 'Denethor', 'Captain', '89272337495', 643, true, 1);
insert into person (first_name, last_name, middle_name, position, phone, country_id, identified, off_id)
    values ('Heinz', 'Doofenshmirtz', 'Junior', 'Director', '89271657495', 840, true, 2);
insert into person (first_name, last_name, middle_name, position, phone, country_id, identified, off_id)
    values ('Franz', 'Fidzherald', 'Courga' , 'Co-director', '89271845895', 840, true, 2);
insert into person (first_name, last_name, position, phone, country_id, identified, off_id)
    values ('Anakin', 'Skywalker', 'Master', '98771837495', 826, true, 3);
insert into person (first_name, last_name, position, phone, country_id, identified, off_id)
    values ('Luke', 'Skywalker', 'Warden', '65871837495', 826, true, 3);
insert into person (first_name, last_name, position, phone, country_id, identified, off_id)
    values ('Maayk', 'Alayer', 'Manager', '78225637495', 554, true, 4);
insert into person (first_name, last_name, position, phone, country_id, identified, off_id)
    values ('Chad', 'Robbinson', 'Director', '89271836585', 643, true, 5);
insert into person (first_name, last_name, position, phone, country_id, identified, off_id)
    values ('Mike', 'Gates', 'Developer', '33371837495', 643, true, 5);


insert into document (id, number, date, type_id) values (1, '4350987711', '1990-12-20', 2);
insert into document (id, number, date, type_id) values (2, '4350987722', '1991-12-21', 2);
insert into document (id, number, date, type_id) values (3, '4350987733', '1992-12-22', 3);
insert into document (id, number, date, type_id) values (4, '4350987744', '1993-12-23', 3);
insert into document (id, number, date, type_id) values (5, '4350987755', '1994-12-24', 3);
insert into document (id, number, date, type_id) values (6, '4350987766', '1995-12-25', 2);
insert into document (id, number, date, type_id) values (7, '4350987788', '1996-12-26', 2);