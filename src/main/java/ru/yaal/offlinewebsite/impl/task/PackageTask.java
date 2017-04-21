package ru.yaal.offlinewebsite.impl.task;

import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.PackageTaskParams;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.task.Task;

import java.util.List;

/**
 * @author Aleksey Yablokov
 */
public class PackageTask implements Task<PackagedRes> {
    private final List<Packager> packagers;
    private final ResourceId<PackagingRes> packagingResId;
    private final Storage storage;

    public PackageTask(PackageTaskParams params) {
        packagers = params.getPackagers();
        packagers.sort((p1, p2) -> Integer.valueOf(p1.getPriority()).compareTo(p2.getPriority()));
        packagingResId = params.getResource();
        storage = params.getStorage();
    }

    @Override
    public ResourceId<PackagedRes> call() throws Exception {
        PackagingRes packagingRes = storage.getResource(packagingResId);
        String contentType = packagingRes.getHttpInfo().getContentType();
        return packagers.stream()
                .filter(packager -> packager.accept(contentType))
                .findFirst().orElseThrow(() -> new IllegalStateException("Packager not found for: " + packagingRes))
                .pack(packagingResId);
    }
}
