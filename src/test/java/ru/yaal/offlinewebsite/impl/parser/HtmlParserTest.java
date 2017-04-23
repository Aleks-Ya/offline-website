package ru.yaal.offlinewebsite.impl.parser;

import org.apache.commons.io.IOUtils;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.Utils;
import org.junit.Test;
import ru.yaal.offlinewebsite.TestFactory;
import ru.yaal.offlinewebsite.api.params.HtmlParserParams;
import ru.yaal.offlinewebsite.api.params.RootLink;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.resource.NewRes;
import ru.yaal.offlinewebsite.api.resource.ParsedRes;
import ru.yaal.offlinewebsite.api.resource.ParsingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.impl.params.HtmlParserParamsImpl;
import ru.yaal.offlinewebsite.impl.params.LinkImpl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertTrue;

/**
 * @author Aleksey Yablokov
 */
public class HtmlParserTest {
    @Test
    public void parse() throws IOException {
        String html = IOUtils.toString(getClass().getResourceAsStream("parser_test.html"), Charset.defaultCharset());
        String rootSiteStr = "https://logback.qos.ch";
        RootLink rootLink = new LinkImpl(rootSiteStr);
        TestFactory factory = new TestFactory(rootLink);
        ResourceId<ParsingRes> parsingResId = factory.createParsingRes(rootLink, html, TestFactory.httpInfoDefault);

        HtmlParserParams<TagNode> params = new HtmlParserParamsImpl<>(factory.getStorage(), rootLink, TestFactory.allExtractors, 1);
        Parser parser = new HtmlParser(params);
        ResourceId<ParsedRes> parsedResId = parser.parse(parsingResId);

        List<ResourceId<NewRes>> newResIds = factory.getStorage().getNewResourceIds();
        assertThat(newResIds, hasSize(9));
        newResIds.stream()
                .map(newResId -> factory.getStorage().getResource(newResId).getUrl())
                .forEach(pageUrl -> assertTrue("Not full URL: " + pageUrl, Utils.isFullUrl(pageUrl.getOrigin())));

        ParsedRes pedRes = factory.getStorage().getResource(parsedResId);
        String cont = IOUtils.toString(pedRes.getParsedContent(), Charset.defaultCharset());
        assertThat(pedRes.getLinks(), hasSize(10));
        assertThat(cont, containsString("<h1>Dependencies per module</h1>"));
    }
}