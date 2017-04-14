package ru.yaal.offlinewebsite.impl.parser;

import org.apache.commons.io.IOUtils;
import org.htmlcleaner.TagNode;
import org.junit.Test;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.ParserParams;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.params.StorageParams;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.resource.*;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.impl.http.HttpInfoImpl;
import ru.yaal.offlinewebsite.impl.params.ParserParamsImpl;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;
import ru.yaal.offlinewebsite.impl.params.StorageParamsImpl;
import ru.yaal.offlinewebsite.impl.resource.ResourceComparatorImpl;
import ru.yaal.offlinewebsite.impl.storage.SyncInMemoryStorageImpl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

/**
 * @author Aleksey Yablokov
 */
public class ParserImplTest {
    @Test
    public void parse() throws IOException {
        StorageParams storageParams = new StorageParamsImpl(new ResourceComparatorImpl());
        Storage storage = new SyncInMemoryStorageImpl(storageParams);
        SiteUrl siteUrl = new SiteUrlImpl("http://ya.ru");
        ParserParams params = new ParserParamsImpl(storage, siteUrl);
        Parser parser = new ParserImpl(params);
        ResourceId<NewRes> newResId = storage.createNewResource(siteUrl);
        ResourceId<HeadingRes> hingResId = storage.createHeadingResource(newResId);
        HttpInfo httpInfo = new HttpInfoImpl(200, 1000, 1000000);
        ResourceId<HeadedRes> hedResId = storage.createHeadedResource(hingResId, httpInfo);
        ResourceId<DownloadingRes> dingResId = storage.createDownloadingResource(hedResId);
        DownloadingRes dingRes = storage.getResource(dingResId);
        OutputStream os = dingRes.getOutputStream();
        String html = "<html><body><a href='http://ya.ru/link'/></body></html>";
        IOUtils.write(html.getBytes(), os);
        ResourceId<DownloadedRes> dedResId = storage.createDownloadedResource(dingResId);
        ResourceId<ParsingRes> pingResId = storage.createParsingRes(dedResId);

        ResourceId<ParsedRes> pedResId = parser.parse(pingResId);

        List<ResourceId<NewRes>> newResIds = storage.getNewResourceIds();
        assertThat(newResIds, hasSize(1));
        assertThat(newResIds.get(0).getId(), equalTo("http://ya.ru/link"));

        ParsedRes<TagNode> pedRes = storage.getResource(pedResId);
        TagNode hrefNode = pedRes.getParsedContent();
        assertThat(hrefNode.getName(), equalTo("html"));
    }

}