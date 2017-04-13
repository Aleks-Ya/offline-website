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
import ru.yaal.offlinewebsite.impl.resource.DownloadingResImpl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author Aleksey Yablokov
 */
public class SyncInMemoryStorageImplTest {
    private final Storage storage = new SyncInMemoryStorageImpl();
    private final String urlStr = "abc";
    private final SiteUrl url = new SiteUrlImpl(urlStr);

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void createNewResource() {
        NewRes.Id newResourceId = storage.createNewResource(url);
        assertThat(newResourceId.getId(), equalTo(urlStr));
    }

    @Test
    public void createDownloadingResource() {
        DownloadingRes.Id dingResId = makeDownloadingResId();
        assertThat(dingResId.getId(), equalTo(urlStr));
    }

    private DownloadingRes.Id makeDownloadingResId() {
        HeadedRes.Id hedResId = makeHeadedResId();
        return storage.createDownloadingResource(hedResId);
    }

    private HeadedRes.Id makeHeadedResId() {
        NewRes.Id newResId = storage.createNewResource(url);
        HeadingRes.Id hingResId = storage.createHeadingResource(newResId);
        HttpInfoImpl httpInfo = new HttpInfoImpl(200, 1000, 1);
        return storage.createHeadedResource(hingResId, httpInfo);
    }

    @Test
    public void createDownloadedResource() {
        DownloadingResImpl.Id dingResId = makeDownloadingResId();
        DownloadedRes.Id dedResId = storage.createDownloadedResource(dingResId);
        assertThat(dedResId.getId(), equalTo(urlStr));
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
}