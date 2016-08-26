package Utilities;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by gshilpa on 8/26/16.
 */
public class BMProxyHandler {

   static  List<HarEntry> entries;
    public static void captureTheNetworkTraffic(BrowserMobProxy server)
    {
        Har har = server.getHar();
        HarLog log = har.getLog();
        entries= new CopyOnWriteArrayList<HarEntry>(log.getEntries());
        PrintMsg.info("Started capturing the Analytics data");
        for (HarEntry entry : entries){
            System.out.println(entry.getRequest().getUrl());
        }
        PrintMsg.info("Analytics Data captured successuffy");
    }

    public static void verifyAnalyticsForTheUrlPatter(String url)
    {
        if(entries.size()!=0)
        {
            PrintMsg.info("Number or request captured "+ entries.size());
            for (HarEntry entry : entries){
                if(entry.getRequest().getUrl().contains(url))
                {
                    System.out.println("Found match for the "+url+" Actual Url is "+entry.getRequest().getUrl());
                    System.out.println("Analytics Querry parameters are....");
                    System.out.println(entry.getRequest().getQueryString());
                    PrintMsg.lineSeparator();
                }
            }
        }
        else
        {
            PrintMsg.info("Unable to capture the Web Analytics Data");
        }
    }
}
