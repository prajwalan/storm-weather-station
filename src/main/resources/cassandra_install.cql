drop keyspace if exists weather_station_keyspace;
create keyspace if not exists weather_station_keyspace with replication = {'class' : 'SimpleStrategy', 'replication_factor' : 1 };

use weather_station_keyspace;
drop table if exists monthlystat;
drop table if exists temperature;
drop table if exists precipitation;
drop type if exists milestone;

CREATE TYPE weather_station_keyspace.milestone (
    stationid int,
    measuredtime timestamp,
    value float,
);

create table weather_station_keyspace.temperature (
    locationid int,
    stationid int,
    measuredtime timestamp,
    measurement float,
    PRIMARY KEY ((locationid, stationid), measuredtime)
)with clustering order by (measuredtime DESC);

create table weather_station_keyspace.precipitation (
    locationid int,
    stationid int,
    measuredtime timestamp,
    low float,
    high float,
    PRIMARY KEY ((locationid, stationid), measuredtime)
)with clustering order by (measuredtime DESC);

create table weather_station_keyspace.monthlystat(
    locationid int,
    entity int,
    year int,
    month int,
    count int,
	max list<frozen<weather_station_keyspace.milestone>>,
	min list<frozen<weather_station_keyspace.milestone>>,
	average float,
    PRIMARY KEY ((locationid, entity), year, month)
)with clustering order by (year desc, month desc);
