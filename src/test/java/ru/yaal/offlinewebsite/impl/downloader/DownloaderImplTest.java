package ru.yaal.offlinewebsite.impl.downloader;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.params.ThreadPoolParams;
import ru.yaal.offlinewebsite.api.resource.DownloadedResource;
import ru.yaal.offlinewebsite.api.resource.NewResource;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.system.Network;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;
import ru.yaal.offlinewebsite.impl.params.DownloaderParamsImpl;
import ru.yaal.offlinewebsite.impl.params.ThreadPoolParamsImpl;
import ru.yaal.offlinewebsite.impl.resource.BytesDownloadedResource;
import ru.yaal.offlinewebsite.impl.resource.DownloadingResourceImpl;
import ru.yaal.offlinewebsite.impl.storage.SyncInMemoryStorageImpl;
import ru.yaal.offlinewebsite.impl.system.BytesNetwork;
import ru.yaal.offlinewebsite.impl.thread.ThreadPoolImpl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Aleksey Yablokov
 */
public class DownloaderImplTest {
    @Test
    public void download() throws ExecutionException, InterruptedException, IOException {
        Storage storage = new SyncInMemoryStorageImpl();
        String content = "how are you";
        Network network = new BytesNetwork(content.getBytes());
        ThreadPoolParams threadPoolParams = new ThreadPoolParamsImpl(1);
        ThreadPool threadPool = new ThreadPoolImpl(threadPoolParams);
        DownloaderParamsImpl params = new DownloaderParamsImpl(storage, network, threadPool);
        Downloader downloader = new DownloaderImpl(params);
        String urlStr = "abc";
        SiteUrl url = new SiteUrlImpl(urlStr);
        NewResource.Id newResource = storage.createNewResource(url);
        DownloadingResourceImpl.Id downloadingResource = storage.createDownloadingResource(newResource);
        Future<BytesDownloadedResource.Id> future = downloader.download(downloadingResource);
        BytesDownloadedResource.Id id = future.get();
        DownloadedResource<BytesDownloadedResource.Id> resource = storage.getResource(id);

        assertThat(resource.getId().getId(), equalTo(urlStr));
        assertThat(IOUtils.toString(resource.getContent(), Charset.defaultCharset()), equalTo(content));
    }

}