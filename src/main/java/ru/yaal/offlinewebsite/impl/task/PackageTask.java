package ru.yaal.offlinewebsite.impl.task;

import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.PackageTaskParams;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.task.Task;
import ru.yaal.offlinewebsite.impl.resource.ResourceIdImpl;

/**
 * @author Aleksey Yablokov
 */
public class PackageTask implements Task<PackagedRes> {
    private final Packager htmlPackager;
    private final Packager inputStreamPackager;
    private final ResourceId<PackagingRes> packagingResId;
    private final Storage storage;

    public PackageTask(PackageTaskParams params) {
        htmlPackager = params.getHtmlPackager();
        inputStreamPackager = params.getInputStreamPackager();
        packagingResId = params.getResource();
        storage = params.getStorage();
    }

    @Override
    public ResourceId<PackagedRes> call() throws Exception {
        PackagingRes packagingRes = storage.getResource(packagingResId);
        String contentType = packagingRes.getHttpInfo().getContentType();
        //TODO create ContentTypeProcessor instead of IFs
        if (HttpInfo.ContentTypes.HTML.equals(contentType)) {
            return htmlPackager.pack(new ResourceIdImpl<>(packagingResId.getId()));
        } else {
            return inputStreamPackager.pack(new ResourceIdImpl<>(packagingResId.getId()));
        }
    }
}
