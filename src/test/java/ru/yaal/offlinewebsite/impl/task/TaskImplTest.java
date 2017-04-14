package ru.yaal.offlinewebsite.impl.task;

import org.junit.Test;
import ru.yaal.offlinewebsite.api.task.Task;

import static ru.yaal.offlinewebsite.impl.TestHelper.makeTask;

/**
 * @author Aleksey Yablokov
 */
public class TaskImplTest {
    @Test
    public void call() throws Exception {
        String rootUrl = "http://ya.ru";
        String html = "<html></html>";
        int responseCode = 200;
        int contentLength = 100_000;
        int lastModified = 2000000;
        boolean onlySameDomain = true;
        int maxSize = 1000000;

        Task task = makeTask(rootUrl, html, responseCode, contentLength, lastModified, onlySameDomain, maxSize);
        task.call();
    }


}