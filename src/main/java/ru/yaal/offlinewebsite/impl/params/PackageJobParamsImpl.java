package ru.yaal.offlinewebsite.impl.params;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.packager.Replacer;
import ru.yaal.offlinewebsite.api.params.PackageJobParams;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;

import java.io.InputStream;
import java.util.List;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@ToString
@Getter
public class PackageJobParamsImpl implements PackageJobParams {
    private final Storage storage;
    private final Packager<TagNode> htmlPackager;
    private final Packager<InputStream> inputStreamPackager;
    private final ThreadPool threadPool;
    private final List<Replacer<TagNode>> replacers;
}
