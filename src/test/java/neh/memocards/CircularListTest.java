package neh.memocards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.CircularList;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class CircularListTest {

    private CircularList<String> circularList;

    @BeforeEach
    void setUp() {
        circularList = new CircularList<>();
    }

    @Test
    void testAddAndSize() {
        circularList.add("Elemento 1");
        circularList.add("Elemento 2");

        assertEquals(2, circularList.size());
        assertFalse(circularList.isEmpty());
    }

    @Test
    void testNextCircularBehavior() {
        circularList.addAll(Arrays.asList("A", "B", "C"));

        assertEquals("A", circularList.next());
        assertEquals("B", circularList.next());
        assertEquals("C", circularList.next());
        assertEquals("A", circularList.next()); // Vuelve al inicio
    }

    @Test
    void testPreviousCircularBehavior() {
        circularList.addAll(Arrays.asList("A", "B", "C"));

        assertEquals("A", circularList.next()); // Empieza en "A"
        assertEquals("C", circularList.previous()); // Retrocede circularmente a "C"
        assertEquals("B", circularList.previous()); // Retrocede a "B"
        assertEquals("A", circularList.previous()); // Retrocede a "A"
    }


    @Test
    void testClearAndIsEmpty() {
        circularList.addAll(Arrays.asList("A", "B", "C"));
        assertFalse(circularList.isEmpty());

        circularList.clear();
        assertTrue(circularList.isEmpty());
        assertEquals(0, circularList.size());
    }

    @Test
    void testNextOnEmptyListThrowsException() {
        assertThrows(NoSuchElementException.class, circularList::next);
    }

    @Test
    void testPreviousOnEmptyListThrowsException() {
        assertThrows(NoSuchElementException.class, circularList::previous);
    }

    @Test
    void testAddAll() {
        circularList.addAll(Arrays.asList("X", "Y", "Z"));

        assertEquals(3, circularList.size());
        assertEquals("X", circularList.next());
        assertEquals("Y", circularList.next());
    }

    @Test
    void testMixNextAndPrevious() {
        circularList.addAll(Arrays.asList("1", "2", "3"));

        assertEquals("1", circularList.next()); // Avanza a "1"
        System.out.println(circularList.getElements().get(circularList.getCurrentIndex()));

        assertEquals("2", circularList.next()); // Avanza a "2"
        System.out.println(circularList.getElements().get(circularList.getCurrentIndex()));

        assertEquals("1", circularList.previous()); // Retrocede a "1"
        System.out.println(circularList.getElements().get(circularList.getCurrentIndex()));

        assertEquals("3", circularList.previous()); // Retrocede a "3"
        System.out.println(circularList.getElements().get(circularList.getCurrentIndex()));

        assertEquals("1", circularList.next()); // Avanza de nuevo a "1"
    }


}
