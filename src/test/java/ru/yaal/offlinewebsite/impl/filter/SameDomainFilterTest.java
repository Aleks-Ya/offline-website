package ru.yaal.offlinewebsite.impl.filter;

import org.junit.Test;
import ru.yaal.offlinewebsite.api.filter.Filter;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Aleksey Yablokov
 */
public class SameDomainFilterTest {
    @Test
    public void isAccepted() {
        SiteUrl rootUrl = new SiteUrlImpl("http://google.com/search");
        Filter<SiteUrl> filter = new SameDomainFilter(rootUrl);
        assertTrue(filter.isAccepted(new SiteUrlImpl("http://google.com/find/something")));
        assertFalse(filter.isAccepted(new SiteUrlImpl("http://yandex.com/search")));
    }

}