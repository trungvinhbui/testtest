package com.testcrawler4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;

/**
 * @author Yasser Ganjisaffar
 */
public class CrawlerMain {
    private static final Logger logger = LoggerFactory.getLogger(CrawlerMain.class);
    private static final String FILENAME = "com/testcrawler4j/app.config";
    public static String seed = "";
    public static String crawlStorageFolder = "";

    public static void main(String[] args) throws Exception {
        Properties prop = new Properties();
        try {
            // the configuration file name
            ClassLoader classLoader = CrawlerMain.class.getClassLoader();
            // Make sure that the configuration file exists
            URL res = Objects.requireNonNull(classLoader.getResource(FILENAME), "Can't find configuration file app.config");
            InputStream is = new FileInputStream(res.getFile());
            // load the properties file
            prop.load(is);
            // Read config file
            int depth = Integer.parseInt(prop.getProperty("depth"));
            int numberOfCrawlers = Integer.parseInt(prop.getProperty("numberOfCrawlers"));
            seed = prop.getProperty("seed");
            crawlStorageFolder = prop.getProperty("crawlStorageFolder");
            // setup crawler: config
            CrawlConfig config = new CrawlConfig();
            config.setCrawlStorageFolder(crawlStorageFolder);
            config.setMaxDepthOfCrawling(depth);
            // setup crawler: pageFetcher
            PageFetcher pageFetcher = new PageFetcher(config);
            // setup crawler: robotstxt
            RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
            RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
            // Start
            CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
            controller.addSeed(seed);
            controller.start(MyCrawler.class, numberOfCrawlers);
            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}