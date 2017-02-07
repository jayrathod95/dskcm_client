import com.deskcomm.core.Event;
import org.junit.Test;

/**
 * Created by Jay Rathod on 08-02-2017.
 */
public class EventTest {
    @Test
    public void insertToTable() throws Exception {


        Event event = new Event("111111111", "aaaaaaaaaaa", "121212", "cccccccccc", "vvvvvvvvvv", "090909", null, null);
        System.out.println(event.insertToTable());
        System.out.println(event.getUpdater().updateVenue("Nagpur"));

    }

    @Test
    public void createTable() {
        Event event = new Event();
        event.createTable();
    }

}