package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.ParsedRes;

/**
 * @author Aleksey Yablokov
 */
@EqualsAndHashCode(callSuper = true)
public class BytesParsedRes<R extends ParsedRes.Id>
        extends AbstractResource<R>
        implements ParsedRes<TagNode, R> {

    @Getter
    private final TagNode parsedContent;

    public BytesParsedRes(R id, SiteUrl url, TagNode parsedContent) {
        super(id, url);
        this.parsedContent = parsedContent;
    }
}
