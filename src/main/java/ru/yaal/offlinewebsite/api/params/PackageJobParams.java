package ru.yaal.offlinewebsite.api.params;

import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.packager.Replacer;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;

import java.io.InputStream;
import java.util.List;

/**
 * @author Aleksey Yablokov
 */
public interface PackageJobParams extends Params {
    Storage getStorage();

    //TODO remove TagNode from interfaces
    Packager<TagNode> getHtmlPackager();

    Packager<InputStream> getInputStreamPackager();

    ThreadPool getThreadPool();

    List<Replacer<TagNode>> getReplacers();
}
