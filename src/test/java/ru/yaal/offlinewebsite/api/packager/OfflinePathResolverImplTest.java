package ru.yaal.offlinewebsite.api.packager;

import org.junit.Test;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Aleksey Yablokov
 */
public class OfflinePathResolverImplTest {
    private final OfflinePathResolver resolver = new OfflinePathResolverImpl();

    @Test
    public void internetUrlToOfflinePath() {
        Path outletDir = Paths.get("c:/tmp/outlet");
        SiteUrl siteUrl = new SiteUrlImpl("http://ya.ru:80/russia/spb.html?query=abc");
        Path path = resolver.internetUrlToOfflinePath(outletDir, siteUrl);

        Path expPath = Paths.get("c:/tmp/outlet/ya.ru-80-http/russia/spb.html/query%3Dabc");
        assertThat(path, equalTo(expPath));
    }

}