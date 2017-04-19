package ru.yaal.offlinewebsite.impl.storage;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.yaal.offlinewebsite.TestFactory;
import ru.yaal.offlinewebsite.api.params.RootSiteUrl;
import ru.yaal.offlinewebsite.api.resource.DownloadedRes;
import ru.yaal.offlinewebsite.api.resource.DownloadingRes;
import ru.yaal.offlinewebsite.api.resource.HeadedRes;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.api.resource.NewRes;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.RejectedRes;
import ru.yaal.offlinewebsite.api.resource.Resource;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.ResourceAlreadyExistsException;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.impl.http.HttpInfoImpl;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;
import ru.yaal.offlinewebsite.impl.params.StorageParamsImpl;
import ru.yaal.offlinewebsite.impl.resource.ResourceComparatorImpl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Aleksey Yablokov
 */
public class SyncInMemoryStorageImplTest {
    private final Storage storage = new SyncInMemoryStorageImpl(new StorageParamsImpl(new ResourceComparatorImpl()));
    private final String urlStr = "http://google.com";
    private final RootSiteUrl rootSiteUrl = new SiteUrlImpl(urlStr);
    private final HttpInfoImpl httpInfo
            = new HttpInfoImpl(200, 1000, 1, "text/html");

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void createNewResource() {
        ResourceId<NewRes> newResId = storage.createNewResource(rootSiteUrl);
        assertTrue(storage.hasResource(newResId));
        assertThat(newResId.getId(), equalTo(urlStr));
    }

    @Test
    public void createHeadingResource() {
        ResourceId<NewRes> newResId = storage.createNewResource(rootSiteUrl);
        ResourceId<HeadingRes> hingResId = storage.createHeadingResource(newResId);
        assertThat(newResId, equalTo(hingResId));
        assertThat(storage.getResource(hingResId), instanceOf(HeadingRes.class));
        assertThat(hingResId.getId(), equalTo(urlStr));
    }

    @Test
    public void createHeadedResource() {
        ResourceId<NewRes> newResId = storage.createNewResource(rootSiteUrl);
        ResourceId<HeadingRes> hingResId = storage.createHeadingResource(newResId);
        ResourceId<HeadedRes> hedResId = storage.createHeadedResource(hingResId, httpInfo);

        assertThat(newResId, equalTo(hedResId));
        assertThat(storage.getResource(hedResId), instanceOf(HeadedRes.class));
        assertThat(hedResId.getId(), equalTo(urlStr));
    }

    @Test
    public void createDownloadingResource() {
        ResourceId<NewRes> newResId = storage.createNewResource(rootSiteUrl);
        ResourceId<HeadingRes> hingResId = storage.createHeadingResource(newResId);
        ResourceId<HeadedRes> hedResId = storage.createHeadedResource(hingResId, httpInfo);
        ResourceId<DownloadingRes> dingResId = storage.createDownloadingResource(hedResId);

        assertThat(newResId, equalTo(dingResId));
        assertThat(storage.getResource(dingResId), instanceOf(DownloadingRes.class));
        assertThat(dingResId.getId(), equalTo(urlStr));
    }

    @Test
    public void createDownloadedResource() {
        ResourceId<NewRes> newResId = storage.createNewResource(rootSiteUrl);
        ResourceId<HeadingRes> hingResId = storage.createHeadingResource(newResId);
        ResourceId<HeadedRes> hedResId = storage.createHeadedResource(hingResId, httpInfo);
        ResourceId<DownloadingRes> dingResId = storage.createDownloadingResource(hedResId);
        ResourceId<DownloadedRes> dedResId = storage.createDownloadedResource(dingResId);

        assertThat(newResId, equalTo(dedResId));
        assertThat(storage.getResource(dedResId), instanceOf(DownloadedRes.class));
        assertThat(dedResId.getId(), equalTo(urlStr));
    }

    @Test
    public void createRejectedResource() {
        ResourceId<NewRes> newResId = storage.createNewResource(rootSiteUrl);
        ResourceId<RejectedRes> rejRes = storage.createRejectedRes(newResId);

        assertThat(newResId, equalTo(rejRes));
        assertThat(storage.getResource(rejRes), instanceOf(RejectedRes.class));
        assertThat(rejRes.getId(), equalTo(urlStr));
    }

    @Test
    public void getResource() {
        ResourceId<NewRes> newResId = storage.createNewResource(rootSiteUrl);
        Resource res = storage.getResource(newResId);
        assertNotNull(res);
        assertThat(res.getId().getId(), equalTo(urlStr));
    }

    @Test
    public void alreadyExistsNew() {
        storage.createNewResource(rootSiteUrl);
        exception.expect(ResourceAlreadyExistsException.class);
        storage.createNewResource(rootSiteUrl);
    }

    @Test
    public void alreadyExistsHeading() {
        ResourceId<NewRes> newResId = storage.createNewResource(rootSiteUrl);
        storage.createHeadingResource(newResId);
        exception.expect(ResourceAlreadyExistsException.class);
        storage.createHeadingResource(newResId);
    }

    @Test
    public void alreadyExistsHeaded() {
        ResourceId<NewRes> newResId = storage.createNewResource(rootSiteUrl);
        ResourceId<HeadingRes> hedResId = storage.createHeadingResource(newResId);
        storage.createHeadedResource(hedResId, httpInfo);
        exception.expect(ResourceAlreadyExistsException.class);
        storage.createHeadedResource(hedResId, httpInfo);
    }

    @Test
    public void alreadyExistsDownloading() {
        ResourceId<HeadedRes> hedResId = makeHeadedResId();
        storage.createDownloadingResource(hedResId);
        exception.expect(ResourceAlreadyExistsException.class);
        storage.createDownloadingResource(hedResId);
    }

    @Test
    public void alreadyExistsDownloaded() {
        ResourceId<HeadedRes> hedResId = makeHeadedResId();
        ResourceId<DownloadingRes> dingResId = storage.createDownloadingResource(hedResId);
        storage.createDownloadedResource(dingResId);
        exception.expect(ResourceAlreadyExistsException.class);
        storage.createDownloadedResource(dingResId);
    }

    @Test
    public void getPackagingResourceIds() {
        TestFactory factory = new TestFactory(rootSiteUrl);
        ResourceId<PackagingRes> packagingResId =
                factory.createPackagingRes(rootSiteUrl, "<html></html>", TestFactory.httpInfoDefault);
        assertThat(factory.getStorage().getResource(packagingResId), Matchers.instanceOf(PackagingRes.class));
    }

    private ResourceId<HeadedRes> makeHeadedResId() {
        ResourceId<NewRes> newResId = storage.createNewResource(rootSiteUrl);
        ResourceId<HeadingRes> hingResId = storage.createHeadingResource(newResId);
        return storage.createHeadedResource(hingResId, httpInfo);
    }
}