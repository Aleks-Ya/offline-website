package ru.yaal.offlinewebsite.impl.parser;

import com.sun.deploy.net.HttpRequest;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.ParserParams;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.resource.*;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.impl.http.HttpInfoImpl;
import ru.yaal.offlinewebsite.impl.params.ParserParamsImpl;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;
import ru.yaal.offlinewebsite.impl.storage.SyncInMemoryStorageImpl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
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
        Storage storage = new SyncInMemoryStorageImpl();
        ParserParams params = new ParserParamsImpl(storage);
        Parser parser = new ParserImpl(params);
        SiteUrl siteUrl = new SiteUrlImpl("http://ya.ru");
        NewRes.Id newResId = storage.createNewResource(siteUrl);
        HeadingRes.Id hingResId = storage.createHeadingResource(newResId);
        HttpInfo httpInfo = new HttpInfoImpl(200, 1000, 1000000);
        HeadedRes.Id hedResId = storage.createHeadedResource(hingResId, httpInfo);
        DownloadingRes.Id dingResId = storage.createDownloadingResource(hedResId);
        DownloadingRes<DownloadingRes.Id> dingRes = storage.getResource(dingResId);
        OutputStream os = dingRes.getOutputStream();
        String html = "<html><body><a href='http://ya.ru/link'/></body></html>";
        IOUtils.write(html.getBytes(), os);
        DownloadedRes.Id dedResId = storage.createDownloadedResource(dingResId);
        ParsingRes.Id pingResId = storage.createParsingRes(dedResId);

        ParsedRes.Id pedResId = parser.parse(pingResId);

        List<NewRes.Id> newResIds = storage.getNewResourceIds();
        assertThat(newResIds, hasSize(1));
        assertThat(newResIds.get(0).getId(), equalTo("http://ya.ru/link"));


//        ParsedRes<ParsedRes.Id> pedRes = storage.getResource(pedResId);
//        String actHtml = IOUtils.toString(pedRes.getContent(), Charset.defaultCharset());
//        assertThat(actHtml, equalTo(html));
    }

}