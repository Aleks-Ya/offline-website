package ru.yaal.offlinewebsite.impl.downloader;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.DownloadedRes;
import ru.yaal.offlinewebsite.api.resource.HeadedRes;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.api.resource.NewRes;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.system.Network;
import ru.yaal.offlinewebsite.impl.http.HttpInfoImpl;
import ru.yaal.offlinewebsite.impl.params.DownloaderParamsImpl;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;
import ru.yaal.offlinewebsite.impl.resource.DownloadingResImpl;
import ru.yaal.offlinewebsite.impl.storage.SyncInMemoryStorageImpl;
import ru.yaal.offlinewebsite.impl.system.BytesNetwork;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;

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
        DownloaderParamsImpl params = new DownloaderParamsImpl(storage, network);
        Downloader downloader = new DownloaderImpl(params);
        String urlStr = "abc";
        SiteUrl url = new SiteUrlImpl(urlStr);
        NewRes.Id newResId = storage.createNewResource(url);
        HeadingRes.Id hingResId = storage.createHeadingResource(newResId);
        HttpInfoImpl httpInfo = new HttpInfoImpl(200, 1000, 1);
        HeadedRes.Id hedResId = storage.createHeadedResource(hingResId, httpInfo);
        DownloadingResImpl.Id downloadingResource = storage.createDownloadingResource(hedResId);
        DownloadedRes.Id dedResId = downloader.download(downloadingResource);
        assertThat(dedResId.getId(), equalTo(urlStr));
        DownloadedRes<DownloadedRes.Id> dedRes = storage.getResource(dedResId);
        assertThat(IOUtils.toString(dedRes.getContent(), Charset.defaultCharset()), equalTo(content));
    }

}