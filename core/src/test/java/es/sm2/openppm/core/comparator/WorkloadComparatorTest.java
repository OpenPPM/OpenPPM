package es.sm2.openppm.core.comparator;

import es.sm2.openppm.core.javabean.TeamMembersFTEs;

import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mariano.fontana on 28/07/2015.
 */
public class WorkloadComparatorTest {

    @Test
    public void testOrderList(){

        List<TeamMembersFTEs> ftes = new ArrayList<TeamMembersFTEs>();

        //Element 1
        TeamMembersFTEs teamMemberFTE1 = new TeamMembersFTEs();
        int [] ftes1= {75,25,50};//mean 50: third
        teamMemberFTE1.setFtes(ftes1);
        ftes.add(teamMemberFTE1);

        //Element 2
        TeamMembersFTEs teamMemberFTE2 = new TeamMembersFTEs();
        int [] ftes2= {10,20,30}; //mean 20: second
        teamMemberFTE2.setFtes(ftes2);
        ftes.add(teamMemberFTE2);

        //Element 3
        TeamMembersFTEs teamMemberFTE3 = new TeamMembersFTEs();
        int [] ftes3= {1,2,3}; //mean 2: first
        teamMemberFTE3.setFtes(ftes3);
        ftes.add(teamMemberFTE3);

        //Element 4
        TeamMembersFTEs teamMemberFTE4 = new TeamMembersFTEs();
        int [] ftes4= {100,200,300}; //mean 200: fourth
        teamMemberFTE4.setFtes(ftes4);
        ftes.add(teamMemberFTE4);

        //Element 5
        TeamMembersFTEs teamMemberFTE5 = new TeamMembersFTEs();
        int [] ftes5= {1000,2000,3000}; //mean 2000: fifth
        teamMemberFTE5.setFtes(ftes5);
        ftes.add(teamMemberFTE5);

        // Order the list by using the comparator to test
        Collections.sort(ftes, new WorkloadComparator());

        //Check the order of the list
        Assert.assertEquals("The order list of TeamMembersFTEs by WorkloadComparator " +
                "is incorrect", ftes3, ftes.get(0).getFtes());
        Assert.assertEquals("The order list of TeamMembersFTEs by WorkloadComparator " +
                "is incorrect", ftes2, ftes.get(1).getFtes());
        Assert.assertEquals("The order list of TeamMembersFTEs by WorkloadComparator " +
                "is incorrect", ftes1, ftes.get(2).getFtes());
        Assert.assertEquals("The order list of TeamMembersFTEs by WorkloadComparator " +
                "is incorrect", ftes4, ftes.get(3).getFtes());
        Assert.assertEquals("The order list of TeamMembersFTEs by WorkloadComparator " +
                "is incorrect", ftes5, ftes.get(4).getFtes());
    }
}
