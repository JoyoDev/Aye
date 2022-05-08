package com.joyodev.aye.model.generator;

import com.joyodev.aye.model.ImageVulnerability;
import com.joyodev.aye.model.ImageVulnerabilityMetric;
import com.joyodev.aye.model.ImageVulnerabilityResult;
import com.joyodev.aye.model.ScanResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ImageScanMetricsGeneratorTest {

    @Test
    public void testGenerateImageVulnerability() {
        // given
        ScanResponse scanResponse = Mockito.mock(ScanResponse.class);
        String image = "testhub.io/test:latest";

        // when
        ImageVulnerabilityResult imageVulnerabilityResult = ImageScanMetricsGenerator.generateImageVulnerability(image, scanResponse);

        // then
        assertTrue(imageVulnerabilityResult.getImage() == image);
        assertTrue(imageVulnerabilityResult.getImageVulnerabilities().size() == scanResponse.getVulnerabilities().size());
    }

    @Test
    public void testGenerateNumberOfVulnerabilitiesForSeverities() {
        // given
        List<ImageVulnerability> imageVulnerabilities = new ArrayList<>();
        ImageVulnerability imageVulnerability1 = new ImageVulnerability("package1", "Unknown", "vuln1", "");
        ImageVulnerability imageVulnerability2 = new ImageVulnerability("package2", "High", "vuln2", "");
        ImageVulnerability imageVulnerability3 = new ImageVulnerability("package3", "Negligible", "vuln3", "");
        ImageVulnerability imageVulnerability4 = new ImageVulnerability("package4", "Negligible", "vuln4", "");
        imageVulnerabilities.add(imageVulnerability1);
        imageVulnerabilities.add(imageVulnerability2);
        imageVulnerabilities.add(imageVulnerability3);
        imageVulnerabilities.add(imageVulnerability4);

        // when
        Map<String, Integer> result = ImageScanMetricsGenerator.generateNumberOfVulnerabilitiesForSeverities(imageVulnerabilities);

        // then
        assertTrue(result.get("Negligible") == 2);
        assertTrue(result.get("High") == 1);
    }

    @Test
    public void testGenerateImageVulnerabilityMetric() {
        // given
        String image = "testhub.io/test:latest";
        List<ImageVulnerability> imageVulnerabilities = new ArrayList<>();
        ImageVulnerability imageVulnerability1 = new ImageVulnerability("package1", "Unknown", "vuln1", "");
        ImageVulnerability imageVulnerability2 = new ImageVulnerability("package2", "High", "vuln2", "");
        ImageVulnerability imageVulnerability3 = new ImageVulnerability("package3", "Negligible", "vuln3", "");
        ImageVulnerability imageVulnerability4 = new ImageVulnerability("package4", "Negligible", "vuln4", "");
        imageVulnerabilities.add(imageVulnerability1);
        imageVulnerabilities.add(imageVulnerability2);
        imageVulnerabilities.add(imageVulnerability3);
        imageVulnerabilities.add(imageVulnerability4);

        ImageVulnerabilityResult imageVulnerabilityResult = new ImageVulnerabilityResult(image, imageVulnerabilities);

        // when
        ImageVulnerabilityMetric imageVulnerabilityMetric = ImageScanMetricsGenerator.generateImageVulnerabilityMetric(image, imageVulnerabilityResult);

        // then
        assertTrue(imageVulnerabilityMetric.getNumberOfVulnerabilities().get("Negligible") == 2);
        assertTrue(imageVulnerabilityMetric.getNumberOfVulnerabilities().get("Unknown") == 1);
        assertTrue(imageVulnerabilityMetric.getNumberOfVulnerabilities().size() == 3);
    }
}
