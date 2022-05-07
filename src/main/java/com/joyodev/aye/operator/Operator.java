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
    boolean addImage(String image);

    /**
     * Adds image to Anchore that previously failed analysis
     * @param image Passed image
     */
    boolean addFailedAnalysisImage(String image);

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

    /**
     * Checks if application is authorized to communicate with Anchore Engine
     * @return true if unauthorized, else false
     */
    boolean checkUnauthorized(String output);

    /**
     * Checks if Anchore Engine returned errors such as 400 or 404
     * @param output
     * @return true if error_codes exists in output, else false
     */
    boolean checkIfErrorOccurred(String output);

    /**
     * Checks if image is analyzed by Anchore Engine
     * @param output
     * @return true if analyzed, else false
     */
    boolean checkIfAnalyzed(String output);

    /**
     * Checks if image passed scanning process
     * @param output
     * @return true if pass is in the output, else false
     */
    boolean checkIfPassed(String output);

    /**
     * Checks if image is being analyzed
     * @param output
     * @return true if output contains analyzing, else false
     */
    boolean checkIfAnalyzing(String output);

    /**
     * Checks whether image failed analysis
     * @param output
     * @return rue if output contains analysis_failed, else false
     */
    boolean checkIfFailedAnalysis(String output);

    /**
     * Checks if image failed scanning process
     * @param output
     * @return true if fail is in the output, else false
     */
    boolean checkIfFailed(String output);
}
