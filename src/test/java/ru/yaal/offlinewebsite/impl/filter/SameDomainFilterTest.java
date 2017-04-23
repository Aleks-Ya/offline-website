package ru.yaal.offlinewebsite.impl.filter;

import org.junit.Test;
import ru.yaal.offlinewebsite.api.filter.Filter;
import ru.yaal.offlinewebsite.api.params.RootPageUrl;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.impl.params.PageUrlImpl;
import ru.yaal.offlinewebsite.impl.resource.HeadingResImpl;
import ru.yaal.offlinewebsite.impl.resource.ResIdImpl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Aleksey Yablokov
 */
public class SameDomainFilterTest {
    @Test
    public void isAccepted() {
        RootPageUrl rootUrl = new PageUrlImpl("http://google.com/search");
        Filter<HeadingRes> filter = new SameDomainFilter(rootUrl);
        String accepted = "http://google.com/find/something";
        String notAccepted = "http://yandex.com/search";
        assertTrue(filter.filter(makeNewRes(accepted)).isAccepted());
        assertFalse(filter.filter(makeNewRes(notAccepted)).isAccepted());
    }

    private HeadingRes makeNewRes(String s1) {
        return new HeadingResImpl(new ResIdImpl<>(s1), new PageUrlImpl(s1));
    }

}