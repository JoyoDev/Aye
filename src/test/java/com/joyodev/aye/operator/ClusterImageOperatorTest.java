package com.joyodev.aye.operator;

import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ClusterImageOperatorTest {

    private Operator operator;

    @Autowired
    MeterRegistry meterRegistry;

    @BeforeEach
    public void setup() {
        operator = new ClusterImageOperator(meterRegistry);
    }

    @Test
    public void testCheckIfAnalyzedWhenIs() {
        // given
        String output = "Image Digest: sha256:190bd1bd612d3d7adc761913edf81e1ebc7b437d3fcd372268bbfb60a91cd08f\n" +
                "Parent Digest: sha256:3a407bd33b2e42f8e7ba8f736765cda37cea1cdf40a97262169f18e3edcb9acb\n" +
                "Analysis Status: analyzed\n" +
                "Image Type: docker\n" +
                "Analyzed At: 2022-05-07T13:24:38Z\n" +
                "Image ID: 0183eb12bb0c0c20f6a78f68fef77535955eb14588217bc3a7af5e39b9e78cbc\n" +
                "Dockerfile Mode: Guessed\n" +
                "Distro: debian\n" +
                "Distro Version: 11\n" +
                "Size: 700344320\n" +
                "Architecture: amd64\n" +
                "Layer Count: 10\n" +
                "\n" +
                "Full Tag: docker.io/library/tomcat:latest\n" +
                "Tag Detected At: 2022-05-07T13:19:05Z\n";

        // when
        boolean analyzed = operator.checkIfAnalyzed(output);

        // then
        assertTrue(analyzed);
    }

    @Test
    public void testCheckIfAnalyzedWhenIsNot() {
        // given
        String output = "Image Digest: sha256:190bd1bd612d3d7adc761913edf81e1ebc7b437d3fcd372268bbfb60a91cd08f\n" +
                "Parent Digest: sha256:3a407bd33b2e42f8e7ba8f736765cda37cea1cdf40a97262169f18e3edcb9acb\n" +
                "Analysis Status: not_analyzed\n" +
                "Image Type: docker\n" +
                "Analyzed At: 2022-05-07T13:24:38Z\n" +
                "Image ID: 0183eb12bb0c0c20f6a78f68fef77535955eb14588217bc3a7af5e39b9e78cbc\n" +
                "Dockerfile Mode: Guessed\n" +
                "Distro: debian\n" +
                "Distro Version: 11\n" +
                "Size: 700344320\n" +
                "Architecture: amd64\n" +
                "Layer Count: 10\n" +
                "\n" +
                "Full Tag: docker.io/library/tomcat:latest\n" +
                "Tag Detected At: 2022-05-07T13:19:05Z\n";

        // when
        boolean analyzed = operator.checkIfAnalyzed(output);

        // then
        assertFalse(analyzed);
    }

    @Test
    public void testCheckIfPassedWhenHas() {
        // given
        String output = "Image Digest: sha256:66eb309b462dc9cf9a78d016362057250ffba2020ce34a72cf9b3f33fb54e515\n" +
                "Full Tag: docker.io/library/httpd:latest\n" +
                "Status: pass\n" +
                "Last Eval: 2022-05-07T19:13:04Z\n" +
                "Policy ID: 2c53a13c-1765-11e8-82ef-23527761d060\n";

        // when
        boolean passed = operator.checkIfPassed(output);

        // then
        assertTrue(passed);
    }

    @Test
    public void testCheckIfPassedWhenHasNot() {
        // given
        String output = "Image Digest: sha256:66eb309b462dc9cf9a78d016362057250ffba2020ce34a72cf9b3f33fb54e515\n" +
                "Full Tag: docker.io/library/httpd:latest\n" +
                "Status: fail\n" +
                "Last Eval: 2022-05-07T19:13:04Z\n" +
                "Policy ID: 2c53a13c-1765-11e8-82ef-23527761d060\n";

        // when
        boolean passed = operator.checkIfPassed(output);

        // then
        assertFalse(passed);
    }

    @Test
    public void testCheckIfAnalyzingWhenIs() {
        // given
        String output = "Image Digest: sha256:4b86ad59abc67fa19a6e1618e936f3fd0f6ae13f49260da55a03eeca763a0fb5\n" +
                "Parent Digest: sha256:2acfab1966f0dbecc6afbead13eca7f47062cfe8726bb9db25e39e0c0b88e9c3\n" +
                "Analysis Status: analyzing\n" +
                "Image Type: docker\n" +
                "Analyzed At: None\n" +
                "Image ID: a5bac665ffa2c7c5a6fa96e9e9e00b51af36a5ca78cdbd682d8a90b6107aec84\n" +
                "Dockerfile Mode: None\n" +
                "Distro: None\n" +
                "Distro Version: None\n" +
                "Size: None\n" +
                "Architecture: None\n" +
                "Layer Count: None\n" +
                "\n" +
                "Full Tag: docker.io/prom/prometheus:latest\n" +
                "Tag Detected At: 2022-05-07T19:11:54Z\n";

        // when
        boolean analyzing = operator.checkIfAnalyzing(output);

        // then
        assertTrue(analyzing);
    }

    @Test
    public void testCheckIfAnalyzingWhenIsNot() {
        // given
        String output = "Image Digest: sha256:4b86ad59abc67fa19a6e1618e936f3fd0f6ae13f49260da55a03eeca763a0fb5\n" +
                "Parent Digest: sha256:2acfab1966f0dbecc6afbead13eca7f47062cfe8726bb9db25e39e0c0b88e9c3\n" +
                "Analysis Status: analyzed\n" +
                "Image Type: docker\n" +
                "Analyzed At: None\n" +
                "Image ID: a5bac665ffa2c7c5a6fa96e9e9e00b51af36a5ca78cdbd682d8a90b6107aec84\n" +
                "Dockerfile Mode: None\n" +
                "Distro: None\n" +
                "Distro Version: None\n" +
                "Size: None\n" +
                "Architecture: None\n" +
                "Layer Count: None\n" +
                "\n" +
                "Full Tag: docker.io/prom/prometheus:latest\n" +
                "Tag Detected At: 2022-05-07T19:11:54Z\n";

        // when
        boolean analyzing = operator.checkIfAnalyzing(output);

        // then
        assertFalse(analyzing);
    }

    @Test
    public void testCheckIfFailedAnalysisWhenHas() {
        // given
        String output = "Image Digest: sha256:4b86ad59abc67fa19a6e1618e936f3fd0f6ae13f49260da55a03eeca763a0fb5\n" +
                "Parent Digest: sha256:2acfab1966f0dbecc6afbead13eca7f47062cfe8726bb9db25e39e0c0b88e9c3\n" +
                "Analysis Status: analysis_failed\n" +
                "Image Type: docker\n" +
                "Analyzed At: None\n" +
                "Image ID: a5bac665ffa2c7c5a6fa96e9e9e00b51af36a5ca78cdbd682d8a90b6107aec84\n" +
                "Dockerfile Mode: None\n" +
                "Distro: None\n" +
                "Distro Version: None\n" +
                "Size: None\n" +
                "Architecture: None\n" +
                "Layer Count: None\n" +
                "\n" +
                "Full Tag: docker.io/prom/prometheus:latest\n" +
                "Tag Detected At: 2022-05-07T19:11:54Z\n";

        // when
        boolean analysisFailed = operator.checkIfFailedAnalysis(output);

        // then
        assertTrue(analysisFailed);
    }

    @Test
    public void testCheckIfFailedAnalysisWhenHasNot() {
        // given
        String output = "Image Digest: sha256:4b86ad59abc67fa19a6e1618e936f3fd0f6ae13f49260da55a03eeca763a0fb5\n" +
                "Parent Digest: sha256:2acfab1966f0dbecc6afbead13eca7f47062cfe8726bb9db25e39e0c0b88e9c3\n" +
                "Analysis Status: analyzed\n" +
                "Image Type: docker\n" +
                "Analyzed At: None\n" +
                "Image ID: a5bac665ffa2c7c5a6fa96e9e9e00b51af36a5ca78cdbd682d8a90b6107aec84\n" +
                "Dockerfile Mode: None\n" +
                "Distro: None\n" +
                "Distro Version: None\n" +
                "Size: None\n" +
                "Architecture: None\n" +
                "Layer Count: None\n" +
                "\n" +
                "Full Tag: docker.io/prom/prometheus:latest\n" +
                "Tag Detected At: 2022-05-07T19:11:54Z\n";

        // when
        boolean analysisFailed = operator.checkIfFailedAnalysis(output);

        // then
        assertFalse(analysisFailed);
    }

    @Test
    public void testCheckIfFailedWhenHas() {
        // given
        String output = "Image Digest: sha256:66eb309b462dc9cf9a78d016362057250ffba2020ce34a72cf9b3f33fb54e515\n" +
                "Full Tag: docker.io/library/httpd:latest\n" +
                "Status: fail\n" +
                "Last Eval: 2022-05-07T19:13:04Z\n" +
                "Policy ID: 2c53a13c-1765-11e8-82ef-23527761d060\n";

        // when
        boolean failed = operator.checkIfFailed(output);

        // then
        assertTrue(failed);
    }

    @Test
    public void testCheckIfFailedWhenHasNot() {
        // given
        String output = "Image Digest: sha256:66eb309b462dc9cf9a78d016362057250ffba2020ce34a72cf9b3f33fb54e515\n" +
                "Full Tag: docker.io/library/httpd:latest\n" +
                "Status: pass\n" +
                "Last Eval: 2022-05-07T19:13:04Z\n" +
                "Policy ID: 2c53a13c-1765-11e8-82ef-23527761d060\n";

        // when
        boolean failed = operator.checkIfFailed(output);

        // then
        assertFalse(failed);
    }

    @Test
    public void testCheckIfErrorOccurredWhenHasWithError() {
        // given
        String output = "Error: image data not found in DB\n" +
                "HTTP Code: 404\n" +
                "Detail: {}\n";

        // when
        boolean errorOccurred = operator.checkIfErrorOccurred(output);

        // then
        assertTrue(errorOccurred);
    }

    @Test
    public void testCheckIfErrorOccurredWhenHasWithErrorCodes() {
        // given
        String output = "Image data not found in DB\n" +
                "HTTP Code: 404\n" +
                "Detail: {'error_codes': []}\n";

        // when
        boolean errorOccurred = operator.checkIfErrorOccurred(output);

        // then
        assertTrue(errorOccurred);
    }

    @Test
    public void testCheckIfErrorOccurredWhenHasNot() {
        // given
        String output = "Image Digest: sha256:66eb309b462dc9cf9a78d016362057250ffba2020ce34a72cf9b3f33fb54e515\n" +
                "Full Tag: docker.io/library/httpd:latest\n" +
                "Status: pass\n" +
                "Last Eval: 2022-05-07T19:13:04Z\n" +
                "Policy ID: 2c53a13c-1765-11e8-82ef-23527761d060\n";

        // when
        boolean errorOccurred = operator.checkIfErrorOccurred(output);

        // then
        assertFalse(errorOccurred);
    }

    @Test
    public void testCheckIfScanResponseIsValidWhenIs() {
        // given
        String output = "{\n" +
                "    \"imageDigest\": \"sha256:bdf44f19d09b558203306836a612cc8e42a1106b2f731fbeb000e2696c04f9c8\",\n" +
                "    \"vulnerabilities\": [{\n" +
                "            \"feed\": \"vulnerabilities\",\n" +
                "            \"feed_group\": \"debian:11\",\n" +
                "            \"fix\": \"None\",\n" +
                "            \"nvd_data\": [\n" +
                "                {\n" +
                "                    \"cvss_v2\": {\n" +
                "                        \"base_score\": 2.1,\n" +
                "                        \"exploitability_score\": 3.9,\n" +
                "                        \"impact_score\": 2.9\n" +
                "                    },\n" +
                "                    \"cvss_v3\": {\n" +
                "                        \"base_score\": -1.0,\n" +
                "                        \"exploitability_score\": -1.0,\n" +
                "                        \"impact_score\": -1.0\n" +
                "                    },\n" +
                "                    \"id\": \"CVE-2004-0971\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"package\": \"libkrb5-3-1.18.3-6+deb11u1\",\n" +
                "            \"package_cpe\": \"None\",\n" +
                "            \"package_cpe23\": \"None\",\n" +
                "            \"package_name\": \"libkrb5-3\",\n" +
                "            \"package_path\": \"pkgdb\",\n" +
                "            \"package_type\": \"dpkg\",\n" +
                "            \"package_version\": \"1.18.3-6+deb11u1\",\n" +
                "            \"severity\": \"Negligible\",\n" +
                "            \"url\": \"https://security-tracker.debian.org/tracker/CVE-2004-0971\",\n" +
                "            \"vendor_data\": [],\n" +
                "            \"vuln\": \"CVE-2004-0971\",\n" +
                "            \"will_not_fix\": false\n" +
                "        }\n],\n" +
                "    \"vulnerability_type\": \"all\"\n" +
                "}\n";

        // when
        boolean isValidResponse = operator.checkIfScanResponseIsValid(output);

        // then
        assertTrue(isValidResponse);
    }

    @Test
    public void testCheckIfScanResponseIsValidWhenIsNot() {
        // given
        String output = "{\n" +
                "    \"message\": \"cannot use input image string (no discovered imageDigest)\"\n" +
                "}\n";

        // when
        boolean isValidResponse = operator.checkIfScanResponseIsValid(output);

        assertFalse(isValidResponse);
    }
}
