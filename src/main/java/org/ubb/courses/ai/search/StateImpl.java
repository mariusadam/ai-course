package org.ubb.courses.ai.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Marius Adam
 */
public class StateImpl implements State {
    final private BoatPosition boatPosition;
    final private Integer boatCapacity;
    final private Set<Individual> rightShore;
    final private Set<Individual> leftShore;
    final private State parent;

    public StateImpl(
            BoatPosition boatPosition,
            Set<Individual> leftShore,
            Set<Individual> rightShore,
            StateImpl parent) {
        this(boatPosition, leftShore, rightShore, parent, parent.boatCapacity);
    }

    public StateImpl(
            BoatPosition boatPosition,
            Set<Individual> leftShore,
            Set<Individual> rightShore,
            State parent,
            Integer boatCapacity) {
        this.boatPosition = boatPosition;
        this.leftShore = new HashSet<>(leftShore);
        this.rightShore = new HashSet<>(rightShore);
        this.parent = parent;
        this.boatCapacity = boatCapacity;
    }

    public BoatPosition getBoatPosition() {
        return boatPosition;
    }

    public Set<Individual> getRightShore() {
        return rightShore;
    }

    public Set<Individual> getLeftShore() {
        return leftShore;
    }

    @Override
    public Boolean isValid() {
        if (!isShoreValid(leftShore)) {
            return false;
        }

        if (!isShoreValid(rightShore)) {
            return false;
        }

        return true;
    }

    @Override
    public Boolean isSolution(State initialState) {
        if (((StateImpl) initialState).getBoatPosition() == BoatPosition.LEFT_SHORE) {
            return leftShore.size() == 0;
        }
        return rightShore.size() == 0;
    }

    protected Boolean isShoreValid(Set<Individual> shore) {
        int cannibalsCount = 0;
        int missionariesCount = 0;
        for (Individual individual : shore) {
            if (individual instanceof Cannibal) {
                cannibalsCount++;
            } else {
                missionariesCount++;
            }
        }
        if (missionariesCount == 0) {
            return true;
        }

        return cannibalsCount <= missionariesCount;
    }

    @Override
    public List<State> getSuccessors() {
        List<State> successors = new ArrayList<>();
        StateImpl current = this;

        for (int i = 1; i <= boatCapacity; i++) {
            for (List<Individual> selected : selectIndividuals(i)) {
                BoatPosition newBoatPosition;
                Set<Individual> newLeft = new HashSet<>(leftShore);
                Set<Individual> newRight = new HashSet<>(rightShore);
                if (boatPosition == BoatPosition.LEFT_SHORE) {
                    newLeft.removeAll(selected);
                    newRight.addAll(selected);
                    newBoatPosition = BoatPosition.RIGHT_SHORE;
                } else {
                    newRight.removeAll(selected);
                    newLeft.addAll(selected);
                    newBoatPosition = BoatPosition.LEFT_SHORE;
                }

                State newState = new StateImpl(
                        newBoatPosition,
                        newLeft,
                        newRight,
                        current
                );

                if (newState.isValid()) {
                    successors.add(newState);
                }
            }
        }

        return successors;
    }

    private Iterable<List<Individual>> selectIndividuals(Integer boatCapacity) {
        List<Individual> individuals;
        if (boatPosition == BoatPosition.LEFT_SHORE) {
            individuals = new ArrayList<>(leftShore);
        } else {
            individuals = new ArrayList<>(rightShore);
        }

        List<List<Individual>> result = new ArrayList<>();
        selectIndividualsRecursive(result, new ArrayList<>(), individuals, boatCapacity);

        return result;
    }

    private void selectIndividualsRecursive(List<List<Individual>> result, List<Individual> tempList, List<Individual> individuals, Integer boatCapacity) {
        if (tempList.size() == boatCapacity) {
            result.add(new ArrayList<>(tempList));
        } else {
            for (int i = 0; i < individuals.size(); i++) {
                if (tempList.contains(individuals.get(i))) {
                    continue; // element already exists, skip
                }

                tempList.add(individuals.get(i));
                selectIndividualsRecursive(result, tempList, individuals, boatCapacity);
                tempList.remove(tempList.size() - 1);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StateImpl state = (StateImpl) o;

        if (getBoatPosition() != state.getBoatPosition()) return false;
        if (!getRightShore().equals(state.getRightShore())) return false;
        return getLeftShore().equals(state.getLeftShore());
    }

    @Override
    public int hashCode() {
        int result = getBoatPosition().hashCode();
        result = 31 * result + getRightShore().hashCode();
        result = 31 * result + getLeftShore().hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("Left Shore: ");
        leftShore.forEach(individual -> ret.append(individual).append(" "));
        if (boatPosition == BoatPosition.LEFT_SHORE) {
            ret.append(" (Boat) ");
        }

        ret.append("\nRight Shore: ");
        rightShore.forEach(individual -> ret.append(individual).append(" "));
        if (boatPosition == BoatPosition.RIGHT_SHORE) {
            ret.append(" (Boat) ");
        }

        ret.append("\n---------------------------\n");
        return ret.toString();
    }

    @Override
    public State getParent() {
        return parent;
    }

    @Override
    public Integer priority() {
        return leftShore.size();
    }
}
