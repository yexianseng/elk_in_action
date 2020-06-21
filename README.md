################## Purpose of this file #########################
##################   OS macpro 19.5.0#############
With ELK (Elastic 7.8  + logstash 7.8  + Kibana 7.8 ) to maintain all micro service log , same with only one service log 
if between diff mciro svc , need add slenth and feign dependency , it will help auto generate and pass trace id between diff micro svc .

!. Step 1 , install elastic search with defaut setting 
            set network host to localhost if other value enable 
	    set maxcimal con
	    start elasticsearch and access via port 9200
            ./bin/elasticsearch

2. Step 2 , install kinaba 
	    ./kibana

3. Step 3 , install logstash
            touch logstash.conf 
            define listening port and data type (in my case , config as tcp json input )
	    config elasticsearch endpoint 

		Sample 
		# Sample Logstash configuration for creating a simple
# Beats -> Logstash -> Elasticsearch pipeline.

input {
  tcp {
    mode => "server"
    host => "0.0.0.0"
    port => 4560
    codec => json_lines
  }
}

output {
  elasticsearch {
    hosts => ["http://localhost:9200"]
    index => "springboot-logstash-%{+YYYY.MM.dd}"
    #user => "elastic"
    #password => "changeme"
  }
}


4 , Step 4 , change log4j in application 

<appender name="logstash"
                  class="net.logstash.logback.appender.LogstashTcpSocketAppender">
            <destination>localhost:4560</destination>
            <encoder charset="UTF-8" class="net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder">
                <providers>
                    <timestamp>
                        <timeZone>SGT</timeZone>
                    </timestamp>
                    <pattern>
                        <pattern>
                            {
                            "severity":"%level",
                            "service": "${springAppName:-}",
                            "trace": "%X{X-B3-TraceId:-}",
                            "span": "%X{X-B3-SpanId:-}",
                            "exportable": "%X{X-Span-Export:-}",
                            "pid": "${PID:-}",
                            "thread": "%thread",
                            "class": "%logger{40}",
                            "rest": "%message"
                            }
                        </pattern>
                    </pattern>
                </providers>
            </encoder>
        </appender>
        <appender name="async-logstash" class="ch.qos.logback.classic.AsyncAppender">
            <discardingThreshold>0</discardingThreshold>
            <queueSize>256</queueSize>
            <appender-ref ref="logstash"/>
        </appender>

5 , Step 5 , run the application and check from kibana 


http://localhost:5601/app/kibana#/home


