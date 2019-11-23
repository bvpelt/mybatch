# Monitoring
## Prometheus
Get prometheus image
```
docker pull prom/prometheus 
```
Set configuration of prometheus in prometheus.yml
``` 
# my global config
global:
  scrape_interval:     15s # Set the scrape interval to every 15 seconds. Default is every 1 minute.
  evaluation_interval: 15s # Evaluate rules every 15 seconds. The default is every 1 minute.
  # scrape_timeout is set to the global default (10s).

# Load rules once and periodically evaluate them according to the global 'evaluation_interval'.
rule_files:
  # - "first_rules.yml"
  # - "second_rules.yml"

# A scrape configuration containing exactly one endpoint to scrape:
# Here it's Prometheus itself.
scrape_configs:
  # The job name is added as a label `job=<job_name>` to any timeseries scraped from this config.
  - job_name: 'prometheus'
    # metrics_path defaults to '/metrics'
    # scheme defaults to 'http'.
    metrics_path: '/actuator/metrics'
    scrape_interval: 5s
    static_configs:
    - targets: ['127.0.0.1:9090']

  - job_name: 'spring-actuator'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 5s
    static_configs:
    - targets: ['HOST_IP:8080']
```
Run docker image
``` 
docker run -d \
    --name=prometheus \
    -p 9090:9090 \
    -v <PATH_TO_prometheus.yml_FILE>:/etc/prometheus/prometheus.yml \
    prom/prometheus \
    --config.file=/etc/prometheus/prometheus.yml
```
in my case this is
``` 
docker run -d \
    --name=prometheus \
    -p 9090:9090 \
    -v /home/bvpelt/Develop/mybatch/src/main/resources/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml \
    prom/prometheus \
    --config.file=/etc/prometheus/prometheus.yml
```
View prometheus at http://localhost:9090/

## Add grafana
``` 
docker run -d --name=grafana -p 3000:3000 grafana/grafana 
```
View grafana at http://localhost:3000

Find network information of running prometheus container

``` 
docker inspect prometheus -f "{{json .NetworkSettings.Networks }}" | jq '.'
{
  "bridge": {
    "IPAMConfig": null,
    "Links": null,
    "Aliases": null,
    "NetworkID": "44061eb2412c4b4edc2f597860389a8a993aaf539a54e4d9ed1397b910588f1f",
    "EndpointID": "4a50ac0aef2e2bc21a7ca33da5d91acb42e052572a725c9e61445f0468700242",
    "Gateway": "172.17.0.1",
    "IPAddress": "172.17.0.2",
    "IPPrefixLen": 16,
    "IPv6Gateway": "",
    "GlobalIPv6Address": "",
    "GlobalIPv6PrefixLen": 0,
    "MacAddress": "02:42:ac:11:00:02",
    "DriverOpts": null
  }
}
```
Use the ipaddress of prometheus in grafana datasource configuration