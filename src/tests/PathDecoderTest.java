import com.deskcomm.support.L;
import com.deskcomm.support.PathDecoder;
import org.junit.Test;

import java.util.Map;

/**
 * Created by Jay Rathod on 06-02-2017.
 */
public class PathDecoderTest {
    @Test
    public void getPathParams() throws Exception {
        Map<Integer, String> pathParams = PathDecoder.getPathParams("message/user/asdjhash");
        L.println(pathParams.toString());
    }

}