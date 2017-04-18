package ru.yaal.offlinewebsite.impl.task;

import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.PackageTaskParams;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.task.Task;

/**
 * @author Aleksey Yablokov
 */
public class PackageTask implements Task<PackagedRes> {
    private final Packager<TagNode> packager;
    private final ResourceId<PackagingRes<TagNode>> packagingResId;

    public PackageTask(PackageTaskParams<TagNode> params) {
        packager = params.getPackager();
        packagingResId = params.getResource();
    }

    @Override
    public ResourceId<PackagedRes> call() throws Exception {
        return packager.pack(packagingResId);
    }
}
