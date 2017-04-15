package ru.yaal.offlinewebsite.impl.packager;

import org.htmlcleaner.TagNode;
import org.junit.Test;
import ru.yaal.offlinewebsite.TestFactory;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;

import java.io.IOException;

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
    }
}