package ru.yaal.offlinewebsite.impl.job;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;
import ru.yaal.offlinewebsite.TestFactory;
import ru.yaal.offlinewebsite.api.job.Job;
import ru.yaal.offlinewebsite.api.params.DownloadJobParams;
import ru.yaal.offlinewebsite.api.params.RootResUrl;
import ru.yaal.offlinewebsite.api.params.ThreadPoolParams;
import ru.yaal.offlinewebsite.api.resource.ParsedRes;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;
import ru.yaal.offlinewebsite.impl.http.HttpInfoImpl;
import ru.yaal.offlinewebsite.impl.params.DownloadJobParamsImpl;
import ru.yaal.offlinewebsite.impl.params.ResUrlImpl;
import ru.yaal.offlinewebsite.impl.params.ThreadPoolParamsImpl;
import ru.yaal.offlinewebsite.impl.system.BytesNetwork;
import ru.yaal.offlinewebsite.impl.thread.ThreadPoolImpl;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

/**
 * @author Aleksey Yablokov
 */
public class SequenceDownloadJobTest {
    @Test
    public void downloadRootSite() throws IOException {
        String rootSiteStr = "http://ya.ru";
        RootResUrl rootResUrl = new ResUrlImpl(rootSiteStr);
        TestFactory factory = new TestFactory(rootResUrl);
        BytesNetwork network = factory.getNetwork();
        String expContent = "the root page";
        network.putBytes(rootResUrl, expContent);
        network.putHttpInfo(rootResUrl, new HttpInfoImpl(200, expContent.length(),
                System.currentTimeMillis(), "text/plain"));

        Storage storage = factory.getStorage();
        DownloadJobParams downloadJobParams = new DownloadJobParamsImpl(rootResUrl, factory.getDownloader(),
                storage, null, factory.getHeadRetriever(), factory.getAllParsers(),
                factory.getHeadingFilters(), factory.getHeadedFilters());

        Job job = new SequenceDownloadJob(downloadJobParams);
        job.process();

        assertThat(storage.getParsedResourceIds(), hasSize(1));
        String actContent = IOUtils.toString(storage.getResource(storage.getParsedResourceIds().get(0)).getParsedContent(), Charset.defaultCharset());
        assertThat(actContent, equalTo(expContent));
    }

    @Test
    public void downloadRootSiteWithNested() throws IOException {
        String rootSiteStr = "http://ya.ru";
        RootResUrl rootResUrl = new ResUrlImpl(rootSiteStr);

        String nestedSiteStr = "http://ya.ru/index.html";
        RootResUrl nestedResUrl = new ResUrlImpl(nestedSiteStr);

        TestFactory factory = new TestFactory(rootResUrl);
        BytesNetwork network = factory.getNetwork();

        String expRootContent = String.format("the root page <a href='%s'>Index</a>", nestedSiteStr) ;
        network.putBytes(rootResUrl, expRootContent);
        network.putHttpInfo(rootResUrl, new HttpInfoImpl(200, expRootContent.length(),
                System.currentTimeMillis(), "text/html"));

        String expNestedContent = "the nested page";
        network.putBytes(nestedResUrl, expNestedContent);
        network.putHttpInfo(nestedResUrl, new HttpInfoImpl(200, expNestedContent.length(),
                System.currentTimeMillis(), "text/plain"));

        Storage storage = factory.getStorage();
        DownloadJobParams downloadJobParams = new DownloadJobParamsImpl(rootResUrl, factory.getDownloader(),
                storage, null, factory.getHeadRetriever(), factory.getAllParsers(),
                factory.getHeadingFilters(), factory.getHeadedFilters());

        Job job = new SequenceDownloadJob(downloadJobParams);
        job.process();

        assertThat(storage.getParsedResourceIds(), hasSize(2));

        ParsedRes rootRes = storage.getResource(storage.getParsedResourceIds().get(0));
        String actRootContent = IOUtils.toString(rootRes.getParsedContent(), Charset.defaultCharset());
        assertThat(actRootContent, equalTo(expRootContent.replace(nestedSiteStr, rootRes.getLinks().get(0).getUUID())));

        ParsedRes nestedRes = storage.getResource(storage.getParsedResourceIds().get(1));
        String actNestedContent = IOUtils.toString(nestedRes.getParsedContent(), Charset.defaultCharset());
        assertThat(actNestedContent, equalTo(expNestedContent));
    }

}