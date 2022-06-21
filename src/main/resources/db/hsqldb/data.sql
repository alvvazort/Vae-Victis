-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled,wins) VALUES ('admin1','4dm1n',TRUE,0);
INSERT INTO authorities(id,username,authority) VALUES (1,'admin1','admin');
-- One owner user, named owner1 with passwor 0wn3r
INSERT INTO users(username,password,enabled,wins) VALUES ('owner1','0wn3r',TRUE,0);
INSERT INTO authorities(id,username,authority) VALUES (2,'owner1','user');
-- One vet user, named vet1 with passwor v3t
INSERT INTO users(username,password,enabled,wins) VALUES ('vet1','v3t',TRUE,0);
INSERT INTO authorities(id,username,authority) VALUES (3,'vet1','user');
INSERT INTO users(username,password,enabled,wins) VALUES ('dandianog','asdasdasd123',TRUE,0);
INSERT INTO authorities(id,username,authority) VALUES (4,'dandianog','admin');
INSERT INTO users(username,password,enabled,wins) VALUES ('luirodvid','luirodvid',TRUE,0);
INSERT INTO authorities(id,username,authority) VALUES (5,'luirodvid','admin');
INSERT INTO users(username,password,enabled,wins) VALUES ('alvvazort','alvvazort',TRUE,0);
INSERT INTO authorities(id,username,authority) VALUES (6,'alvvazort','admin');
INSERT INTO users(username,password,enabled,wins) VALUES ('alvmirpoz','alvmirpoz',TRUE,0);
INSERT INTO authorities(id,username,authority) VALUES (7,'alvmirpoz','admin');
INSERT INTO users(username,password,enabled,wins) VALUES ('gonmarmar5','gonmarmar5',TRUE,10);
INSERT INTO authorities(id,username,authority) VALUES (8,'gonmarmar5','admin');

-- Jugadores de ejemplo
INSERT INTO users(username,password,enabled,wins) VALUES ('player1','asdasdasd123',TRUE,0);
INSERT INTO authorities(id,username,authority) VALUES (9,'player1','user');
INSERT INTO users(username,password,enabled,wins) VALUES ('player2','asdasdasd123',TRUE,0);
INSERT INTO authorities(id,username,authority) VALUES (10,'player2','user');
INSERT INTO users(username,password,enabled,wins) VALUES ('player3','asdasdasd123',TRUE,0);
INSERT INTO authorities(id,username,authority) VALUES (11,'player3','user');
INSERT INTO users(username,password,enabled,wins) VALUES ('player4','asdasdasd123',TRUE,0);
INSERT INTO authorities(id,username,authority) VALUES (12,'player4','user');
INSERT INTO users(username,password,enabled,wins) VALUES ('player5','asdasdasd123',TRUE,0);
INSERT INTO authorities(id,username,authority) VALUES (13,'player5','user');
INSERT INTO users(username,password,enabled,wins) VALUES ('player6','asdasdasd123',TRUE,0);
INSERT INTO authorities(id,username,authority) VALUES (14,'player6','user');

--Comprobando que se creen correctamente
INSERT INTO FRIENDS VALUES (TRUE,'admin1','luirodvid');
INSERT INTO FRIENDS VALUES (TRUE,'admin1','owner1');
INSERT INTO FRIENDS VALUES (TRUE,'owner1','luirodvid');

INSERT INTO FRIENDS VALUES (TRUE,'player1','player2');
INSERT INTO FRIENDS VALUES (TRUE,'player1','player3');
INSERT INTO FRIENDS VALUES (TRUE,'player1','player4');
INSERT INTO FRIENDS VALUES (TRUE,'player1','player5');
INSERT INTO FRIENDS VALUES (TRUE,'player1','player6');
--Hay que añadir restricciones en el controlador para este caso:

INSERT INTO achievements(name,description,achievement_condition) VALUES('Ave Cesar','Gana al menos 1 partida',1);
INSERT INTO achievements(name,description,achievement_condition) VALUES('Una monedita?','Pierde 1 partida por falta de monedas',1);
INSERT INTO achievements(name,description,achievement_condition) VALUES('Querido amigo...','Agrega al menos a 1 amigo',1);
INSERT INTO achievements(name,description,achievement_condition) VALUES('Esto es esparta','Derrota al menos 1 vez a tus enemigos en el campo de batalla',1);
INSERT INTO achievements(name,description,achievement_condition) VALUES('Se te da bien','Gana al menos 1 partidas',10);
