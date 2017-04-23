package ru.yaal.offlinewebsite.api.statistics;

/**
 * @author Aleksey Yablokov
 */
public interface Statistics {
    int getCreatedNewRes();

    int getCreatedPackagedRes();

    int getCreatedRejectedRes();
}
