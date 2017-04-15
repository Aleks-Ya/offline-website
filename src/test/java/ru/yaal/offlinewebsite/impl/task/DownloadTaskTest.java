package ru.yaal.offlinewebsite.impl.task;

import org.junit.Test;
import ru.yaal.offlinewebsite.TestFactory;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.task.Task;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;

/**
 * @author Aleksey Yablokov
 */
public class DownloadTaskTest {
    @Test
    public void call() throws Exception {
        SiteUrlImpl rootSiteUrl = new SiteUrlImpl("http://ya.ru");
        TestFactory factory = new TestFactory(rootSiteUrl);
        ResourceId<HeadingRes> headingRes = factory.createHeadingRes(rootSiteUrl, TestFactory.httpInfoDefault);
        String html = "<html></html>";
        factory.getNetwork().putBytes(rootSiteUrl, html);
        factory.getNetwork().putHttpInfo(rootSiteUrl, TestFactory.httpInfoDefault);
        Task task = factory.createTask(rootSiteUrl, headingRes, true, 1000000);

        task.call();
    }


}