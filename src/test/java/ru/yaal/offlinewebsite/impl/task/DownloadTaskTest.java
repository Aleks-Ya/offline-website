package ru.yaal.offlinewebsite.impl.task;

import org.junit.Test;
import ru.yaal.offlinewebsite.TestFactory;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.task.Task;
import ru.yaal.offlinewebsite.impl.link.LinkImpl;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

/**
 * @author Aleksey Yablokov
 */
public class DownloadTaskTest {
    @Test
    public void call() throws Exception {
        LinkImpl rootPageUrl = new LinkImpl("http://ya.ru");
        TestFactory factory = new TestFactory(rootPageUrl);
        ResourceId<HeadingRes> headingRes = factory.createHeadingRes(rootPageUrl, TestFactory.httpInfoDefault);
        String html = "<html></html>";
        factory.getNetwork().putBytes(rootPageUrl, html);
        factory.getNetwork().putHttpInfo(rootPageUrl, TestFactory.httpInfoDefault);
        Task task = factory.createDownloadTask(rootPageUrl, headingRes);

        task.call();

        assertThat(factory.getStorage().getParsedResourceIds(), hasSize(1));
    }
}