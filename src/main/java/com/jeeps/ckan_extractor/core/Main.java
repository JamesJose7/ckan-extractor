package com.jeeps.ckan_extractor.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import static com.jeeps.ckan_extractor.core.SemanticCreator.CURRENT_COUNTRY;
import static com.jeeps.ckan_extractor.core.SemanticCreator.CURRENT_PLATFORM;

public class Main {
    public static void main(String[] args) throws IOException {
        Instant start = Instant.now();

        //extractCkanData();
        extractSDGData();

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.printf("Elapsed time:\n" +
                "millis: %d\n" +
                "seconds: %d\n", timeElapsed, (timeElapsed / 1000));
    }

    private static void extractSDGData() {
        SdgExtractor sdgExtractor = new SdgExtractor();

        sdgExtractor.extract();
    }

    private static void extractCkanData() throws FileNotFoundException {
        CkanExtractor ckanExtractor = new CkanExtractor();

//        CURRENT_COUNTRY = "Ecuador";
//        CURRENT_PLATFORM = "Ambar UTPL";
        ckanExtractor.extract("http://ambar.utpl.edu.ec/api/action/");

//        CURRENT_COUNTRY = "";
//        CURRENT_PLATFORM = "EU Open Data Portal";
        ckanExtractor.extractByPost("http://data.europa.eu/euodp/data/api/3/action/");

//        CURRENT_COUNTRY = "Netherlands";
//        CURRENT_PLATFORM = "The Humanitarian Data Exchange";
        ckanExtractor.extract("https://data.humdata.org/api/3/action/");

        CURRENT_COUNTRY = "Switzerland";
        CURRENT_PLATFORM = "Switzerland government";
        ckanExtractor.extract("https://opendata.swiss/api/3/action/");

//        CURRENT_COUNTRY = "Australia";
//        CURRENT_PLATFORM = "Australian government";
        ckanExtractor.extract("https://data.gov.au/api/3/action/");

        ckanExtractor.writeFile();
    }
}