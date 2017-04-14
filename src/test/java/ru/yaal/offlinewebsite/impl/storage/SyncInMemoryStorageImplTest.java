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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

/**
 * @author Aleksey Yablokov
 */
public class SyncInMemoryStorageImplTest {
    private final Storage storage = new SyncInMemoryStorageImpl();
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
        assertFalse(storage.hasResource(newResId));
        assertTrue(storage.hasResource(hingResId));
        assertThat(hingResId.getId(), equalTo(urlStr));
    }

    @Test
    public void createHeadedResource() {
        ResourceId<NewRes> newResId = storage.createNewResource(url);
        ResourceId<HeadingRes> hingResId = storage.createHeadingResource(newResId);
        ResourceId<HeadedRes> hedResId = storage.createHeadedResource(hingResId, httpInfo);

        assertFalse(storage.hasResource(newResId));
        assertFalse(storage.hasResource(hingResId));
        assertTrue(storage.hasResource(hedResId));
        assertThat(hingResId.getId(), equalTo(urlStr));
    }

    @Test
    public void createDownloadingResource() {
        ResourceId<NewRes> newResId = storage.createNewResource(url);
        ResourceId<HeadingRes> hingResId = storage.createHeadingResource(newResId);
        ResourceId<HeadedRes> hedResId = storage.createHeadedResource(hingResId, httpInfo);
        ResourceId<DownloadingRes> dingResId = storage.createDownloadingResource(hedResId);

        assertFalse(storage.hasResource(newResId));
        assertFalse(storage.hasResource(hingResId));
        assertFalse(storage.hasResource(hedResId));
        assertTrue(storage.hasResource(dingResId));
        assertThat(dingResId.getId(), equalTo(urlStr));
    }

    @Test
    public void createDownloadedResource() {
        ResourceId<NewRes> newResId = storage.createNewResource(url);
        ResourceId<HeadingRes> hingResId = storage.createHeadingResource(newResId);
        ResourceId<HeadedRes> hedResId = storage.createHeadedResource(hingResId, httpInfo);
        ResourceId<DownloadingRes> dingResId = storage.createDownloadingResource(hedResId);
        ResourceId<DownloadedRes> dedResId = storage.createDownloadedResource(dingResId);

        assertFalse(storage.hasResource(newResId));
        assertFalse(storage.hasResource(hingResId));
        assertFalse(storage.hasResource(hedResId));
        assertFalse(storage.hasResource(dingResId));
        assertTrue(storage.hasResource(dedResId));
        assertThat(dedResId.getId(), equalTo(urlStr));
    }

    @Test
    public void createRejectedResource() {
        ResourceId<NewRes> newResId = storage.createNewResource(url);
        ResourceId<RejectedRes> rejRes = storage.createRejectedRes(newResId);

        assertFalse(storage.hasResource(newResId));
        assertTrue(storage.hasResource(rejRes));
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