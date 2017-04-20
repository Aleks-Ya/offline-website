package ru.yaal.offlinewebsite.impl.packager;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import ru.yaal.offlinewebsite.TestFactory;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.RootSiteUrl;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * TODO assert packaging result
 *
 * @author Aleksey Yablokov
 */
public class UuidLinkPackagerTest {
    @Test
    public void pack() throws IOException {
        URL rootPageHtml = getClass().getResource("root_page.html");
        String rootUrlStr = rootPageHtml.toString();
        RootSiteUrl rootSiteUrl = new SiteUrlImpl(rootUrlStr);
        TestFactory factory = new TestFactory(rootSiteUrl);
        Packager packager = factory.getUuidLinkPackager();

        SiteUrl siteUrl = new SiteUrlImpl(rootUrlStr + "/nested_page.html");
        String html = IOUtils.toString(rootPageHtml, Charset.defaultCharset());
        ResourceId<PackagingRes> packagingRes = factory.createPackagingRes(siteUrl, html, TestFactory.httpInfoDefault);
        ResourceId<PackagedRes> packagedResId = packager.pack(packagingRes);
        PackagedRes packagedRes = factory.getStorage().getResource(packagedResId);

        Path path = packagedRes.getLocation();
        List<String> lines = Files.readAllLines(path);
        System.out.println(lines);
    }

}