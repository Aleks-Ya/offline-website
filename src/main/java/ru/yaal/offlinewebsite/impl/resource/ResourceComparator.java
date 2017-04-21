package ru.yaal.offlinewebsite.impl.resource;

import ru.yaal.offlinewebsite.api.resource.DownloadedRes;
import ru.yaal.offlinewebsite.api.resource.DownloadingRes;
import ru.yaal.offlinewebsite.api.resource.HeadedRes;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.api.resource.NewRes;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ParsedRes;
import ru.yaal.offlinewebsite.api.resource.ParsingRes;
import ru.yaal.offlinewebsite.api.resource.RejectedRes;
import ru.yaal.offlinewebsite.api.resource.Resource;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Aleksey Yablokov
 */
public class ResourceComparator implements Comparator<Resource> {
    public static ResourceComparator INSTANCE = new ResourceComparator();

    private ResourceComparator() {
    }

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

        put(PackagingRes.class, 8);
        put(PackagingResImpl.class, 8);

        put(PackagedRes.class, 9);
        put(PackagedResImpl.class, 9);

        put(RejectedRes.class, 10);
        put(RejectedResImpl.class, 10);
    }};

    @Override
    public int compare(Resource r1, Resource r2) {
        return ress.get(r1.getClass()).compareTo(ress.get(r2.getClass()));
    }

    public boolean isFirstGreaterOrEquals(Class<? extends Resource> resClass1, Class<? extends Resource> resClass2) {
        return ress.get(resClass1).compareTo(ress.get(resClass2)) >= 0;
    }
}
