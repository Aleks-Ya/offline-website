package ru.yaal.offlinewebsite.impl.resource;

import org.junit.Test;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.api.resource.NewRes;
import ru.yaal.offlinewebsite.api.resource.ResourceComparator;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * @author Aleksey Yablokov
 */
public class ResourceComparatorImplTest {
    private final ResourceComparator comparator = new ResourceComparatorImpl();
    private final static String urlStr = "a";
    private final SiteUrlImpl siteUrl = new SiteUrlImpl(urlStr);
    private final NewRes newRes = new NewResImpl(new ResourceIdImpl<>(urlStr), siteUrl);
    private final HeadingRes headingRes = new HeadingResImpl(new ResourceIdImpl<>(urlStr), siteUrl);

    @Test
    public void compare() {
        assertThat(comparator.compare(newRes, headingRes), lessThan(0));
    }

    @Test
    public void isFirstGreater() {
        assertFalse(comparator.isFirstGreaterOrEquals(newRes.getClass(), headingRes.getClass()));
    }

}