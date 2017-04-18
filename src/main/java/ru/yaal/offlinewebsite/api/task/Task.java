package ru.yaal.offlinewebsite.api.task;

import ru.yaal.offlinewebsite.api.resource.ResourceId;

import java.util.concurrent.Callable;

/**
 * @author Aleksey Yablokov
 */
public interface Task<T> extends Callable<ResourceId<T>> {
}
