package ru.yaal.offlinewebsite.impl.resource;

import org.junit.Test;
import ru.yaal.offlinewebsite.api.link.Link;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.api.resource.NewRes;
import ru.yaal.offlinewebsite.impl.link.LinkImpl;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static ru.yaal.offlinewebsite.impl.resource.ResourceComparator.INSTANCE;

/**
 * @author Aleksey Yablokov
 */
public class ResourceComparatorTest {
    private final static String urlStr = "a";
    private final Link link = new LinkImpl(urlStr);
    private final NewRes newRes = new NewResImpl(new ResIdImpl<>(urlStr), link);
    private final HeadingRes headingRes = new HeadingResImpl(new ResIdImpl<>(urlStr), link);

    @Test
    public void compare() {
        assertThat(INSTANCE.compare(newRes, headingRes), lessThan(0));
    }

    @Test
    public void isFirstGreater() {
        assertFalse(INSTANCE.isFirstGreaterOrEquals(newRes.getClass(), headingRes.getClass()));
    }

}