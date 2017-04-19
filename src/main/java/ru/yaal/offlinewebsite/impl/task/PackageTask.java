package ru.yaal.offlinewebsite.impl.task;

import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.packager.Replacer;
import ru.yaal.offlinewebsite.api.params.PackageTaskParams;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.task.Task;
import ru.yaal.offlinewebsite.impl.resource.ResourceIdImpl;

import java.io.InputStream;
import java.util.List;

/**
 * @author Aleksey Yablokov
 */
public class PackageTask implements Task<PackagedRes> {
    private final Packager<TagNode> htmlPackager;
    private final Packager<InputStream> inputStreamPackager;
    private final ResourceId<PackagingRes<Object>> packagingResId;
    private final List<Replacer<TagNode>> replacers;
    private final Storage storage;

    public PackageTask(PackageTaskParams params) {
        htmlPackager = params.getHtmlPackager();
        inputStreamPackager = params.getInputStreamPackager();
        packagingResId = params.getResource();
        storage = params.getStorage();
        replacers = params.getReplacers();
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
