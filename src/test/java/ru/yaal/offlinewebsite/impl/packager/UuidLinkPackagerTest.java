package ru.yaal.offlinewebsite.impl.packager;

import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;
import ru.yaal.offlinewebsite.TestFactory;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.PageUrl;
import ru.yaal.offlinewebsite.api.params.RootPageUrl;
import ru.yaal.offlinewebsite.api.resource.NewRes;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.task.Task;
import ru.yaal.offlinewebsite.impl.params.PackageTaskParamsImpl;
import ru.yaal.offlinewebsite.impl.params.PageUrlImpl;
import ru.yaal.offlinewebsite.impl.task.PackageTask;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

/**
 * @author Aleksey Yablokov
 */
public class UuidLinkPackagerTest {

    @Test
    public void pack() throws IOException {
        String rootUrlStr = "http://ya.ru/site";
        RootPageUrl rootPageUrl = new PageUrlImpl(rootUrlStr);

        TestFactory factory = new TestFactory(rootPageUrl);
        String rootHtml = "<html><body><a href='nested_page.html'>Janino</a></body></html>";
        ResourceId<PackagingRes> rootPackagingResId = factory.createPackagingRes(rootPageUrl, rootHtml, TestFactory.httpInfoDefault);

        List<ResourceId<NewRes>> newResIds = factory.getStorage().getNewResourceIds();
        assertThat(newResIds, Matchers.iterableWithSize(1));
        ResourceId<NewRes> nestedNewResId = newResIds.get(0);

        ResourceId<PackagingRes> nestedParsingResId = factory.createParsedRes(nestedNewResId,
                "<html><body><h1>Nested</h1></body></html>", TestFactory.httpInfoDefault);

        Packager packager = factory.getUuidLinkPackager();
        ResourceId<PackagedRes> rootPackagedResId = packager.pack(rootPackagingResId);
        ResourceId<PackagedRes> nestedPackagedResId = packager.pack(nestedParsingResId);

        PackagedRes rootPackagedRes = factory.getStorage().getResource(rootPackagedResId);
        PackagedRes nestedPackagedRes = factory.getStorage().getResource(nestedPackagedResId);

        String rootOutput = Files.readAllLines(rootPackagedRes.getLocation()).stream().collect(Collectors.joining("\n"));
        String nestedOutput = Files.readAllLines(nestedPackagedRes.getLocation()).stream().collect(Collectors.joining("\n"));

        assertThat(rootOutput, containsString(nestedPackagedRes.getLocation().toString()));
        assertThat(nestedOutput, containsString("<h1>Nested</h1>"));
    }

    @Test
    @Ignore("fix me")
    public void urlsDifferOnlyFragments() throws Exception {
        String rootUrlStr = "http://ya.ru/index.html";
        RootPageUrl rootPageUrl = new PageUrlImpl(rootUrlStr);
        TestFactory factory = new TestFactory(rootPageUrl);

        PageUrl pageUrl1 = new PageUrlImpl("http://ya.ru/contacts.html#phone");
        PageUrl pageUrl2 = new PageUrlImpl("http://ya.ru/contacts.html#address");

        String html = "<html><body>345-45-67</body></html>";

        ResourceId<PackagingRes> packagingResId1 = factory.createPackagingRes(pageUrl1, html, TestFactory.httpInfoDefault);
        ResourceId<PackagingRes> packagingResId2 = factory.createPackagingRes(pageUrl2, html, TestFactory.httpInfoDefault);

        Task task1 = new PackageTask(new PackageTaskParamsImpl(
                factory.getStorage(), factory.getAllPackagers(), packagingResId1));
        Task task2 = new PackageTask(new PackageTaskParamsImpl(
                factory.getStorage(), factory.getAllPackagers(), packagingResId2));

        task1.call();
        task2.call();
    }
}