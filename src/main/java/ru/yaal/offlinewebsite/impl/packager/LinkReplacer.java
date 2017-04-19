package ru.yaal.offlinewebsite.impl.packager;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.packager.Replacer;
import ru.yaal.offlinewebsite.api.packager.ReplacerParams;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.impl.parser.UrlHelper;
import ru.yaal.offlinewebsite.impl.resource.ResourceIdImpl;

import java.net.URL;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class LinkReplacer implements Replacer<TagNode> {
    private final Storage storage;
    private final URL rootUrl;

    @SneakyThrows
    public LinkReplacer(ReplacerParams params) {
        storage = params.getStorage();
        rootUrl = new URL(params.getRootSiteUrl().getUrl());
    }

    @Override
    public ResourceId<PackagingRes<TagNode>> replaceUrls(ResourceId<PackagingRes<TagNode>> packagingResId) {
        PackagingRes<TagNode> packagingRes = storage.getResource(packagingResId);
        TagNode content = packagingRes.getContent();
        for (TagNode node : content.getElementListByName("a", true)) {
            String attrValue = node.getAttributeByName("href");
            URL absoluteURL = UrlHelper.newAbsoluteURL(rootUrl, attrValue);
            ResourceId<PackagedRes> packagedResId = new ResourceIdImpl<>(absoluteURL.toString());
            PackagedRes packagedRes = storage.getResource(packagedResId);
            if (packagedRes != null) {
                String urlStr = packagedRes.getUrl().getUrl();
                node.addAttribute("a", urlStr);//TODO all object should be immutable
                log.debug("Set attribute <a href>: " + urlStr);
            }
        }
        return packagingResId;
    }
}
