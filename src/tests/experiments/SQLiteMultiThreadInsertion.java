package experiments;

import com.deskcomm.core.User;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Jay Rathod on 09-02-2017.
 */
public class SQLiteMultiThreadInsertion {
    @Test
    public void test() {
        for (int i = 0; i < 50; i++) {
            User user = new User(UUID.randomUUID().toString(),
                    RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(1)
            );
            try {
                user.insertToTable();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

}
