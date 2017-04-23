package ru.yaal.offlinewebsite.impl.filter;

import org.junit.Test;
import ru.yaal.offlinewebsite.TestFactory;
import ru.yaal.offlinewebsite.api.filter.FilterDecision;
import ru.yaal.offlinewebsite.api.filter.HeadingResFilter;
import ru.yaal.offlinewebsite.api.link.Link;
import ru.yaal.offlinewebsite.api.link.RootLink;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.impl.link.LinkImpl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Aleksey Yablokov
 */
public class NestedPathFilterTest {
    @Test
    public void accepted() {
        FilterDecision decision = getFilterDecision(
                "http://ya.ru/site/index.html", "http://ya.ru/site/info/contacts.html");
        assertTrue(decision.isAccepted());
    }

    @Test
    public void notAccepted() {
        FilterDecision decision = getFilterDecision(
                "http://ya.ru/site/index.html", "http://ya.ru/data/info/contacts.html");
        assertFalse(decision.isAccepted());
    }

    @Test
    public void emptyPath() {
        FilterDecision decision = getFilterDecision(
                "http://ya.ru", "http://ya.ru");
        assertTrue(decision.toString(), decision.isAccepted());
    }

    @Test
    public void differentDomains() {
        FilterDecision decision = getFilterDecision(
                "http://ya.ru/site/index.html", "http://ya2.ru/site/index.html");
        assertFalse(decision.isAccepted());
    }

    private static FilterDecision getFilterDecision(String rootPageStr, String pageStr) {
        RootLink rootLink = new LinkImpl(rootPageStr);
        TestFactory factory = new TestFactory(rootLink);
        HeadingResFilter filter = new NestedPathFilter(rootLink, true);
        Link link = new LinkImpl(pageStr);
        ResourceId<HeadingRes> headingResId = factory.createHeadingRes(link, TestFactory.httpInfoDefault);
        HeadingRes headingRes = factory.getStorage().getResource(headingResId);
        return filter.filter(headingRes);
    }

}