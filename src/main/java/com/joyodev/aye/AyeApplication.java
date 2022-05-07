package com.joyodev.aye;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.metrics.export.prometheus.EnablePrometheusMetrics;
import org.springframework.metrics.export.prometheus.EnablePrometheusScraping;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnablePrometheusMetrics
@EnablePrometheusScraping
public class AyeApplication {

	public static void main(String[] args) {
		SpringApplication.run(AyeApplication.class, args);
	}

}
