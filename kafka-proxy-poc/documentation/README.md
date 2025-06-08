# Kafka Proxy POC Infrastructure

This document provides detailed information about the infrastructure setup for the Kafka Proxy POC project.

## Components Overview

The project uses Docker Compose to set up the following services:

1. **Kafka and ZooKeeper**
2. **Topic Initialization Service**
3. **VictoriaMetrics** (time-series database)
4. **Grafana** (visualization)
5. **OpenTelemetry Collector** (metrics collection)

## Kafka Infrastructure

### ZooKeeper

ZooKeeper is used for managing the Kafka cluster:

- **Image**: confluentinc/cp-zookeeper:7.4.0
- **Port**: 2181
- **Configuration**:
  - Client port: 2181
  - Tick time: 2000ms

### Kafka Broker

Single-node Kafka broker:

- **Image**: confluentinc/cp-kafka:7.4.0
- **Port**: 9092 (externally accessible)
- **Internal Network Address**: kafka:29092
- **Configuration**:
  - Broker ID: 1
  - Zookeeper connection: zookeeper:2181
  - Replication factor: 1
- **Data Persistence**: Uses a dedicated volume (`kafka-data`)

### Topic Initialization

An initializer container that automatically creates required topics:

- **Image**: confluentinc/cp-kafka:7.4.0
- **Topics Created**:
  - `incoming` (1 partition, replication factor 1)
  - `outgoing` (1 partition, replication factor 1)
- **Behavior**: Waits for Kafka to be ready before creating topics

## Monitoring Infrastructure

### VictoriaMetrics

Time-series database for storing metrics:

- **Image**: victoriametrics/victoria-metrics:v1.96.0
- **Port**: 8428
- **Configuration**:
  - Storage path: /victoria-metrics-data
  - HTTP listener address: :8428
- **Data Persistence**: Uses a dedicated volume (`vm-data`)
- **API Endpoints**:
  - Write API: http://victoriametrics:8428/api/v1/write
  - Query API: http://victoriametrics:8428/api/v1/query

### Grafana

Visualization platform for metrics:

- **Image**: grafana/grafana:10.2.0
- **Port**: 3000
- **Default Credentials**: 
  - Username: admin
  - Password: admin
- **Data Source**: VictoriaMetrics (automatically configured)
- **Dashboards**: Pre-configured Kafka metrics dashboard
- **Data Persistence**: Uses a dedicated volume (`grafana-data`)

### OpenTelemetry Collector

Collector for metrics, traces, and logs:

- **Image**: otel/opentelemetry-collector-contrib:0.91.0
- **Ports**:
  - 4317: OTLP gRPC
  - 4318: OTLP HTTP
  - 8888: Metrics endpoint
- **Configuration**: See `otel-collector-config.yaml`

## Pre-configured Dashboards

### Kafka Metrics Dashboard

A comprehensive dashboard for monitoring Kafka performance:

- **Message Throughput**: 
  - Messages in/out by topic
  - Bytes in/out rates
  
- **Performance Metrics**:
  - Request times for produce and consume operations
  - CPU usage
  - JVM heap usage
  
- **Cluster Health**:
  - Partition count
  - Offline partitions
  - Under-replicated partitions
  
- **Client Metrics**:
  - Producer message rates
  - Consumer message rates

## Network Configuration

All services are connected to a dedicated Docker network called `kafka-network`, allowing them to communicate with each other using service names as hostnames.

## Data Volumes

The setup includes the following persistent volumes:

- `kafka-data`: Stores Kafka data
- `vm-data`: Stores VictoriaMetrics data
- `grafana-data`: Stores Grafana configuration and dashboards

## Usage Instructions

### Starting the Infrastructure

```bash
docker-compose up -d
```

### Accessing Services

- **Kafka**: localhost:9092
- **VictoriaMetrics**: http://localhost:8428
- **Grafana**: http://localhost:3000 (admin/admin)

### Connecting Applications to Kafka

Producer and consumer applications should connect to:

- **Bootstrap Servers**: localhost:9092
- **Topics**: `incoming`, `outgoing`

### Sending Metrics with OpenTelemetry

Applications can send metrics to the OpenTelemetry Collector at:

- **OTLP gRPC**: localhost:4317
- **OTLP HTTP**: localhost:4318

## Instrumenting Applications

### Java Applications

For Java applications, add the OpenTelemetry agent and configure it to send metrics to the collector:

```java
// Example configuration
Properties properties = new Properties();
properties.put("bootstrap.servers", "localhost:9092");
properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

// OpenTelemetry configuration
System.setProperty("otel.metrics.exporter", "otlp");
System.setProperty("otel.exporter.otlp.endpoint", "http://localhost:4317");
System.setProperty("otel.resource.attributes", "service.name=my-kafka-producer");
```

## Additional Information

For more details on the individual components:

- [Kafka Documentation](https://kafka.apache.org/documentation/)
- [VictoriaMetrics Documentation](https://docs.victoriametrics.com/)
- [Grafana Documentation](https://grafana.com/docs/)
- [OpenTelemetry Documentation](https://opentelemetry.io/docs/)
