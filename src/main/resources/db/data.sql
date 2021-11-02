insert into user( account_non_expired,account_non_locked, credentials_non_expired, enabled, name, password, username)
        values ( true, true, true, true, '', '$2a$10$4IWuE2cwBTmg1CuZoKzGWOgTHBNINJl19BlTLj72mvvapycVuM/.m', 'moyu');

insert into role(role, username) values ('ROLE_ADMIN', 'moyu');
insert into role(role, username) values ('ROLE_SYSTEM', 'moyu');

insert into users_roles(user_id, role_id) values (1, 1);
insert into users_roles(user_id, role_id) values (1, 2);
