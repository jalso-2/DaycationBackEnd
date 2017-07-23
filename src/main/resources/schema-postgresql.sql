DROP TABLE IF EXISTS users, trips, destinations, users_destinations, users_trips;
DROP SEQUENCE if EXISTS users_id_seq;

CREATE SEQUENCE users_id_seq;

CREATE TABLE  users (
  ID integer DEFAULT NEXTVAL('users_id_seq') PRIMARY KEY,
  NAME varchar(100) UNIQUE NOT NULL,
  PASSWORD varchar(100) NOT NULL,
  PREFERENCES jsonb
);

CREATE TABLE  trips (
  ID SERIAL PRIMARY KEY,
  NAME varchar(100) NOT NULL,
  DESTINATIONS jsonb
);

CREATE TABLE  destinations (
  ID SERIAL PRIMARY KEY,
  NAME varchar(100) NOT NULL,
  DESCRIPTION jsonb
);

CREATE TABLE  users_destinations (
  ID SERIAL PRIMARY KEY,
  USER_ID integer REFERENCES users,
  DESTINATION_ID integer REFERENCES destinations
);

CREATE TABLE  users_trips (
  ID SERIAL PRIMARY KEY,
  USER_ID integer REFERENCES users,
  TRIP_ID integer REFERENCES trips
);
