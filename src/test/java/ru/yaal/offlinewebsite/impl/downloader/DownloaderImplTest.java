package ru.yaal.offlinewebsite.impl.downloader;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import ru.yaal.offlinewebsite.TestFactory;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.params.RootResUrl;
import ru.yaal.offlinewebsite.api.resource.DownloadedRes;
import ru.yaal.offlinewebsite.api.resource.DownloadingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.impl.params.ResUrlImpl;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Aleksey Yablokov
 */
public class DownloaderImplTest {
    @Test
    public void download() throws IOException {
        String rootUrlStr = "http://ya.ru/info/index.html";
        RootResUrl rootResUrl = new ResUrlImpl(rootUrlStr);
        TestFactory factory = new TestFactory(rootResUrl);
        String html = "<html><body><a href='http://ya.ru/link'/></body></html>";

        ResourceId<DownloadingRes> dingResId = factory.createDownloadingRes(rootResUrl, html, TestFactory.httpInfoDefault);

        Downloader downloader = factory.getDownloader();
        ResourceId<DownloadedRes> dedResId = downloader.download(dingResId);

        assertThat(dedResId.getId(), equalTo(rootUrlStr));
        DownloadedRes dedRes = factory.getStorage().getResource(dedResId);
        assertThat(IOUtils.toString(dedRes.getContent(), Charset.defaultCharset()), equalTo(html));
    }
}