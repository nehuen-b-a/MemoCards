package utils;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Getter
@Setter

public class CircularList<T> {
    private final List<T> elements;
    private int currentIndex;
    private boolean newBorn;
    private boolean isNext;

    // Constructor
    public CircularList() {
        this.elements = new ArrayList<>();
        this.currentIndex = 0;
        this.newBorn = true;
        this.isNext = false;
    }

    // Añadir un elemento
    public void add(T element) {
        elements.add(element);
    }

    // Añadir todos los elementos de una lista
    public void addAll(List<T> list) {
        elements.addAll(list);
    }

    public T next() {
        if (elements.isEmpty()) {
            throw new NoSuchElementException("La lista circular está vacía.");
        }

        // Ajustar índice si venimos de previous()
        if (!isNext && !newBorn) {
            currentIndex = (currentIndex + 1) % elements.size();
        }

        T element = elements.get(currentIndex);
        currentIndex = (currentIndex + 1) % elements.size(); // Avanzar circularmente
        isNext = true;
        newBorn = false;

        System.out.println("currentIndex: " + currentIndex + ", element: " + element);
        return element;
    }


    public T previous() {
        if (elements.isEmpty()) {
            throw new NoSuchElementException("La lista circular está vacía.");
        }

        // Ajustar índice si venimos de next()
        if (isNext && !newBorn) {
            currentIndex = (currentIndex - 1 + elements.size()) % elements.size();
        }

        currentIndex = (currentIndex - 1 + elements.size()) % elements.size(); // Retroceder circularmente
        T element = elements.get(currentIndex);
        isNext = false;
        newBorn = false;

        System.out.println("currentIndex: " + currentIndex + ", element: " + element);
        return element;
    }

    // Obtener el tamaño de la lista
    public int size() {
        return elements.size();
    }

    // Verificar si la lista está vacía
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    // Limpiar la lista
    public void clear() {
        elements.clear();
        currentIndex = 0;
    }

    public T get(int i) {
        return elements.get(i);
    }

    public T remove(int i) {
        return elements.remove(i);
    }
}
