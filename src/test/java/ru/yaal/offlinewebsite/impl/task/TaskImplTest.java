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

/**
 * @author Aleksey Yablokov
 */
public class TaskImplTest {
    @Test
    public void call() throws Exception {
        SiteUrl rootSiteUrl = new SiteUrlImpl("http://ya.ru");
        Storage storage = new SyncInMemoryStorageImpl();
        NewRes.Id newResId = storage.createNewResource(rootSiteUrl);
        HeadingRes.Id hingResId = storage.createHeadingResource(newResId);
        HttpInfo httpInfo = new HttpInfoImpl(200, 100_000, 2000000);
        Network network = new BytesNetwork("<html></html>".getBytes(), httpInfo);
        DownloaderParams params = new DownloaderParamsImpl(storage, network);
        Downloader downloader = new DownloaderImpl(params);
        HeadRequestParams headRequestParams = new HeadRequestParamsImpl(storage, network);
        HeadRequest headRequest = new HeadRequestImpl(headRequestParams);
        ParserParams parserParams = new ParserParamsImpl(storage);
        Parser parser = new ParserImpl(parserParams);
        TaskParams taskParams = new TaskParamsImpl(rootSiteUrl, hingResId, downloader, storage,
                true, headRequest, 1000000, parser);
        Task task = new TaskImpl(taskParams);
        task.call();
    }

}