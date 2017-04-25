package ru.yaal.offlinewebsite.impl.resource;

import org.junit.Test;
import ru.yaal.offlinewebsite.api.params.ResUrl;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.api.resource.NewRes;
import ru.yaal.offlinewebsite.impl.params.ResUrlImpl;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static ru.yaal.offlinewebsite.impl.resource.ResourceComparator.INSTANCE;

/**
 * @author Aleksey Yablokov
 */
public class ResourceComparatorTest {
    private final static String urlStr = "a";
    private final ResUrl resUrl = new ResUrlImpl(urlStr);
    private final NewRes newRes = new NewResImpl(new ResIdImpl<>(urlStr), resUrl);
    private final HeadingRes headingRes = new HeadingResImpl(new ResIdImpl<>(urlStr), resUrl);

    @Test
    public void compare() {
        assertThat(INSTANCE.compare(newRes, headingRes), lessThan(0));
    }

    @Test
    public void isFirstGreater() {
        assertFalse(INSTANCE.isFirstGreaterOrEquals(newRes.getClass(), headingRes.getClass()));
    }

}