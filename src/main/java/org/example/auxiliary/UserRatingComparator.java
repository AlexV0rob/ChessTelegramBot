package org.example.auxiliary;

import java.util.Comparator;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.example.states.UserState;

/**
 * Компаратор, для сравнения пользовательского рейтинга
 */
public class UserRatingComparator implements Comparator<ImmutablePair<UserState, Double>> {
    @Override
    public int compare(ImmutablePair<UserState, Double> pair1, ImmutablePair<UserState, Double> pair2) {
        if (pair1.getRight() > pair2.getRight()) {
            return -1;
        }
        if (Math.abs(pair1.getRight() - pair2.getRight()) < 1e-9) {
            return 0;
        }
        return 1;
    }

}
