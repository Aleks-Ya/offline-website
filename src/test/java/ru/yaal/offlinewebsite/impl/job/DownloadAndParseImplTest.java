package ru.yaal.offlinewebsite.impl.job;

import org.junit.Test;
import ru.yaal.offlinewebsite.TestFactory;
import ru.yaal.offlinewebsite.api.job.Job;
import ru.yaal.offlinewebsite.api.params.DownloadJobParams;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.params.ThreadPoolParams;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;
import ru.yaal.offlinewebsite.impl.params.DownloadJobParamsImpl;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;
import ru.yaal.offlinewebsite.impl.params.ThreadPoolParamsImpl;
import ru.yaal.offlinewebsite.impl.thread.ThreadPoolImpl;

/**
 * @author Aleksey Yablokov
 */
public class DownloadAndParseImplTest {
    @Test
    public void process() {
        String rootSiteStr = "http://ya.ru";
        SiteUrl siteUrl = new SiteUrlImpl(rootSiteStr);
        TestFactory factory = new TestFactory(siteUrl);

        ThreadPoolParams threadPoolParams = new ThreadPoolParamsImpl(3);
        ThreadPool threadPool = new ThreadPoolImpl(threadPoolParams);

        DownloadJobParams downloadJobParams = new DownloadJobParamsImpl(siteUrl, factory.getDownloader(),
                factory.getStorage(), threadPool, factory.getHeadRequest(), factory.getParser());

        Job job = new DownloadJob(downloadJobParams);
        job.process();
    }

}