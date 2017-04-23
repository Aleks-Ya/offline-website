package ru.yaal.offlinewebsite.impl.statistics;

import lombok.Getter;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.statistics.Statistics;

/**
 * @author Aleksey Yablokov
 */
@ToString
@Getter
public class StatisticsImpl implements Statistics {
    private int createdNewRes;
    private int createdPackagedRes;
    private int createdRejectedRes;

    public void incrementCratedNewRes() {
        createdNewRes++;
    }

    public void incrementCratedPackagedRes() {
        createdPackagedRes++;
    }

    public void incrementCratedRejectedRes() {
        createdRejectedRes++;
    }
}
