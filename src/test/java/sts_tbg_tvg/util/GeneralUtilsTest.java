package sts_tbg_tvg.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class GeneralUtilsTest {

    @Test
    public void testArrToString_withNullArray() {
        assertNull(GeneralUtils.arrToString(null));
    }

    @Test
    public void testArrToString_withEmptyArray() {
        Object[] emptyArray = new Object[0];
        assertEquals("", GeneralUtils.arrToString(emptyArray));
    }

    @Test
    public void testArrToString_withSingleElement() {
        Object[] singleElement = new Object[]{"Test"};
        assertEquals("Test", GeneralUtils.arrToString(singleElement));
    }

    @Test
    public void testArrToString_withMultipleElements() {
        Object[] multipleElements = new Object[]{"Alice", "Bob", "Charlie"};
        assertEquals("Alice, Bob, Charlie", GeneralUtils.arrToString(multipleElements));
    }

    @Test
    public void testArrToString_withNumbers() {
        Object[] numbers = new Object[]{1, 2, 3};
        assertEquals("1, 2, 3", GeneralUtils.arrToString(numbers));
    }

    @Test
    public void testRemovePrefix_withValidID() {
        String prefixedID = "sts_tbg_tvg:Strike";
        assertEquals("Strike", GeneralUtils.removePrefix(prefixedID));
    }

    @Test
    public void testRemovePrefix_withMultipleColons() {
        String prefixedID = "mod:card:type:Strike";
        assertEquals("card:type:Strike", GeneralUtils.removePrefix(prefixedID));
    }
}
