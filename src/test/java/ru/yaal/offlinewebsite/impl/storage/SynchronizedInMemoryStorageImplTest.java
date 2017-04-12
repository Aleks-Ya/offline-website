package ru.yaal.offlinewebsite.impl.storage;

import org.junit.Test;
import ru.yaal.offlinewebsite.api.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.*;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.impl.SiteUrlImpl;
import ru.yaal.offlinewebsite.impl.resource.BytesDownloadedResource;
import ru.yaal.offlinewebsite.impl.resource.DownloadingResourceImpl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author Aleksey Yablokov
 */
public class SynchronizedInMemoryStorageImplTest {
    private final Storage storage = new SynchronizedInMemoryStorageImpl();
    private final String urlStr = "abc";
    private final SiteUrl url = new SiteUrlImpl(urlStr);

    @Test
    public void createNewResource() {
        NewResource.NewResourceId newResourceId = storage.createNewResource(url);
        assertThat(newResourceId.getId(), equalTo(urlStr));
    }

    @Test
    public void createDownloadingResource() {
        NewResource.NewResourceId newResourceId = storage.createNewResource(url);
        DownloadingResourceImpl.Id downloadingResource = storage.createDownloadingResource(newResourceId);
        assertThat(downloadingResource.getId(), equalTo(urlStr));
    }

    @Test
    public void createDownloadedResource() {
        NewResource.NewResourceId newResourceId = storage.createNewResource(url);
        DownloadingResourceImpl.Id downloadingResource = storage.createDownloadingResource(newResourceId);
        BytesDownloadedResource.Id downloadedResourceId = storage.createDownloadedResource(downloadingResource);
        assertThat(downloadedResourceId.getId(), equalTo(urlStr));
    }

    @Test
    public void getResource() {
        NewResource.NewResourceId newResourceId = storage.createNewResource(url);
        Resource<NewResource.NewResourceId> res = storage.getResource(newResourceId);
        assertNotNull(res);
        assertThat(res.getId().getId(), equalTo(urlStr));
    }


}