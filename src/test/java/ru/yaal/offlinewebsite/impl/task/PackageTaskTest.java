package ru.yaal.offlinewebsite.impl.task;

import org.junit.Test;
import ru.yaal.offlinewebsite.TestFactory;
import ru.yaal.offlinewebsite.api.params.PackageTaskParams;
import ru.yaal.offlinewebsite.api.params.PageUrl;
import ru.yaal.offlinewebsite.api.params.RootPageUrl;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.task.Task;
import ru.yaal.offlinewebsite.impl.params.PackageTaskParamsImpl;
import ru.yaal.offlinewebsite.impl.params.PageUrlImpl;
import ru.yaal.offlinewebsite.impl.resource.ResIdImpl;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Aleksey Yablokov
 */
public class PackageTaskTest {
    @Test
    public void call() throws Exception {
        RootPageUrl rootPageUrl = new PageUrlImpl("http://google.com");
        TestFactory factory = new TestFactory(rootPageUrl);
        String pageUrlStr = "http://google.com/data.html";
        PageUrl pageUrl = new PageUrlImpl(pageUrlStr);
        String html = "<html></html>";
        ResourceId<PackagingRes> packagingResId = factory.createPackagingRes(pageUrl, html, TestFactory.httpInfoDefault);
        Storage storage = factory.getStorage();
        PackageTaskParams params = new PackageTaskParamsImpl(storage, factory.getAllPackagers(),
                new ResIdImpl<>(packagingResId.getId()));
        Task<PackagedRes> task = new PackageTask(params);
        ResourceId<PackagedRes> packagedResId = task.call();

        PackagedRes packagedRes = storage.getResource(packagedResId);
        assertThat(packagedRes.getUrl().getUrl(), equalTo(pageUrlStr));

    }

}