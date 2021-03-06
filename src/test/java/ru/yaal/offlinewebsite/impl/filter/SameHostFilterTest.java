package ru.yaal.offlinewebsite.impl.filter;

import org.junit.Test;
import ru.yaal.offlinewebsite.api.filter.Filter;
import ru.yaal.offlinewebsite.api.params.RootResUrl;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.impl.params.ResUrlImpl;
import ru.yaal.offlinewebsite.impl.resource.HeadingResImpl;
import ru.yaal.offlinewebsite.impl.resource.ResIdImpl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Aleksey Yablokov
 */
public class SameHostFilterTest {
    @Test
    public void isAccepted() {
        RootResUrl rootUrl = new ResUrlImpl("http://google.com/search");
        Filter<HeadingRes> filter = new SameHostFilter(rootUrl, true);
        String accepted = "http://google.com/find/something";
        String notAccepted = "http://yandex.com/search";
        assertTrue(filter.filter(makeNewRes(accepted)).isAccepted());
        assertFalse(filter.filter(makeNewRes(notAccepted)).isAccepted());
    }

    private HeadingRes makeNewRes(String s1) {
        return new HeadingResImpl(new ResIdImpl<>(s1), new ResUrlImpl(s1));
    }

}