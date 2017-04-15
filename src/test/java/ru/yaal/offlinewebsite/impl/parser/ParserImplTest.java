package ru.yaal.offlinewebsite.impl.parser;

import org.htmlcleaner.TagNode;
import org.junit.Test;
import ru.yaal.offlinewebsite.TestFactory;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.ParserParams;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.resource.NewRes;
import ru.yaal.offlinewebsite.api.resource.ParsedRes;
import ru.yaal.offlinewebsite.api.resource.ParsingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.impl.http.HttpInfoImpl;
import ru.yaal.offlinewebsite.impl.params.ParserParamsImpl;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;

import java.io.IOException;
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
        String html = "<html><body><a href='http://ya.ru/link'/></body></html>";
        String rootSiteStr = "http://ya.ru";
        SiteUrl rootSiteUrl = new SiteUrlImpl(rootSiteStr);
        TestFactory factory = new TestFactory(rootSiteUrl);
        ResourceId<ParsingRes<TagNode>> parsingResId = factory.createParsingRes(rootSiteUrl, html, TestFactory.httpInfoDefault);

        ParserParams params = new ParserParamsImpl(factory.getStorage(), rootSiteUrl);
        Parser<TagNode> parser = new ParserImpl(params);
        ResourceId<ParsedRes<TagNode>> parsedResId = parser.parse(parsingResId);

        List<ResourceId<NewRes>> newResIds = factory.getStorage().getNewResourceIds();
        assertThat(newResIds, hasSize(1));
        assertThat(newResIds.get(0).getId(), equalTo("http://ya.ru/link"));

        ParsedRes<TagNode> pedRes = factory.getStorage().getResource(parsedResId);
        TagNode hrefNode = pedRes.getParsedContent();
        assertThat(hrefNode.getName(), equalTo("html"));
    }
}