package kr.pe.speech.crawler4j;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

public class HtmlCrawler extends WebCrawler {

    private final static Pattern EXCLUSIONS = Pattern.compile(".*(\\.(css|js|xml|gif|jpg|png|mp3|mp4|zip|gz|pdf))$");




    private CrawlerStatistics stats;

    public HtmlCrawler(CrawlerStatistics stats) {
        this.stats = stats;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String urlString = url.getURL().toLowerCase();
        return !EXCLUSIONS.matcher(urlString).matches()
                && urlString.startsWith("https://www.llsollu.com/");
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        stats.incrementProcessedPageCount();

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String title = htmlParseData.getTitle();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();
            stats.incrementTotalLinksCount(links.size());

            System.out.printf("Page with url '%s' %n", url);
            System.out.printf("Page with title '%s' %n", title);
            System.out.printf("    Text length: %d %n", text.length());
            System.out.printf("    HTML length: %d %n", html.length());
            System.out.printf("    %d outbound links %n", links.size());


            try {
                FileWriter fw = new FileWriter("result.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);

                bw.write(url);   bw.write("\t");
                bw.write(title);  bw.write("\n");

                /*
                Iterator<WebURL> it = links.iterator();
                while(it.hasNext()){
                    bw.write( "\t");
                    bw.write( it.next().getURL() );
                    bw.write( "\n");
                }*/

                bw.close();
                fw.close();
            }
            catch( IOException e){
                System.out.println("exception occurred" + e);
            }


        }
    }


}
