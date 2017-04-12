package ru.yaal.offlinewebsite.impl.storage;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.DownloadingResource;
import ru.yaal.offlinewebsite.api.resource.NewResource;
import ru.yaal.offlinewebsite.api.resource.Resource;
import ru.yaal.offlinewebsite.api.storage.ResourceAlreadyExistsException;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;
import ru.yaal.offlinewebsite.impl.resource.BytesDownloadedResource;
import ru.yaal.offlinewebsite.impl.resource.DownloadingResourceImpl;

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
        NewResource.Id newResourceId = storage.createNewResource(url);
        assertThat(newResourceId.getId(), equalTo(urlStr));
    }

    @Test
    public void createDownloadingResource() {
        NewResource.Id newResId = storage.createNewResource(url);
        DownloadingResourceImpl.Id dingRes = storage.createDownloadingResource(newResId);
        assertThat(dingRes.getId(), equalTo(urlStr));
    }

    @Test
    public void createDownloadedResource() {
        NewResource.Id newResId = storage.createNewResource(url);
        DownloadingResourceImpl.Id dingResId = storage.createDownloadingResource(newResId);
        BytesDownloadedResource.Id dedResId = storage.createDownloadedResource(dingResId);
        assertThat(dedResId.getId(), equalTo(urlStr));
    }

    @Test
    public void getResource() {
        NewResource.Id newResId = storage.createNewResource(url);
        Resource<NewResource.Id> res = storage.getResource(newResId);
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
        NewResource.Id newResId = storage.createNewResource(url);
        storage.createDownloadingResource(newResId);
        exception.expect(ResourceAlreadyExistsException.class);
        storage.createDownloadingResource(newResId);
    }

    @Test
    public void alreadyExistsDownloaded() {
        NewResource.Id newResId = storage.createNewResource(url);
        DownloadingResource.Id dingResId = storage.createDownloadingResource(newResId);
        storage.createDownloadedResource(dingResId);
        exception.expect(ResourceAlreadyExistsException.class);
        storage.createDownloadedResource(dingResId);
    }
}