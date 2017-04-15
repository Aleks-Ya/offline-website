package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

/**
 * @author Aleksey Yablokov
 */
@EqualsAndHashCode(callSuper = true)
@Getter
public class PackagingResImpl extends AbstractRes<PackagingRes> implements PackagingRes<TagNode> {

    private final TagNode content;

    public PackagingResImpl(ResourceId<PackagingRes> id, SiteUrl url, TagNode content) {
        super(id, url);
        this.content = content;
    }
}
