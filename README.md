# storm-weather-station

Consider a weather station system deployed across several locations. Each location will have a numeric id and contain several stations under it. Each of these stations will also have a numeric id. We need a system that can accept large number of temperature readings from several hundreds of such locations. The system should also provide real time analytics and possibly database table scanning should be avoided.

This sample implementation attempts to address above issue. It retrieves temperatures measured by different stations in different locations and stores them in Cassandra. Along the way, it also calculates basic statistical monthly aggregates for each station, thereby demonstrating real time analytics. 

The project highlights concepts such as queueing (with [Kafka](http://kafka.apache.org/)), real time message processing and analytics (with [Storm](http://storm.apache.org/)) and NoSQL database ([Cassandra](http://cassandra.apache.org/)) design.

### Building and running
The project uses Maven. So things should be easy and straight-forward.

| Command | Description |
| ------ | -------- |
| mvn clean eclipse:eclipse | Command to generate project files for importing in Eclipse  |
| mvn clean package | Command to generate the output jar file  |

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

> Kafka   ===>  [Kafka Spout] -> (Deserialize Bolt) -> (Storage Bolt) -> (Statistics Calculation Bolt)


#### Data structure
Data structure in Cassandra involves two tables, one for storing the temperatures and other for aggregates.
A cql file for installing the keyspace and tables is provided in the *resources* folder.
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
This code is here solely for demo purpose and is not meant to be used directly in any kind of production. The code itself may not be optimized. 
