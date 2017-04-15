package ru.yaal.offlinewebsite.api.resource;

import java.nio.file.Path;

/**
 * @author Aleksey Yablokov
 */
public interface PackagedRes extends Resource<PackagedRes> {
    Path getLocation();
}
