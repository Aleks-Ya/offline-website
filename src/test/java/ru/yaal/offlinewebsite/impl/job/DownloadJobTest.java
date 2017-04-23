package ru.yaal.offlinewebsite.impl.job;

import org.junit.Test;
import ru.yaal.offlinewebsite.TestFactory;
import ru.yaal.offlinewebsite.api.job.Job;
import ru.yaal.offlinewebsite.api.params.DownloadJobParams;
import ru.yaal.offlinewebsite.api.link.RootLink;
import ru.yaal.offlinewebsite.api.params.ThreadPoolParams;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;
import ru.yaal.offlinewebsite.impl.params.DownloadJobParamsImpl;
import ru.yaal.offlinewebsite.impl.link.LinkImpl;
import ru.yaal.offlinewebsite.impl.params.ThreadPoolParamsImpl;
import ru.yaal.offlinewebsite.impl.thread.ThreadPoolImpl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Aleksey Yablokov
 */
public class DownloadJobTest {
    @Test
    public void process() {
        String rootSiteStr = "http://ya.ru";
        RootLink rootLink = new LinkImpl(rootSiteStr);
        TestFactory factory = new TestFactory(rootLink);

        ThreadPoolParams threadPoolParams = new ThreadPoolParamsImpl(3);
        ThreadPool threadPool = new ThreadPoolImpl(threadPoolParams);

        DownloadJobParams downloadJobParams = new DownloadJobParamsImpl(rootLink, factory.getDownloader(),
                factory.getStorage(), threadPool, factory.getHeadRetriever(), factory.getAllParsers(),
                factory.getHeadingFilters(), factory.getHeadedFilters());

        Job job = new DownloadJob(downloadJobParams);
        job.process();

        assertThat(threadPool.getCompletedTaskCount(), equalTo(1L));
        assertThat(factory.getStorage().getParsedResourceIds(), emptyIterable());
    }

}