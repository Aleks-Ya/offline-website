package ru.yaal.offlinewebsite.impl.task;

import org.junit.Test;
import ru.yaal.offlinewebsite.TestFactory;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.task.Task;
import ru.yaal.offlinewebsite.impl.params.PageUrlImpl;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

/**
 * @author Aleksey Yablokov
 */
public class DownloadTaskTest {
    @Test
    public void call() throws Exception {
        PageUrlImpl rootPageUrl = new PageUrlImpl("http://ya.ru");
        TestFactory factory = new TestFactory(rootPageUrl);
        ResourceId<HeadingRes> headingRes = factory.createHeadingRes(rootPageUrl, TestFactory.httpInfoDefault);
        String html = "<html></html>";
        factory.getNetwork().putBytes(rootPageUrl, html);
        factory.getNetwork().putHttpInfo(rootPageUrl, TestFactory.httpInfoDefault);
        Task task = factory.createTask(rootPageUrl, headingRes);

        task.call();

        assertThat(factory.getStorage().getParsedResourceIds(), hasSize(1));
    }
}