import com.deskcomm.core.Event;
import com.deskcomm.db.DbConnection;
import org.junit.Test;

import java.sql.Connection;

/**
 * Created by Jay Rathod on 08-02-2017.
 */
public class EventTest {
    @Test
    public void insertToTable() throws Exception {

        Connection connection = DbConnection.getConnection();
        connection.setAutoCommit(false);
        //insert to messages
        //ddddddddddddddddd.


        connection.commit();

    }

    @Test
    public void createTable() {
        Event event = new Event();
        event.createTable();
    }

}