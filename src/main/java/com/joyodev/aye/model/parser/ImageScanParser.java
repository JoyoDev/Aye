package com.joyodev.aye.model.parser;

import com.google.gson.Gson;
import com.joyodev.aye.model.ScanResponse;

public class ImageScanParser {

    public static ScanResponse jsonToScanResponse (String jsonResponse) {
        return new Gson().fromJson(jsonResponse, ScanResponse.class);
    }
}
