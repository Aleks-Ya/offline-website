package ru.yaal.offlinewebsite.impl.parser;

import org.apache.commons.io.IOUtils;
import org.htmlcleaner.TagNode;
import org.junit.Test;
import ru.yaal.offlinewebsite.TestFactory;
import ru.yaal.offlinewebsite.api.params.ParserParams;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.resource.NewRes;
import ru.yaal.offlinewebsite.api.resource.ParsedRes;
import ru.yaal.offlinewebsite.api.resource.ParsingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.impl.params.ParserParamsImpl;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;

import java.io.IOException;
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
        String html = IOUtils.toString(getClass().getResourceAsStream("parser_test.html"), Charset.defaultCharset());
        String rootSiteStr = "https://logback.qos.ch";
        SiteUrl rootSiteUrl = new SiteUrlImpl(rootSiteStr);
        TestFactory factory = new TestFactory(rootSiteUrl);
        ResourceId<ParsingRes<TagNode>> parsingResId = factory.createParsingRes(rootSiteUrl, html, TestFactory.httpInfoDefault);

        ParserParams<TagNode> params = new ParserParamsImpl<>(factory.getStorage(), rootSiteUrl, TestFactory.allExtractors);
        Parser<TagNode> parser = new ParserImpl(params);
        ResourceId<ParsedRes<TagNode>> parsedResId = parser.parse(parsingResId);

        List<ResourceId<NewRes>> newResIds = factory.getStorage().getNewResourceIds();
        assertThat(newResIds, hasSize(13));

        ParsedRes<TagNode> pedRes = factory.getStorage().getResource(parsedResId);
        TagNode hrefNode = pedRes.getParsedContent();
        assertThat(hrefNode.getName(), equalTo("html"));
    }
}