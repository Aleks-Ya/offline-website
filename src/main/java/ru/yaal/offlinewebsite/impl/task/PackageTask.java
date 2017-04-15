package ru.yaal.offlinewebsite.impl.task;

import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.PackageTaskParams;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.task.Task;

/**
 * @author Aleksey Yablokov
 */
public class PackageTask implements Task {
    private final Storage storage;
    private final Packager<TagNode> packager;
    private final ResourceId<PackagingRes<TagNode>> packagingResId;

    public PackageTask(PackageTaskParams<TagNode> params) {
        storage = params.getStorage();
        packager = params.getPackager();
        packagingResId = params.getResource();
    }

    @Override
    public Void call() throws Exception {
        packager.pack(packagingResId);
        return null;
    }
}
