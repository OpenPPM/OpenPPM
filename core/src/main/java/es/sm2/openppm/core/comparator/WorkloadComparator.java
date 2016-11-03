package es.sm2.openppm.core.comparator;

import es.sm2.openppm.core.javabean.TeamMembersFTEs;

import java.util.Comparator;

/**
 * Created by mariano.fontana on 28/07/2015.
 */
public class WorkloadComparator implements Comparator<TeamMembersFTEs> {

    @Override
    public int compare(TeamMembersFTEs firstTeamMembersFTEs, TeamMembersFTEs secondTeamMembersFTEs) {

        int firstWorkloadAverage = -1;
        int secondWorkloadAverage = -1;

        for (int i = 0; i < firstTeamMembersFTEs.getFtes().length; i++) {

            firstWorkloadAverage += firstTeamMembersFTEs.getFtes()[i];
            secondWorkloadAverage += secondTeamMembersFTEs.getFtes()[i];
        }

        firstWorkloadAverage = firstWorkloadAverage/ firstTeamMembersFTEs.getFtes().length;
        secondWorkloadAverage = secondWorkloadAverage/ firstTeamMembersFTEs.getFtes().length;

        return firstWorkloadAverage <= secondWorkloadAverage ? -1 : 1;
    }

}
