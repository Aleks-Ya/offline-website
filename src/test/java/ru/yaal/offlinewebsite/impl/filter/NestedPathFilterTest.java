package ru.yaal.offlinewebsite.impl.filter;

import org.junit.Test;
import ru.yaal.offlinewebsite.TestFactory;
import ru.yaal.offlinewebsite.api.filter.FilterDecision;
import ru.yaal.offlinewebsite.api.filter.HeadingResFilter;
import ru.yaal.offlinewebsite.api.params.PageUrl;
import ru.yaal.offlinewebsite.api.params.RootPageUrl;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.impl.params.PageUrlImpl;

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
        RootPageUrl rootPageUrl = new PageUrlImpl(rootPageStr);
        TestFactory factory = new TestFactory(rootPageUrl);
        HeadingResFilter filter = new NestedPathFilter(rootPageUrl, true);
        PageUrl pageUrl = new PageUrlImpl(pageStr);
        ResourceId<HeadingRes> headingResId = factory.createHeadingRes(pageUrl, TestFactory.httpInfoDefault);
        HeadingRes headingRes = factory.getStorage().getResource(headingResId);
        return filter.filter(headingRes);
    }

}