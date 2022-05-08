package com.joyodev.aye.model.parser;

import com.joyodev.aye.model.ScanResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ImageScanParserTest {

    @Test
    public void testJsonToScanResponseWhenGivenValid() {
        // given
        String response = "{\n" +
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
        ScanResponse scanResponse = ImageScanParser.jsonToScanResponse(response);

        // then
        assertTrue(scanResponse.getImageDigest().contains("sha256:bdf44f19d09b558203306836a612cc8e42a1106b2f731fbeb000e2696c04f9c8"));

    }

    @Test
    public void testJsonToScanResponseWhenGivenInvalid() {
        // given
        String response = "{\n" +
                "    \"message\": \"cannot use input image string (no discovered imageDigest)\"\n" +
                "}\n";
        // when
        ScanResponse scanResponse = ImageScanParser.jsonToScanResponse(response);

        // then
        System.out.println(scanResponse);
        assertTrue(scanResponse.getImageDigest() == null);
    }
}
