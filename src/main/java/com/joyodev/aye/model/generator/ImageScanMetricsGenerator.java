package com.joyodev.aye.model.generator;

import com.joyodev.aye.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageScanMetricsGenerator {

    // Generates ImageVulnerabilityResult from Anchore's' scan response/results
    public static ImageVulnerabilityResult generateImageVulnerability(String image, ScanResponse scanResponse) {
        List<ImageVulnerability> vulnerabilities = new ArrayList<>();
        for(VulnerabilitiesItem vulnerabilitiesItem : scanResponse.getVulnerabilities()) {
            String vulnerabilityPackage = vulnerabilitiesItem.getPackageName();
            String severity = vulnerabilitiesItem.getSeverity();
            String vulnerability = vulnerabilitiesItem.getVuln();
            String url = vulnerabilitiesItem.getUrl();

            ImageVulnerability imageVulnerability = new ImageVulnerability(vulnerabilityPackage, severity, vulnerability, url);

            vulnerabilities.add(imageVulnerability);
        }

        return new ImageVulnerabilityResult(image, vulnerabilities);
    }

    // Generates number of vulnerabilities for each severity
    public static Map<String, Integer> generateNumberOfVulnerabilitiesForSeverities(List<ImageVulnerability> imageVulnerabilities) {
        Map<String, Integer> result = new HashMap<>();

        for(ImageVulnerability imageVulnerability : imageVulnerabilities) {
            result.compute(imageVulnerability.getSeverity(), (key, val) -> (val == null) ? 1 : val + 1);
        }

        return result;
    }

    // Generates metric that will be exposed to Prometheus
    public static ImageVulnerabilityMetric generateImageVulnerabilityMetric(String image, ImageVulnerabilityResult imageVulnerabilityResult) {
        return new ImageVulnerabilityMetric(image, imageVulnerabilityResult.getImageVulnerabilities(),
                generateNumberOfVulnerabilitiesForSeverities(imageVulnerabilityResult.getImageVulnerabilities()));
    }

    // Generate Prometheus metric from Anchore's response/results using functions above
    public static ImageVulnerabilityMetric generateImageVulnerabilityMetricFromScanResult(String image, ScanResponse response) {
        return generateImageVulnerabilityMetric(image, generateImageVulnerability(image, response));
    }


}
