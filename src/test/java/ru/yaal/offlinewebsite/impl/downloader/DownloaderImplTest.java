package ru.yaal.offlinewebsite.impl.downloader;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.params.StorageParams;
import ru.yaal.offlinewebsite.api.resource.*;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.system.Network;
import ru.yaal.offlinewebsite.impl.http.HttpInfoImpl;
import ru.yaal.offlinewebsite.impl.params.DownloaderParamsImpl;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;
import ru.yaal.offlinewebsite.impl.params.StorageParamsImpl;
import ru.yaal.offlinewebsite.impl.resource.ResourceComparatorImpl;
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
        String urlStr = "abc";
        SiteUrl siteUrl = new SiteUrlImpl(urlStr);
        StorageParams storageParams = new StorageParamsImpl(new ResourceComparatorImpl());
        Storage storage = new SyncInMemoryStorageImpl(storageParams);
        String content = "how are you";
        BytesNetwork network = new BytesNetwork();
        network.putBytes(siteUrl, content);
        DownloaderParamsImpl params = new DownloaderParamsImpl(storage, network);
        Downloader downloader = new DownloaderImpl(params);
        ResourceId<NewRes> newResId = storage.createNewResource(siteUrl);
        ResourceId<HeadingRes> hingResId = storage.createHeadingResource(newResId);
        HttpInfoImpl httpInfo = new HttpInfoImpl(200, 1000, 1);
        ResourceId<HeadedRes> hedResId = storage.createHeadedResource(hingResId, httpInfo);
        ResourceId<DownloadingRes> downloadingResource = storage.createDownloadingResource(hedResId);
        ResourceId<DownloadedRes> dedResId = downloader.download(downloadingResource);
        assertThat(dedResId.getId(), equalTo(urlStr));
        DownloadedRes dedRes = storage.getResource(dedResId);
        assertThat(IOUtils.toString(dedRes.getContent(), Charset.defaultCharset()), equalTo(content));
    }

}