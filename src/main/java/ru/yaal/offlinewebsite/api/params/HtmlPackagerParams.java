package ru.yaal.offlinewebsite.api.params;

import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.packager.OfflinePathResolver;
import ru.yaal.offlinewebsite.api.packager.Replacer;
import ru.yaal.offlinewebsite.api.storage.Storage;

import java.nio.file.Path;
import java.util.List;

/**
 * @author Aleksey Yablokov
 */
public interface HtmlPackagerParams extends Params {
    Path getOutletDir();

    OfflinePathResolver getOfflinePathResolver();

    Storage getStorage();

    List<Replacer<TagNode>> getReplacers();
}
