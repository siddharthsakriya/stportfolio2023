package uk.ac.ed.inf;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PathFindingStatisticalTesting {

    public void genfiles() {
        Random random = new Random();
        int day = random.nextInt(1, 31);

        String[] args = new String[2];

        if (day < 10) {
            String dayString = "0" + String.valueOf(day);
            args[0] = "2023-10-" + dayString;
        }
        else{
            args[0] = "2023-10-" + String.valueOf(day);
        }
        args[1] = "https://ilp-rest.azurewebsites.net/";
        App.main(args);
    }

    public long time(){
        long startTime = System.currentTimeMillis();
        genfiles();
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    @Test
    public void run(){
        List<Long> times = new ArrayList<>();
        boolean flag = true;
        for (int i = 0; i < 100; i++) {
            long time = time();
            times.add(time);
            if (time > 60000){
                flag = false;
                break;
            }
        }
        Assert.assertTrue(flag);
        System.out.println(times);
    }

}
