package ru.yaal.offlinewebsite.api.task;

import java.util.concurrent.Callable;

/**
 * @author Aleksey Yablokov
 */
public interface Task extends Callable<Void> {
}
