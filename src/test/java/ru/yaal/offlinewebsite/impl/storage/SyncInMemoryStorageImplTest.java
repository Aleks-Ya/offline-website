package ru.yaal.offlinewebsite.impl.storage;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.*;
import ru.yaal.offlinewebsite.api.storage.ResourceAlreadyExistsException;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.impl.http.HttpInfoImpl;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;
import ru.yaal.offlinewebsite.impl.params.StorageParamsImpl;
import ru.yaal.offlinewebsite.impl.resource.ResourceComparatorImpl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

/**
 * @author Aleksey Yablokov
 */
public class SyncInMemoryStorageImplTest {
    private final Storage storage = new SyncInMemoryStorageImpl(new StorageParamsImpl(new ResourceComparatorImpl()));
    private final String urlStr = "http://google.com";
    private final SiteUrl url = new SiteUrlImpl(urlStr);
    private final HttpInfoImpl httpInfo = new HttpInfoImpl(200, 1000, 1);

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void createNewResource() {
        ResourceId<NewRes> newResId = storage.createNewResource(url);
        assertTrue(storage.hasResource(newResId));
        assertThat(newResId.getId(), equalTo(urlStr));
    }

    @Test
    public void createHeadingResource() {
        ResourceId<NewRes> newResId = storage.createNewResource(url);
        ResourceId<HeadingRes> hingResId = storage.createHeadingResource(newResId);
        assertThat(newResId, equalTo(hingResId));
        assertThat(storage.getResource(hingResId), instanceOf(HeadingRes.class));
        assertThat(hingResId.getId(), equalTo(urlStr));
    }

    @Test
    public void createHeadedResource() {
        ResourceId<NewRes> newResId = storage.createNewResource(url);
        ResourceId<HeadingRes> hingResId = storage.createHeadingResource(newResId);
        ResourceId<HeadedRes> hedResId = storage.createHeadedResource(hingResId, httpInfo);

        assertThat(newResId, equalTo(hedResId));
        assertThat(storage.getResource(hedResId), instanceOf(HeadedRes.class));
        assertThat(hedResId.getId(), equalTo(urlStr));
    }

    @Test
    public void createDownloadingResource() {
        ResourceId<NewRes> newResId = storage.createNewResource(url);
        ResourceId<HeadingRes> hingResId = storage.createHeadingResource(newResId);
        ResourceId<HeadedRes> hedResId = storage.createHeadedResource(hingResId, httpInfo);
        ResourceId<DownloadingRes> dingResId = storage.createDownloadingResource(hedResId);

        assertThat(newResId, equalTo(dingResId));
        assertThat(storage.getResource(dingResId), instanceOf(DownloadingRes.class));
        assertThat(dingResId.getId(), equalTo(urlStr));
    }

    @Test
    public void createDownloadedResource() {
        ResourceId<NewRes> newResId = storage.createNewResource(url);
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
        ResourceId<NewRes> newResId = storage.createNewResource(url);
        ResourceId<RejectedRes> rejRes = storage.createRejectedRes(newResId);

        assertThat(newResId, equalTo(rejRes));
        assertThat(storage.getResource(rejRes), instanceOf(RejectedRes.class));
        assertThat(rejRes.getId(), equalTo(urlStr));
    }

    @Test
    public void getResource() {
        ResourceId<NewRes> newResId = storage.createNewResource(url);
        Resource res = storage.getResource(newResId);
        assertNotNull(res);
        assertThat(res.getId().getId(), equalTo(urlStr));
    }

    @Test
    public void alreadyExistsNew() {
        storage.createNewResource(url);
        exception.expect(ResourceAlreadyExistsException.class);
        storage.createNewResource(url);
    }

    @Test
    public void alreadyExistsHeading() {
        ResourceId<NewRes> newResId = storage.createNewResource(url);
        storage.createHeadingResource(newResId);
        exception.expect(ResourceAlreadyExistsException.class);
        storage.createHeadingResource(newResId);
    }

    @Test
    public void alreadyExistsHeaded() {
        ResourceId<NewRes> newResId = storage.createNewResource(url);
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

    private ResourceId<HeadedRes> makeHeadedResId() {
        ResourceId<NewRes> newResId = storage.createNewResource(url);
        ResourceId<HeadingRes> hingResId = storage.createHeadingResource(newResId);
        return storage.createHeadedResource(hingResId, httpInfo);
    }
}