package ru.yaal.offlinewebsite.impl.packager;

import org.hamcrest.Matchers;
import org.junit.Test;
import ru.yaal.offlinewebsite.TestFactory;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.RootResUrl;
import ru.yaal.offlinewebsite.api.resource.NewRes;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.impl.params.ResUrlImpl;

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
        RootResUrl rootResUrl = new ResUrlImpl(rootUrlStr);

        TestFactory factory = new TestFactory(rootResUrl);
        String rootHtml = "<html><body><a href='nested_page.html'>Janino</a></body></html>";
        ResourceId<PackagingRes> rootPackagingResId = factory.createPackagingRes(rootResUrl, rootHtml, TestFactory.httpInfoDefault);

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
}