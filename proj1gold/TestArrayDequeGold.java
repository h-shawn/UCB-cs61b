import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayDeque;

public class TestArrayDequeGold {
    @Test
    public void testArrayDequeAndStudentArray() {
        ArrayDeque<Integer> adq = new ArrayDeque<>();
        StudentArrayDeque<Integer> sadq = new StudentArrayDeque<>();
        int addNum, option, num = 1000;
        int adqNum, sadqNum;
        String log = "";

        for (int i = 0; i < num; i++) {
            adqNum = 0;
            sadqNum = 0;

            addNum = StdRandom.uniform(100);
            if (adq.isEmpty()) {
                option = StdRandom.uniform(2);
            } else {
                option = StdRandom.uniform(4);
            }

            switch (option) {
                case 0:
                    log = log + "addFirst(" + addNum + ")\n";
                    adq.addFirst(addNum);
                    sadq.addFirst(addNum);
                    break;
                case 1:
                    log = log + "addLast(" + addNum + ")\n";
                    adq.addLast(addNum);
                    sadq.addLast(addNum);
                    break;
                case 2:
                    log = log + "removeFirst()\n";
                    adqNum = adq.removeFirst();
                    sadqNum = sadq.removeFirst();
                    break;
                case 3:
                    log = log + "removeLast()\n";
                    adqNum = adq.removeLast();
                    sadqNum = sadq.removeLast();
                    break;
                default:
            }
            assertEquals(log, adqNum, sadqNum);
        }
    }
}
