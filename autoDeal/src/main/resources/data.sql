 --INSERT INTO customer (first_name, last_name, password, email, phone) VALUES ('Jan','Kowalski', '$2a$10$qf69GDBrAJqbzyPkNCtCqu6sviASNVzd3ja60BTi11NlKagCkn5z6','jan.kowalski@gmail.com', '501-123-456');
--hasło to 'abc'

 --INSERT INTO customer (first_name, last_name, password, email, phone) VALUES ('Jan','Kot', '$2a$10$Sj393/G2PgIJeJpStvxizOpOYBZJ8pDZCJu0OauiDW4sZuWrZa.9u','jan.kot@gmail.com', '501-123-002');
--hasło to 'asd'

--INSERT INTO user_role (name) VALUES ('ROLE_ADMIN');
--INSERT INTO user_role (name) VALUES ('ROLE_USER');

INSERT INTO user_x_roles VALUES (1,1);
INSERT INTO user_x_roles VALUES (2,2);

INSERT INTO product (name, price, car_make, mileage, origin, type, code, color, warranty,production_year) VALUES ('Dacia prawie niebita', '10000','Dacia','200000', 'Maroko','VAN', '1234567', 'biały', '1','2019');

INSERT INTO product (name, price, car_make, mileage, origin, type, code, color, warranty,production_year) VALUES ('Passat jak nowy', '5000','Volkswagen','300000', 'Niemcy','SEDAN', '7654321', 'czarny', '0','2016');
INSERT INTO product (name, price, car_make, mileage, origin, type, code, color, warranty,production_year) VALUES ('Passat inny', '5','Volkswagen','300000', 'Niemcy','SEDAN', '7654321', 'czarny', '0','2016');