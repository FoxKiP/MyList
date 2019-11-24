package com.MyList;

import java.util.NoSuchElementException;

public class MyListImpl<E> implements MyList<E> {
    private Entry<E> head;
    private Entry<E> tail;
    private int size = 0;

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if(isEmpty()) return false;
        return indexOf(o) != -1;
    }

    @Override
    public boolean add(E e) {
        linkTail(e);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if(index == -1) return false;
        remove(index);
        return true;
    }

    @Override
    public void clear() {
        this.head = null;
        this.tail = null;
        size = 0;
    }

    @Override
    public E get(int index) {
        return getData(index);
    }

    @Override
    public E getFirst() {
        if(isEmpty()) throw new NoSuchElementException();
        return this.head.data;
    }

    @Override
    public E getLast() {
        if(isEmpty()) throw new NoSuchElementException();
        return this.tail.data;
    }

    @Override
    public E removeFirst() {
        if(isEmpty()) throw new NoSuchElementException();
        return unlinkHead();
    }

    @Override
    public E removeLast() {
        if(isEmpty()) throw new NoSuchElementException();
        return unlinkTail();
    }

    @Override
    public void addFirst(E e) {
        linkHead(e);
    }

    @Override
    public void addLast(E e) {
        linkTail(e);
    }

    @Override
    public E set(int index, E e) {
        checkIndex(index);
        Entry<E> entry = getEntry(index);
        E data = entry.data;
        entry.data = e;
        return data;
    }

    @Override
    public void add(int index, E e) {
        if(index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        else if(index == 0) linkHead(e);
        else if(index == size) linkTail(e);
        else linkBefore(index, e);
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        if(index == 0) return unlinkHead();
        if(index == size - 1) return unlinkTail();
        return unlink(index);
    }

    private void linkTail(E data) {
        Entry<E> tail = this.tail;
        Entry<E> newTail = new Entry<>(tail, null, data);
        this.tail = newTail;
        if(tail == null) this.head = newTail;
        else tail.next = newTail;
        ++size;
    }

    private void linkHead(E data) {
        Entry<E> head = this.head;
        Entry<E> newHead = new Entry<>(null, head, data);
        this.head = newHead;
        if(head == null) this.tail = newHead;
        else head.previous = newHead;
        ++size;
    }

    private void linkBefore(int index, E data) {
        Entry<E> next = getEntry(index);
        Entry<E> previous = next.previous;
        Entry<E> entry = new Entry<>(previous, next, data);
        previous.next = entry;
        next.previous = entry;
        ++size;
    }

    private E unlinkHead() {
        E data = this.head.data;
        Entry<E> newHead = this.head.next;
        if(newHead == null) {
            this.head = null;
            this.tail = null;
        } else {
            newHead.previous = null;
            this.head = newHead;
        }
        --size;
        return data;
    }

    private E unlinkTail() {
        E data = this.tail.data;
        Entry<E> newTail = this.tail.previous;
        if(newTail == null) {
            this.head = null;
            this.tail = null;
        } else {
            newTail.next = null;
            this.tail = newTail;
        }
        --size;
        return data;
    }

    private E unlink(int index) {
        Entry<E> target = getEntry(index);
        E data = target.data;
        Entry<E> previous = target.previous;
        Entry<E> next = target.next;
        previous.next = next;
        next.previous = previous;
        --size;
        return data;
    }

    private void checkIndex(int index) {
        if(index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    private E getData(int index) {
        return getEntry(index).data;
    }

    private Entry<E> getEntry(int index) {
        checkIndex(index);
        if(index == 0) return this.head;
        if(index == this.size - 1) return this.tail;

        int middleIndex = getIndexMiddleElement();
        if(index <= middleIndex) return searchForward(index);
        else return searchBack(index);
    }

    private int getIndexMiddleElement() {
        return this.size / 2;
    }

    private Entry<E> searchForward(int index) {
        Entry<E> target = this.head;
        for(int i = 1; i <= index; i++) {
            target = target.next;
        }
        return target;
    }

    private Entry<E> searchBack(int index) {
        Entry<E> target = this.tail;
        for(int i = 1; i < this.size - index; i++) {
            target = target.previous;
        }
        return target;
    }

    private int indexOf(Object o) {
        Entry<E> next = this.head;
        if(o == null) {
            for(int i = 0; i < this.size; i++) {
                if(next.data == null) return i;
                else next = next.next;
            }
        } else {
            for(int i = 0; i < this.size; i++) {
                if(next.data.equals(o)) return i;
                else next = next.next;
            }
        }
        return -1;
    }

    private class Entry<E> {
        Entry<E> previous;
        Entry<E> next;
        E data;

        public Entry(Entry<E> previous, Entry<E> next, E data) {
            this.data = data;
            this.previous = previous;
            this.next = next;
        }
    }
}
