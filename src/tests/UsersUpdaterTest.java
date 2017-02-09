import com.deskcomm.core.User;
import com.deskcomm.core.bookkeeping.BookKeeper;
import org.json.JSONArray;
import org.junit.Test;

/**
 * Created by Jay Rathod on 08-02-2017.
 */
public class UsersUpdaterTest {
    @Test
    public void updateUsers() throws Exception {
        User user = new User("1234", "jay", "rathod", "asasas", "123", "aaaaaaaaaa", "M");
        JSONArray array = new JSONArray();
        array.put(user.toJSON());
        boolean b = BookKeeper.getUsersUpdater().updateUsers(array);
        System.out.println(b);
    }
}