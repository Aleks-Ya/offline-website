package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.ParsingRes;

import java.io.InputStream;

/**
 * @author Aleksey Yablokov
 */
@EqualsAndHashCode(callSuper = true)
@Getter
public class ParsingResImpl<R extends ParsingRes.Id>
        extends AbstractResource<R>
        implements ParsingRes<TagNode, R> {

    private final InputStream downloadedContent;

    @Setter
    private TagNode parsedContent;

    public ParsingResImpl(R id, SiteUrl url, InputStream downloadedContent) {
        super(id, url);
        this.downloadedContent = downloadedContent;
    }
}
