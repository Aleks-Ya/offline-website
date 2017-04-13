package ru.yaal.offlinewebsite.impl.filter;

import org.junit.Test;
import ru.yaal.offlinewebsite.api.filter.Filter;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;
import ru.yaal.offlinewebsite.impl.resource.HeadingResImpl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Aleksey Yablokov
 */
public class SameDomainFilterTest {
    @Test
    public void isAccepted() {
        SiteUrl rootUrl = new SiteUrlImpl("http://google.com/search");
        Filter<HeadingRes> filter = new SameDomainFilter(rootUrl);
        String accepted = "http://google.com/find/something";
        String notAccepted = "http://yandex.com/search";
        assertTrue(filter.filter(makeNewRes(accepted)).isAccepted());
        assertFalse(filter.filter(makeNewRes(notAccepted)).isAccepted());
    }

    private HeadingRes<HeadingRes.Id> makeNewRes(String s1) {
        return new HeadingResImpl<>(new HeadingRes.Id(s1), new SiteUrlImpl(s1));
    }

}