package ru.yaal.offlinewebsite.impl.packager;

import org.htmlcleaner.TagNode;
import org.junit.Test;
import ru.yaal.offlinewebsite.TestFactory;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static ru.yaal.offlinewebsite.TestFactory.httpInfoDefault;

/**
 * @author Aleksey Yablokov
 */
public class FolderPackagerTest {
    @Test
    public void pack() throws IOException {
        SiteUrl siteUrl = new SiteUrlImpl("http://ya.ru/info");
        String html = "<html><body><a href='http://ya.ru/link'/></body></html>";
        TestFactory factory = new TestFactory(siteUrl);
        Packager<TagNode> packager = factory.getPackager();
        ResourceId<PackagingRes<TagNode>> packagingResId = factory.createPackagingRes(siteUrl, html, httpInfoDefault);
        ResourceId<PackagedRes> packedRedId = packager.pack(packagingResId);
        PackagedRes packedRes = factory.getStorage().getResource(packedRedId);

        List<String> content = Files.readAllLines(packedRes.getLocation());
        assertThat(content, contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
                "<html><head></head><body><a href=\"http://ya.ru/link\"></a></body></html>"));
    }
}