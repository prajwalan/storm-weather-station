create keyspace if not exists weather_station_keyspace with replication = {'class' : 'SimpleStrategy', 'replication_factor' : 1 };

use weather_station_keyspace;
drop table if exists monthlystat;
drop table if exists temperature;
drop table if exists precipitation;

create table temperature (
    locationid int,
    stationid int,
    measuredtime timestamp,
    measurement float,
    PRIMARY KEY ((locationid, stationid), measuredtime)
)with clustering order by (measuredtime DESC);

create table precipitation (
    locationid int,
    stationid int,
    measuredtime timestamp,
    low float,
    high float,
    PRIMARY KEY ((locationid, stationid), measuredtime)
)with clustering order by (measuredtime DESC);

create table monthlystat(
    locationid int,
    entity int,
    year int,
    month int,
    count int,
	max float,
	min float,
	average float,
    PRIMARY KEY ((locationid, entity), year, month)
)with clustering order by (year desc, month desc);
