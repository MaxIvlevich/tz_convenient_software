package max.iv.tz_convenient_software;

import max.iv.tz_convenient_software.services.impl.XlsxServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class XlsxServiceImplTest {

    private final XlsxServiceImpl service = new XlsxServiceImpl();

    @Test
    void testFindNthMinimum_validCase() {
        List<Integer> input = Arrays.asList(10, 3, 5, 2, 8);
        int n = 3;
        int expected = 5;

        int result = service.findNthMinimum(input, n);
        assertEquals(expected, result);
    }

    @Test
    void testFindNthMinimum_invalidN() {
        List<Integer> input = Arrays.asList(1, 2);
        assertThrows(IllegalArgumentException.class, () -> {
            service.findNthMinimum(input, 5);
        });
    }

    @Test
    void testFindNthMinimum_zeroN() {
        List<Integer> input = Arrays.asList(1, 2);
        assertThrows(IllegalArgumentException.class, () -> {
            service.findNthMinimum(input, 0);
        });
    }

    @Test
    void testFindNthMinimum_nullList() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.findNthMinimum(null, 1);
        });
    }
}
