package com.joyodev.aye.operator;

import com.joyodev.aye.util.TimeCalculator;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ClusterImageOperator implements Operator {

    private List<String> currentImages;

    private List<String> previousImages;

    private Map<String, LocalTime> failedAnalysisTime;

    private CLIRunner cliRunner;

    public ClusterImageOperator() {
        this.currentImages = new ArrayList<>();
        this.previousImages = new ArrayList<>();
        this.failedAnalysisTime = new HashMap<>();
        this.cliRunner = new CLIRunner();
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
            log.error("Failed to add image {} to the Anchore", image);
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
    public boolean AddFailedAnalysisImage(String image) {
        log.debug("ime of last analysis for image {} is {}", image, failedAnalysisTime.get(image));
        if (TimeCalculator.calculateTimeSince(failedAnalysisTime.get(image)) > 15) {
            log.debug("Adding image {} that previously failed analysis", image);
            boolean added = addImage(image);
            if (!added) {
                log.error("Failed to add image {} to the Anchore", image);
                return false;
            }
            failedAnalysisTime.replace(image, LocalTime.now());
            return true;
        }
        log.debug("Waiting until 15 minutes have passed since last attempt of analysis for image {}", image);
        return true;
    }

    @Override
    public String checkImageStatus(String image) {
        String output = cliRunner.exec("anchore-cli", "image", "get", image);
        if (output == null) {
            log.error("Failed to get status for image {}", image);
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
            log.error("Failed to get evaluation status for image {}", image);
            return null;
        }
        if(checkUnauthorized(output))
            return null;

        log.debug("Got evaluation status for image {}", image);
        return output;
    }

    @Override
    public void operate() {
        log.info("Scanning started...");
        for(String image : currentImages) {
            System.out.println(image);
        }
    }

    @Override
    public void exposeScanResult(String image) {

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
        if(output.contains("error_codes")) {
            log.warn("Anchore returned error.");
            return true;
        }
        return false;
    }

    @Override
    public boolean checkIfAnalyzed(String output) {
        return output.contains("analyzed");
    }

    @Override
    public boolean checkIfPassed(String output) {
        return output.contains("pass");
    }

    @Override
    public boolean checkIfAnalyzing(String output) {
        return output.contains("analyzing");
    }

    @Override
    public boolean checkIfFailedAnalysis(String output) {
        return output.contains("analysis_failed");
    }


}
