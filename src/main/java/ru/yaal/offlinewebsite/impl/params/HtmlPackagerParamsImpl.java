package ru.yaal.offlinewebsite.impl.params;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.packager.OfflinePathResolver;
import ru.yaal.offlinewebsite.api.packager.Replacer;
import ru.yaal.offlinewebsite.api.params.HtmlPackagerParams;
import ru.yaal.offlinewebsite.api.storage.Storage;

import java.nio.file.Path;
import java.util.List;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@Getter
public class HtmlPackagerParamsImpl implements HtmlPackagerParams {
    private final Path outletDir;
    private final OfflinePathResolver offlinePathResolver;
    private final Storage storage;
    private final List<Replacer<TagNode>> replacers;
}
