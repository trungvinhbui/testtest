package com.testcrawler4j;

import java.io.*;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yasser Ganjisaffar
 */
public class MyCrawler extends WebCrawler {
    private static final Logger logger = LoggerFactory.getLogger(CrawlerMain.class);
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp4|zip|gz))$");

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches() && href.startsWith(CrawlerMain.seed);
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        System.out.println("URL: " + url);
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String filename = CrawlerMain.crawlStorageFolder + htmlParseData.getTitle();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            try {
                BufferedWriter bwTxt = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename + ".txt"), "UTF-8"));
                bwTxt.write(text);
                bwTxt.flush();
                BufferedWriter bwHtml = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename + ".html"), "UTF-8"));
                bwHtml.write(html);
                bwHtml.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}