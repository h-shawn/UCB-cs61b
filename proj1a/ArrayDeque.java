public class ArrayDeque<T> {
    private T[] array;
    private int size;
    private int length;
    private int first;
    private int last;
    @SuppressWarnings("unchecked")
    public ArrayDeque(){
        array = (T[]) new Object[8];
        size = 0;
        length = 8;
        first = last = 4;
    }
    public int forward(int index, int mod){
        return (index + 1) % mod;
    }
    public int backward(int index, int mod){
        return (index + mod - 1) % mod;
    }
    @SuppressWarnings("unchecked")
    public void expand(){
        T[] new_array = (T[]) new Object[length * 2];
        int p = first;
        int new_p = length / 2;
        while(p != last){
            new_array[new_p] = array[p];
            p = forward(p, length);
            new_p = forward(new_p, 2 * length);
        }
        first = length / 2;
        last = new_p;
        array = new_array;
        length *= 2;
    }
    @SuppressWarnings("unchecked")
    public void shrink(){
        T[] new_array = (T[]) new Object[length / 2];
        int p = first;
        int new_p = length / 4;
        while(p != last){
            new_array[new_p] = array[p];
            p = forward(p, length / 2);
            new_p = forward(new_p, length / 2);
        }
        first = length / 4;
        last = new_p;
        array = new_array;
        length /= 2;
    }
    public void addFirst(T item){
        if(size == length - 1)
            expand();
        first = backward(first, length);
        array[first] = item;
        size++;
    }
    public void addLast(T item){
        if(size == length - 1)
            expand();
        array[last] = item;
        last = forward(last, length);
        size++;
    }
    public boolean isEmpty(){
        return size == 0;
    }
    public int size(){
        return size;
    }
    public void printDeque(){
        for(int i = first; i != last; i = forward(i, length))
            System.out.print(array[i] + " ");
    }
    public T removeFirst(){
        if(size == 0)
            return null;
        if(length >= 16 && length / size > 4)
            shrink();
        T ans = array[first];
        first = forward(first, length);
        size--;
        return ans;
    }
    public T removeLast(){
        if(size == 0)
            return null;
        if(length >= 16 && length / size > 4)
            shrink();
        last = backward(last, length);
        size--;
        return array[last];
    }
    public T get(int index){
        if(index >= size)
            return null;
        int k = first;
        for(int i = 0; i < index; i++)
            k = forward(k, length);
        return array[k];
    }
}
