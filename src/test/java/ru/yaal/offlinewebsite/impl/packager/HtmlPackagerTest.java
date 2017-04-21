package ru.yaal.offlinewebsite.impl.packager;

import org.junit.Ignore;
import org.junit.Test;
import ru.yaal.offlinewebsite.TestFactory;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.RootPageUrl;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.impl.params.PageUrlImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static ru.yaal.offlinewebsite.TestFactory.httpInfoDefault;

/**
 * @author Aleksey Yablokov
 */
public class HtmlPackagerTest {
    @Test
    @Ignore
    public void pack() throws IOException {
        RootPageUrl rootPageUrl = new PageUrlImpl("http://ya.ru/info");
        String html = "<html><body><a href='http://ya.ru/link'/></body></html>";
        TestFactory factory = new TestFactory(rootPageUrl);
        Packager packager = factory.getCopyPackager();
        ResourceId<PackagingRes> packagingResId = factory.createPackagingRes(rootPageUrl, html, httpInfoDefault);
        PageUrlImpl nestedPageUrl = new PageUrlImpl("http://ya.ru/link");
//        factory.createPackagedRes(nestedPageUrl, "<html></html>", TestFactory.httpInfoDefault);
        ResourceId<PackagedRes> packedRedId = packager.pack(packagingResId);
        PackagedRes packedRes = factory.getStorage().getResource(packedRedId);

        List<String> content = Files.readAllLines(packedRes.getLocation());
        assertThat(content, contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
                "<html><head></head><body><a href=\"http://ya.ru/link\"></a></body></html>"));
    }
}