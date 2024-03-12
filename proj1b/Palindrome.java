public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> dq = new ArrayDeque<>();
        for (int i = 0; i < word.length(); i++) {
            dq.addLast(word.charAt(i));
        }
        return dq;
    }
//  simple version
    public boolean isPalindrome(String word) {
        int i = 0, j = word.length() - 1;
        boolean flag = true;
        while (i < j) {
            if (word.charAt(i) != word.charAt(j)) {
                flag = false;
                break;
            }
            i++;
            j--;
        }
        return flag;
    }

/*  deque version
    public boolean isPalindrome(String word) {
        Deque<Character> dq = wordToDeque(word);
        boolean flag = true;
        while (dq.size() > 1) {
            if (dq.removeFirst() != dq.removeLast()) {
                flag = false;
                break;
            }
        }
        return flag;
    }
 */

/*  recursive version
    private boolean isPalindrome(String word, int i, int j) {
        if (i >= j) {
            return true;
        }
        if (word.charAt(i) != word.charAt(j)) {
            return false;
        }
        return isPalindrome(word, i + 1, j - 1);
    }
    public boolean isPalindrome(String word) {
        return isPalindrome(word, 0, word.length() - 1);
    }
 */
    public boolean isPalindrome(String word, CharacterComparator cc) {
        int i = 0, j = word.length() - 1;
        boolean flag = true;
        while (i < j) {
            if (!cc.equalChars(word.charAt(i), word.charAt(j))) {
                flag = false;
                break;
            }
            i++;
            j--;
        }
        return flag;
    }
}
