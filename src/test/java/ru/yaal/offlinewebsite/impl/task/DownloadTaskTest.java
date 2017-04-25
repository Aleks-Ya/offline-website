package ru.yaal.offlinewebsite.impl.task;

import org.junit.Test;
import ru.yaal.offlinewebsite.TestFactory;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.task.Task;
import ru.yaal.offlinewebsite.impl.params.ResUrlImpl;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

/**
 * @author Aleksey Yablokov
 */
public class DownloadTaskTest {
    @Test
    public void call() throws Exception {
        ResUrlImpl rootResUrl = new ResUrlImpl("http://ya.ru");
        TestFactory factory = new TestFactory(rootResUrl);
        ResourceId<HeadingRes> headingRes = factory.createHeadingRes(rootResUrl, TestFactory.httpInfoDefault);
        String html = "<html></html>";
        factory.getNetwork().putBytes(rootResUrl, html);
        factory.getNetwork().putHttpInfo(rootResUrl, TestFactory.httpInfoDefault);
        Task task = factory.createTask(rootResUrl, headingRes);

        task.call();

        assertThat(factory.getStorage().getParsedResourceIds(), hasSize(1));
    }
}