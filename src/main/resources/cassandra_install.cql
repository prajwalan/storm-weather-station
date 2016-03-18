drop keyspace if exists weather_station_keyspace;
create keyspace if not exists weather_station_keyspace with replication = {'class' : 'SimpleStrategy', 'replication_factor' : 1 };

use weather_station_keyspace;
drop table if exists weather_station_keyspace.monthlystat;
drop table if exists weather_station_keyspace.temperature;

create table weather_station_keyspace.temperature (
    locationid int,
    stationid int,
    measuredtime timestamp,
    measurement text,
    PRIMARY KEY ((locationid, stationid), measuredtime)
)with clustering order by (measuredtime DESC);

create table weather_station_keyspace.monthlystat(
    locationid int,
    entity int,
    year int,
    month int,
    count int,
    average text,
	max text,
	maxtime timestamp,
	maxstationid int,
	min text,
	mintime timestamp,
	minstationid int,	
    PRIMARY KEY ((locationid, entity), year, month)
)with clustering order by (year desc, month desc);
