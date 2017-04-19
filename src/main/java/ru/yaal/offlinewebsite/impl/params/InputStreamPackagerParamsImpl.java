package ru.yaal.offlinewebsite.impl.params;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.yaal.offlinewebsite.api.packager.OfflinePathResolver;
import ru.yaal.offlinewebsite.api.params.InputStreamPackagerParams;
import ru.yaal.offlinewebsite.api.storage.Storage;

import java.nio.file.Path;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@Getter
public class InputStreamPackagerParamsImpl implements InputStreamPackagerParams {
    private final Path outletDir;
    private final OfflinePathResolver offlinePathResolver;
    private final Storage storage;
}
