public class LinkedListDeque<T> {
    public class Node {
        private T item;
        private Node pre;
        private Node next;
        public Node(T item0, Node pre0, Node next0) {
            item = item0;
            pre = pre0;
            next = next0;
        }
        public Node(Node pre0, Node next0) {
            pre = pre0;
            next = next0;
        }
    }
    private final Node head;
    private int size;
    public LinkedListDeque(){
        head = new Node(null, null);
        head.pre = head;
        head.next = head;
    }
    public void addFirst(T item){
        Node cur = new Node(item, head, head.next);
        head.next.pre = cur;
        head.next = cur;
        size++;
    }
    public void addLast(T item){
        Node cur = new Node(item, head.pre, head);
        head.pre.next = cur;
        head.pre = cur;
        size++;
    }
    public boolean isEmpty(){
        return size == 0;
    }
    public int size(){
        return size;
    }
    public void printDeque(){
        Node cur = head.next;
        while(cur != head){
            System.out.print(cur.item + " ");
            cur = cur.next;
        }
    }
    public T removeFirst(){
        if(size == 0)
            return null;
        head.next.next.pre = head;
        T ans = head.next.item;
        head.next = head.next.next;
        size--;
        return ans;
    }
    public T removeLast(){
        if(size == 0)
            return null;
        head.pre.pre.next = head;
        T ans = head.pre.item;
        head.pre = head.pre.pre;
        size--;
        return ans;
    }
    public T get(int index){
        if(index >= size)
            return null;
        Node cur = head.next;
        for(int i = 0; i < index; i++)
            cur = cur.next;
        return cur.item;
    }
    public T recursive(Node node, int index){
        if(index == 0)
            return node.item;
        return recursive(node.next, index - 1);
    }
    public T getRecursive(int index){
        if(index >= size)
            return null;
        return recursive(head.next, index);
    }
}
