package ru.yaal.offlinewebsite.impl.resource;

import ru.yaal.offlinewebsite.api.resource.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Aleksey Yablokov
 */
public class ResourceComparatorImpl implements ResourceComparator {
    private final Map<Class<? extends Resource>, Integer> ress = new HashMap<Class<? extends Resource>, Integer>() {{
        put(NewRes.class, 1);
        put(NewResImpl.class, 1);

        put(HeadingRes.class, 2);
        put(HeadingResImpl.class, 2);

        put(HeadedRes.class, 3);
        put(HeadedResImpl.class, 3);

        put(DownloadingRes.class, 4);
        put(DownloadingResImpl.class, 4);

        put(DownloadedRes.class, 5);
        put(BytesDownloadedRes.class, 5);

        put(ParsingRes.class, 6);
        put(ParsingResImpl.class, 6);

        put(ParsedRes.class, 7);
        put(BytesParsedRes.class, 7);

        put(RejectedRes.class, 8);
        put(RejectedResImpl.class, 8);
    }};

    @Override
    public int compare(Resource r1, Resource r2) {
        return ress.get(r1.getClass()).compareTo(ress.get(r2.getClass()));
    }

    @Override
    public boolean isFirstGreaterOrEquals(Class<? extends Resource> resClass1, Class<? extends Resource> resClass2) {
        return ress.get(resClass1).compareTo(ress.get(resClass2)) >= 0;
    }
}
