package com.joyodev.aye.operator;

import java.util.List;

public interface Operator {

    /**
     * Passes current images on cluster to the Operator
     * @param images The list of current cluster images
     */
    void setCurrentImages(List<String> images);

    /**
     * Adds image to the Anchore
     * @param image Passed image
     */
    void addImage(String image);

    /**
     * Adds image to Anchore that previously failed analysis
     * @param image Passed image
     */
    void AddFailedAnalysisImage(String image);

    /**
     * Gets status for specific image from Anchore
     * @param image Passed image
     * @return Analysis status as String
     */
    String checkImageStatus(String image);

    /**
     * Gets image evaluation status from Anchore
     * @param image Passed image
     * @return Evaluation status as String
     */
    String checkImageEvaluationStatus(String image);

    /**
     * Performs image scanning and metric exposing
     */
    void operate();

    /**
     * Exposes scan result to Prometheus
     * @param image Passed image
     */
    void exposeScanResult(String image);

    /**
     * Deletes old metric that is not needed anymore
     * @param image Passed image
     */
    void deleteScanMetric(String image);

    /**
     * Calls deleteScanMetric for each old image
     */
    void clearOldMetrics();
}
