package ru.yaal.offlinewebsite.impl.task;

import org.htmlcleaner.TagNode;
import org.junit.Test;
import ru.yaal.offlinewebsite.TestFactory;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.PackageTaskParams;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.task.Task;
import ru.yaal.offlinewebsite.impl.params.PackageTaskParamsImpl;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Aleksey Yablokov
 */
public class PackageTaskTest {
    @Test
    public void call() throws Exception {
        SiteUrl rootSiteUrl = new SiteUrlImpl("http://google.com");
        TestFactory factory = new TestFactory(rootSiteUrl);
        String siteUrlStr = "http://google.com/data.html";
        SiteUrl siteUrl = new SiteUrlImpl(siteUrlStr);
        String html = "<html></html>";
        ResourceId<PackagingRes<TagNode>> packagingResId = factory.createPackagingRes(siteUrl, html, TestFactory.httpInfoDefault);
        Packager<TagNode> packager = factory.getPackager();
        Storage storage = factory.getStorage();
        PackageTaskParams<TagNode> params = new PackageTaskParamsImpl<>(
                storage, packager, packagingResId);
        Task<PackagedRes> task = new PackageTask(params);
        ResourceId<PackagedRes> packagedResId = task.call();

        PackagedRes packagedRes = storage.getResource(packagedResId);
        assertThat(packagedRes.getUrl().getUrl(), equalTo(siteUrlStr));

    }

}