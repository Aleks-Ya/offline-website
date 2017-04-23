package ru.yaal.offlinewebsite.api.packager;

import org.junit.Test;
import ru.yaal.offlinewebsite.api.params.Link;
import ru.yaal.offlinewebsite.impl.params.LinkImpl;

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
        Link link = new LinkImpl("http://ya.ru:80/russia/spb.html?query=abc");
        Path path = resolver.internetUrlToOfflinePath(outletDir, link);

        Path expPath = Paths.get("c:/tmp/outlet/ya.ru-80-http/russia/spb.html/query%3Dabc");
        assertThat(path, equalTo(expPath));
    }

    @Test
    public void noQuery() {
        Path outletDir = Paths.get("c:/tmp/outlet");
        Link link = new LinkImpl("http://ya.ru:80/russia/spb.html");
        Path path = resolver.internetUrlToOfflinePath(outletDir, link);

        Path expPath = Paths.get("c:/tmp/outlet/ya.ru-80-http/russia/spb.html");
        assertThat(path, equalTo(expPath));
    }


    @Test
    public void noPath() {
        Path outletDir = Paths.get("c:/tmp/outlet");
        Link link = new LinkImpl("http://ya.ru:80");
        Path path = resolver.internetUrlToOfflinePath(outletDir, link);

        Path expPath = Paths.get("c:/tmp/outlet/ya.ru-80-http");
        assertThat(path, equalTo(expPath));
    }

    @Test
    public void noPort() {
        Path outletDir = Paths.get("c:/tmp/outlet");
        Link link = new LinkImpl("http://ya.ru/russia");
        Path path = resolver.internetUrlToOfflinePath(outletDir, link);

        Path expPath = Paths.get("c:/tmp/outlet/ya.ru-http/russia");
        assertThat(path, equalTo(expPath));
    }

    @Test
    public void fileProtocol() {
        Path outletDir = Paths.get("c:/tmp/outlet");
        Link link = new LinkImpl("file:/C:/yaal/nested_page.html");
        Path path = resolver.internetUrlToOfflinePath(outletDir, link);

        Path expPath = Paths.get("c:/tmp/outlet/file/C/yaal/nested_page.html");
        assertThat(path, equalTo(expPath));
    }

}