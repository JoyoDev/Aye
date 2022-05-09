package com.joyodev.aye.operator;

import com.joyodev.aye.model.ImageVulnerabilityMetric;
import com.joyodev.aye.model.ScanResponse;
import com.joyodev.aye.model.generator.ImageScanMetricsGenerator;
import com.joyodev.aye.model.parser.ImageScanParser;
import com.joyodev.aye.util.TimeCalculator;
import io.micrometer.core.instrument.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ClusterImageOperator implements Operator {

    private List<String> currentImages;

    private List<String> previousImages;

    private final Map<String, LocalDateTime> failedAnalysisTime;

    private final CLIRunner cliRunner;

    // Prometheus metrics
    private final Counter addedImagesCount;

    private final Counter failedImagesCount;

    private final Counter failedAnalysisCount;

    private final MeterRegistry meterRegistry;

    // Enable detailed metrics in expose function
    private final boolean detailedMetricsEnabled;


    public ClusterImageOperator(MeterRegistry meterRegistry) {
        this.currentImages = new ArrayList<>();
        this.previousImages = new ArrayList<>();
        this.failedAnalysisTime = new HashMap<>();
        this.cliRunner = new CLIRunner();
        this.meterRegistry = meterRegistry;

        this.detailedMetricsEnabled = Boolean.parseBoolean(System.getenv("ENABLE_DETAILED_METRICS"));

        addedImagesCount = Counter.builder("aye.added.images")
                .tag("service", "aye")
                .description("Number of added images")
                .register(meterRegistry);

        failedImagesCount = Counter.builder("aye.failed.images")
                .tag("service", "aye")
                .description("Number of images that failed scanning")
                .register(meterRegistry);

        failedAnalysisCount = Counter.builder("aye.failed.analysis")
                .tag("service", "aye")
                .description("Number of failed analysis")
                .register(meterRegistry);

    }

    @Override
    public void setCurrentImages(List<String> images) {
        this.previousImages = this.currentImages;
        this.currentImages = images;
    }

    @Override
    public boolean addImage(String image) {
        String output = cliRunner.exec("anchore-cli", "image", "add", image);
        if (output == null) {
            log.error("Anchore CLI failed to add image {} to the Anchore", image);
            return false;
        }
        if(checkUnauthorized(output))
            return false;
        if (checkIfErrorOccurred(output))
            return false;

        log.debug("Image {} added to the Anchore", image);
        return true;
    }

    @Override
    public boolean addFailedAnalysisImage(String image) {
        log.debug("ime of last analysis for image {} is {}", image, failedAnalysisTime.get(image));
        if (TimeCalculator.calculateTimeSince(failedAnalysisTime.get(image)) > 15) {
            log.debug("Adding image {} that previously failed analysis", image);
            boolean added = addImage(image);
            if (!added) {
                log.error("Anchore CLI failed to add image {} to the Anchore", image);
                return false;
            }
            failedAnalysisTime.replace(image, LocalDateTime.now());
            return true;
        }
        log.debug("Waiting until 15 minutes have passed since last attempt of analysis for image {}", image);
        return true;
    }

    @Override
    public String checkImageStatus(String image) {
        String output = cliRunner.exec("anchore-cli", "image", "get", image);
        if (output == null) {
            log.error("Anchore CLI failed to get status for image {}", image);
            return null;
        }
        if(checkUnauthorized(output))
            return null;
        if (checkIfErrorOccurred(output))
            return null;

        log.debug("Got status for image {}", image);
        return output;
    }

    @Override
    public String checkImageEvaluationStatus(String image) {
        String output = cliRunner.exec("anchore-cli", "evaluate", "check", image);
        if (output == null) {
            log.error("Anchore CLI failed to get evaluation status for image {}", image);
            return null;
        }
        if(checkUnauthorized(output))
            return null;

        if (checkIfErrorOccurred(output))
            return null;

        log.debug("Got evaluation status for image {}", image);
        return output;
    }

    @Override
    public ScanResponse getImageScanResults(String image) {
        log.debug("Getting scan results for image {}", image);
        String output = cliRunner.exec("anchore-cli", "--json", "image", "vuln", image, "all");
        if (output == null) {
            log.error("Anchore CLI failed to get scan response for image {}", image);
            return null;
        }
        if(checkUnauthorized(output))
            return null;

        if(checkIfErrorOccurred(output))
            return null;
        if(!checkIfScanResponseIsValid(output))
            return null;

        ScanResponse scanResponse  = ImageScanParser.jsonToScanResponse(output);

        if(scanResponse.getImageDigest() == null) {
            log.error("Failed to parse scan response for image {}", image);
            return null;
        }

        log.debug("Got scan results for image {}", image);
        return scanResponse;
    }

    @Override
    public void operate() {
        log.info("Scanning started");
        for(String image : currentImages) {
            String status = checkImageStatus(image);
            String evalStatus = checkImageEvaluationStatus(image);

            if(!checkIfAnalyzed(status) && !checkIfAnalyzing(status) || !checkIfPassed(evalStatus)) {
                if (checkIfFailedAnalysis(status)) {
                    failedAnalysisCount.increment();
                    log.warn("Analysis failed for image {}", image);
                    if(!failedAnalysisTime.containsKey(image)) {
                        log.debug("Added image to failed analysis");
                        failedAnalysisTime.put(image, LocalDateTime.now());
                    } else {
                        boolean successAdding = addFailedAnalysisImage(image);
                        if(!successAdding)
                            log.error("Error adding image {}", image);
                    }
                }
                else if(checkIfFailed(evalStatus)) {
                    failedImagesCount.increment();
                    exposeScanResult(image);
                    log.warn("Image {} failed scanning", image);
                }
                else {
                    if(checkIfAnalyzing(status))
                        log.debug("Image {} is being analyzed at the moment", image);
                    else {
                        boolean successAdding = addImage(image);
                        if(!successAdding)
                            log.error("Error adding image {}", image);
                        else
                            addedImagesCount.increment();
                    }
                }
            }
            else {
                log.debug("Image {} is already analyzed and passed", image);
                exposeScanResult(image);
            }
        }
        log.info("Analysis loop completed");
    }

    @Override
    public void exposeScanResult(String image) {
        ScanResponse scanResponse = getImageScanResults(image);
        if(scanResponse == null) {
            log.error("Could not get scan results for image {} to expose", image);
        }
        else {
            log.debug("Exposing scan results for image {}", image);
            ImageVulnerabilityMetric imageVulnerabilityMetric = ImageScanMetricsGenerator.generateImageVulnerabilityMetricFromScanResult(image, scanResponse);
            for(Map.Entry<String, Integer> set : imageVulnerabilityMetric.getNumberOfVulnerabilities().entrySet()) {
                Gauge.builder("aye.image.severity.vulnerabilities", set, Map.Entry::getValue)
                        .tags(Tags.of(Tag.of("image", image), Tag.of("severity", set.getKey())))
                        .description("Image scan result in form of: severity - number of vulnerabilities for that severity")
                        .register(meterRegistry);
            }

            if(detailedMetricsEnabled) {
                log.info("Exposing detailed image scan report for image {}", image);
                // Expose all of image's vulnerabilities with Package name and URL
                List<String> vulns = imageVulnerabilityMetric.getImageVulnerabilities().stream()
                        .map(v -> String.format("Package: %s URL: %s", v.getVulnerabilityPackage(),v.getUrl())).collect(Collectors.toList());
                Gauge.builder("aye.image.vulnerability.details", vulns, List::size)
                        .tags(Tags.of(Tag.of("image", image), Tag.of("vulnerabilities", vulns.toString())))
                        .description("All vulnerabilities of the image with Package name and URL")
                        .register(meterRegistry);
            }
        }
    }

    @Override
    public void deleteScanMetric(String image) {

    }

    @Override
    public void clearOldMetrics() {

    }

    @Override
    public boolean checkUnauthorized(String output) {
        if (output.contains("Unauthorized")) {
            log.warn("Application is not authorized to access Anchore API. Please provide valid credentials.");
            return true;
        }
        return false;
    }

    @Override
    public boolean checkIfErrorOccurred(String output) {
        if(output.contains("error_codes") || output.contains("Error")) {
            log.warn("{}", output);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkIfAnalyzed(String output) {
        return output != null && output.contains("analyzed") && !output.contains("not_analyzed");
    }

    @Override
    public boolean checkIfPassed(String output) {
        return output != null && output.contains("pass");
    }

    @Override
    public boolean checkIfAnalyzing(String output) {
        return output != null && output.contains("analyzing");
    }

    @Override
    public boolean checkIfFailedAnalysis(String output) {
        return output != null && output.contains("analysis_failed");
    }

    @Override
    public boolean checkIfFailed(String output) {
        return output != null && output.contains("fail");
    }

    @Override
    public boolean checkIfScanResponseIsValid(String output) {
        if(!output.contains("\"vulnerability_type\": \"all\"")) {
            log.warn("Scan response is not valid. {}", output);
            return false;
        }
        return true;
    }

}
