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
        NewRes.Id newResId = storage.createNewResource(url);
        assertTrue(storage.hasResource(newResId));
        assertThat(newResId.getId(), equalTo(urlStr));
    }

    @Test
    public void createHeadingResource() {
        NewRes.Id newResId = storage.createNewResource(url);
        HeadingRes.Id hingResId = storage.createHeadingResource(newResId);
        assertFalse(storage.hasResource(newResId));
        assertTrue(storage.hasResource(hingResId));
        assertThat(hingResId.getId(), equalTo(urlStr));
    }

    @Test
    public void createHeadedResource() {
        NewRes.Id newResId = storage.createNewResource(url);
        HeadingRes.Id hingResId = storage.createHeadingResource(newResId);
        HeadedRes.Id hedResId = storage.createHeadedResource(hingResId, httpInfo);

        assertFalse(storage.hasResource(newResId));
        assertFalse(storage.hasResource(hingResId));
        assertTrue(storage.hasResource(hedResId));
        assertThat(hingResId.getId(), equalTo(urlStr));
    }

    @Test
    public void createDownloadingResource() {
        NewRes.Id newResId = storage.createNewResource(url);
        HeadingRes.Id hingResId = storage.createHeadingResource(newResId);
        HeadedRes.Id hedResId = storage.createHeadedResource(hingResId, httpInfo);
        DownloadingRes.Id dingResId = storage.createDownloadingResource(hedResId);

        assertFalse(storage.hasResource(newResId));
        assertFalse(storage.hasResource(hingResId));
        assertFalse(storage.hasResource(hedResId));
        assertTrue(storage.hasResource(dingResId));
        assertThat(dingResId.getId(), equalTo(urlStr));
    }

    @Test
    public void createDownloadedResource() {
        NewRes.Id newResId = storage.createNewResource(url);
        HeadingRes.Id hingResId = storage.createHeadingResource(newResId);
        HeadedRes.Id hedResId = storage.createHeadedResource(hingResId, httpInfo);
        DownloadingRes.Id dingResId = storage.createDownloadingResource(hedResId);
        DownloadedRes.Id dedResId = storage.createDownloadedResource(dingResId);

        assertFalse(storage.hasResource(newResId));
        assertFalse(storage.hasResource(hingResId));
        assertFalse(storage.hasResource(hedResId));
        assertFalse(storage.hasResource(dingResId));
        assertTrue(storage.hasResource(dedResId));
        assertThat(dedResId.getId(), equalTo(urlStr));
    }

    @Test
    public void createRejectedResource() {
        NewRes.Id newResId = storage.createNewResource(url);
        RejectedRes.Id rejRes = storage.createRejectedRes(newResId);

        assertFalse(storage.hasResource(newResId));
        assertTrue(storage.hasResource(rejRes));
        assertThat(rejRes.getId(), equalTo(urlStr));
    }

    @Test
    public void getResource() {
        NewRes.Id newResId = storage.createNewResource(url);
        Resource<NewRes.Id> res = storage.getResource(newResId);
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
        NewRes.Id newResId = storage.createNewResource(url);
        storage.createHeadingResource(newResId);
        exception.expect(ResourceAlreadyExistsException.class);
        storage.createHeadingResource(newResId);
    }

    @Test
    public void alreadyExistsHeaded() {
        NewRes.Id newResId = storage.createNewResource(url);
        HeadingRes.Id hedResId = storage.createHeadingResource(newResId);
        storage.createHeadedResource(hedResId, httpInfo);
        exception.expect(ResourceAlreadyExistsException.class);
        storage.createHeadedResource(hedResId, httpInfo);
    }

    @Test
    public void alreadyExistsDownloading() {
        HeadedRes.Id hedResId = makeHeadedResId();
        storage.createDownloadingResource(hedResId);
        exception.expect(ResourceAlreadyExistsException.class);
        storage.createDownloadingResource(hedResId);
    }

    @Test
    public void alreadyExistsDownloaded() {
        HeadedRes.Id hedResId = makeHeadedResId();
        DownloadingRes.Id dingResId = storage.createDownloadingResource(hedResId);
        storage.createDownloadedResource(dingResId);
        exception.expect(ResourceAlreadyExistsException.class);
        storage.createDownloadedResource(dingResId);
    }

    private HeadedRes.Id makeHeadedResId() {
        NewRes.Id newResId = storage.createNewResource(url);
        HeadingRes.Id hingResId = storage.createHeadingResource(newResId);
        return storage.createHeadedResource(hingResId, httpInfo);
    }
}