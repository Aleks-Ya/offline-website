package ru.yaal.offlinewebsite.impl.task;

import org.junit.Test;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.http.HeadRequest;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.*;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.api.resource.NewRes;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.system.Network;
import ru.yaal.offlinewebsite.api.task.Task;
import ru.yaal.offlinewebsite.impl.downloader.DownloaderImpl;
import ru.yaal.offlinewebsite.impl.http.HeadRequestImpl;
import ru.yaal.offlinewebsite.impl.http.HttpInfoImpl;
import ru.yaal.offlinewebsite.impl.params.*;
import ru.yaal.offlinewebsite.impl.parser.ParserImpl;
import ru.yaal.offlinewebsite.impl.storage.SyncInMemoryStorageImpl;
import ru.yaal.offlinewebsite.impl.system.BytesNetwork;

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