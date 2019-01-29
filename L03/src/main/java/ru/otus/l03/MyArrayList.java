package ru.otus.l03;

import java.util.*;

public class MyArrayList<T> implements List<T> {
    private int size = 0;
    private Object[] elementData;

    MyArrayList() {
        elementData = new Object[0];

    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(T t) {
        size++;
        Object[] newElementData = new Object[size];
        System.arraycopy(elementData, 0, newElementData, 0, elementData.length);
        elementData = newElementData;
        elementData[size - 1] = t;
        return true;
    }

    @Override
    public T remove(int index) {
        if (size == 0 || index > size - 1) return null;
        size--;
        Object[] newElementData = new Object[size];
        System.arraycopy(elementData, 0, newElementData, 0, index);
        System.arraycopy(elementData, index + 1, newElementData, index, size - index);
        T result = (T) elementData[index];
        elementData = newElementData;
        return result;
    }

    @Override
    public T get(int index) {
        if (index < size)
            return (T) elementData[index];
        return null;

    }

    @Override
    public T set(int index, T element) {
        if (index < size) {
            T oldElm = (T) elementData[index];
            elementData[index] = element;
            return oldElm;
        }
        return null;
    }


    @Override
    public ListIterator<T> listIterator() {
        return new LstItr();
    }

    public Object[] toArray() {
        return Arrays.copyOf(elementData, size);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }


    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }


    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public void add(int index, T element) {

    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return null;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public String toString() {
        return "MyArrayList{" +
                "elementData=" + Arrays.toString(elementData) +
                '}';


    }

    private class LstItr implements ListIterator<T> {
        int curIndex;

        public LstItr() {
            this.curIndex = 0;
        }

        @Override
        public boolean hasNext() {
            if (curIndex < size) return true;
            return false;
        }

        @Override
        public T next() {
            if (curIndex < size && size > 0) {
                curIndex++;
                return (T) elementData[curIndex - 1];
            }
            throw new NoSuchElementException();
        }

        @Override
        public void set(T t) {
            elementData[curIndex - 1] = t;
        }

        @Override
        public boolean hasPrevious() {
            return false;
        }

        @Override
        public T previous() {
            return null;
        }

        @Override
        public int nextIndex() {
            return 0;
        }

        @Override
        public int previousIndex() {
            return 0;
        }

        @Override
        public void remove() {

        }


        @Override
        public void add(T t) {

        }
    }
}
