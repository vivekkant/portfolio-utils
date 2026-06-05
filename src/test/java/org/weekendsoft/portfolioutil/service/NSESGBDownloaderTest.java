package org.weekendsoft.portfolioutil.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.weekendsoft.portfolioutil.model.Price;

public class NSESGBDownloaderTest {

    @Test
    public void testDownload() throws Exception {
        NSESGBDownloader downloader = new NSESGBDownloader("/Users/vivekkant/Dropbox/Account/Portfolio/REF/SGB.csv");

        List<String> symbols = new ArrayList<>();
        
        symbols.add("SGBJ28VIII");

        Map<String, Price> prices = downloader.download(symbols);
        Assert.assertNotNull(prices);
    }

}
