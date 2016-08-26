import Utilities.PrintMsg;
import Utilities.BMProxyHandler;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;
import net.sf.uadetector.internal.data.domain.Browser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by gshilpa on 8/23/16.
 */
public class VerifyWebAnalyticsForMobile {

    private static AppiumDriverLocalService service;
    private static AppiumServiceBuilder serBuilder;
    FileInputStream input;
    Properties prop=new Properties();
    protected static AndroidDriver<MobileElement> driverAndroid;
    String baseURL = "https://www.thoughtworks.com";
    String navigateToURL = baseURL + "/mingle/signup/";
    String urlPattern="https://www.google-analytics.com/collect";

    BrowserMobProxy server;

    @Before
    public void setup() throws IOException {

        server = new BrowserMobProxyServer();
        server.start(5555);
        Proxy proxy = ClientUtil.createSeleniumProxy(server);
        input= new FileInputStream("config.properties");
        prop.load(input);
        PrintMsg.info("Initializing the appium server at port 7000");
        serBuilder=new AppiumServiceBuilder().withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js")).usingPort(7000);
        service = serBuilder.build();
        service.start();
        if (service == null || !service.isRunning()) {
            throw new RuntimeException("Unable to start Appium node server");
        }
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, prop.getProperty("BROWSER_NAME"));
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, prop.getProperty("DEVICE_NAME"));
        capabilities.setCapability(MobileCapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(CapabilityType.PROXY, proxy);
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("ignore-certificate-errors");
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        if(prop.getProperty("PLATFORM_NAME").equalsIgnoreCase("Android"))
        {
            capabilities.setCapability(AndroidMobileCapabilityType.AVD_LAUNCH_TIMEOUT, 500000);
            driverAndroid= new AndroidDriver<>(service.getUrl(), capabilities);
            driverAndroid.get(baseURL);
        }

        server.newHar("newHar");
    }


    @After
    public void tearDown()
    {
        driverAndroid.quit();
        server.stop();
    }

    @Test
    public void captureWebAnalyticsDataForAndroidMobile() throws Throwable {
        driverAndroid.get(navigateToURL);
        try{
            PrintMsg.info("Waiting for page to load successfully");
            Thread.sleep(10000);
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        BMProxyHandler.captureTheNetworkTraffic(server);
        BMProxyHandler.verifyAnalyticsForTheUrlPatter(urlPattern);
    }

}
