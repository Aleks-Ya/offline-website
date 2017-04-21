package ru.yaal.offlinewebsite.impl.resource;

import org.junit.Test;
import ru.yaal.offlinewebsite.api.params.PageUrl;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.api.resource.NewRes;
import ru.yaal.offlinewebsite.api.resource.ResourceComparator;
import ru.yaal.offlinewebsite.impl.params.PageUrlImpl;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * @author Aleksey Yablokov
 */
public class ResourceComparatorImplTest {
    private final ResourceComparator comparator = new ResourceComparatorImpl();
    private final static String urlStr = "a";
    private final PageUrl pageUrl = new PageUrlImpl(urlStr);
    private final NewRes newRes = new NewResImpl(new ResourceIdImpl<>(urlStr), pageUrl);
    private final HeadingRes headingRes = new HeadingResImpl(new ResourceIdImpl<>(urlStr), pageUrl);

    @Test
    public void compare() {
        assertThat(comparator.compare(newRes, headingRes), lessThan(0));
    }

    @Test
    public void isFirstGreater() {
        assertFalse(comparator.isFirstGreaterOrEquals(newRes.getClass(), headingRes.getClass()));
    }

}