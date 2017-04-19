package ru.yaal.offlinewebsite.impl.parser;

import org.apache.commons.io.IOUtils;
import org.htmlcleaner.TagNode;
import org.junit.Test;
import ru.yaal.offlinewebsite.TestFactory;
import ru.yaal.offlinewebsite.api.params.ParserParams;
import ru.yaal.offlinewebsite.api.params.RootSiteUrl;
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
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;

/**
 * @author Aleksey Yablokov
 */
public class HtmlParserTest {
    @Test
    public void parse() throws IOException {
        String html = IOUtils.toString(getClass().getResourceAsStream("parser_test.html"), Charset.defaultCharset());
        String rootSiteStr = "https://logback.qos.ch";
        RootSiteUrl rootSiteUrl = new SiteUrlImpl(rootSiteStr);
        TestFactory factory = new TestFactory(rootSiteUrl);
        ResourceId<ParsingRes> parsingResId = factory.createParsingRes(rootSiteUrl, html, TestFactory.httpInfoDefault);

        ParserParams<TagNode> params = new ParserParamsImpl<>(factory.getStorage(), rootSiteUrl, TestFactory.allExtractors, 1);
        Parser parser = new HtmlParser(params);
        ResourceId<ParsedRes> parsedResId = parser.parse(parsingResId);

        List<ResourceId<NewRes>> newResIds = factory.getStorage().getNewResourceIds();
        assertThat(newResIds, hasSize(9));

        ParsedRes pedRes = factory.getStorage().getResource(parsedResId);
        String cont = IOUtils.toString(pedRes.getParsedContent(), Charset.defaultCharset());
        assertThat(pedRes.getLinks(), hasSize(10));
        assertThat(cont, containsString("<h1>Dependencies per module</h1>"));
    }
}