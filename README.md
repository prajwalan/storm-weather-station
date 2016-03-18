# storm-weather-station

### Introduction
Consider a weather station system deployed across several locations. Each location will have a numeric id and contain several stations under it. Each of these stations will also have a numeric id.

This sample implementation retrieves temperatures measured by different stations in different locations and stores them in Cassandra. Along the way, it also calculates basic statistical monthly aggregates for each station.

### System setup
The system consists of following components. The IP address for hosts and ports are in EvnConstant class. Ideally, you would read it from a config file in a real application. But here it is just a demo.

| Component  | Purpose  | Configuration Location |
| ---------- | -------- | ---------- |
| Kafka      | Message queue  | EnvConstant.KAFKA_BROKER_LIST, +++ |
| Storm      | Real time message processing  | TopologyConstants |
| Cassandra  | Message storage  | EnvConstant.CASSANDRA_HOST, +++ |


### Process
1. All the stations published measured temperatures to a Kafka queue as a JSON message.
2. The Kafka spout in storm topology reads from the queue and passes to storage bolt for storing to Cassandra.
3. The message is further passed to statistics calculation bolt that calculates the monthly aggregates for each location.

#### Kafka Message
All the messages are published to and read from the topic "temperatureseries". It is declared in TopologyConstants class.

Format of JSON message published by stations:
> {
>   "Measurement": "17.43",
>   "LocationId": 50,
>   "StationId": 502,
>   "Timestamp": 1458297000939
> }

#### Storm topology
Storm message processing system looks like below:

> Kafka   ===>  Kafka Spout -> Deserialize Bolt -> Storage Bolt -> Statistics Calculation Bolt


#### Data structure
Data structure in Cassandra involves two tables, one for storing the temperatures and other for aggregates.
The structure looks like below:
```sh
create table weather_station_keyspace.temperature (
    locationid int,
    stationid int,
    measuredtime timestamp,
    measurement text,
    PRIMARY KEY ((locationid, stationid), measuredtime)
)with clustering order by (measuredtime DESC);
```

```sh
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
```

#### Tuple Generator
The TupleGenerator class in *com.weather.publisher* package generates dummy temperature readings and publishes to the Kafka topic for testing purpose.

#### Disclaimer
This code is here solely for demo purpose and is not meant to be used directly in any kind of production. It demonstrates concepts such as queueing, real time message processing and analytics and NoSQL database design. 
