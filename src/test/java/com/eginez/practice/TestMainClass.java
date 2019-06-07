package com.eginez.practice;

import org.junit.Test;

import java.util.*;

public class TestMainClass {
    @Test
    public void createPermutations() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
        List<List<Integer>> all = permute(numbers);
        System.out.println(all);
    }

    List<List<Integer>> permute(List<Integer> n) {
        if (n.size() == 1) {
            ArrayList<List<Integer>> ll = new ArrayList<>();
            ll.add(n);
            return ll;
        }

        List<List<Integer>> all = new ArrayList<>();
        for (int i = 0; i < n.size(); i++){
            List<Integer> m = new ArrayList<>(n);
            m.remove(i);
            List<List<Integer>> permuted = permute(m);
            for (List<Integer> p : permuted) {
                p.add(0,n.get(i));
                all.add(p);
            }
        }
        return all;
    }


    @Test
    public void palindromePartitions() {
        String str = "gfefaabbasdasd";

        List<String> partitions = partitions(str);
        System.out.println(partitions);

        for(String s : partitions){
            if(isPalindrome(s)) {
                System.out.println(s);
            }
        }
    }

    public List<String>partitions(String str) {

        List<String> parts = new ArrayList<>();
        for (int i = 1; i <= str.length(); i++) {
            int stepSize = i;

            int start = 0;
            int end = start+stepSize;
            while(end <= str.length()) {
                parts.add(str.substring(start, end));
                start++;
                end =start + stepSize;
            }
        }
        return parts;
    }

    public boolean isPalindrome(String str) {
        int i = 0;
        int j = str.length() - 1;
        char[] c = str.toCharArray();

        while( i < j) {
            if(c[i] != c[j]) {
                return false;
            }
            i++;
            j--;
        }
        return true;
    }


    @Test
    public void testSubsetSum() {
        assert isSubsetSum(Arrays.asList(1, 20, 18), 19);
        assert isSubsetSum(Arrays.asList(1, 3, 18, 3, 2, 4 ), 23);
        assert !isSubsetSum(Arrays.asList(1, 3, 18, 3, 2, 4 ), 123);
        assert isSubsetSum(Arrays.asList(100,200,300, 1,2,3 ), 6);
    }

    private boolean isSubsetSum(List<Integer> numbers, int target) {
        if (numbers.size() == 1) {
            return numbers.get(0) == target;
        }

        ArrayList<Integer> rest = new ArrayList<>(numbers);
        Integer head = numbers.get(0);
        rest.remove(0);
        return isSubsetSum(rest, target - head) || isSubsetSum(rest, target);
    }


    @Test
    public void testLevenshteinDistance() {
        assert 0 == startLV("", "").distance;
        assert 0 == startLV("ab", "ab").distance;
        assert 3 == startLV("abc", "").distance;
        assert 3 == startLV("", "abc").distance;
        assert 1 == startLV("a", "b").distance;
        assert 2 == startLV("a", "bc").distance;
        assert 1 == startLV("ki", "ka").distance;
        assert 2 == startLV("abc", "a").distance;
        assert 3 == startLV("abc", "d").distance;
        startLV("this is a nice life", "this is another sentence");
        startLV("this is a nice life asdf asdf asdf asdf asdf asdf ", " akd akdee dkakao 3 3 2 3this is another sentence");

    }

    private class LVData {
        int distance;
        List<String> changes;
    }

    private LVData startLV(String a, String b) {
        LVData res = new LVData();
        int[][] state =new int[a.length()][b.length()];
        res.distance = levenshteinDistance(a,b,state);
        res.changes = genChanges(a, b, state);
        System.out.println(res.changes);
        return res;
    }

    //Walks back from the graph and generates the transitions
    private List<String> genChanges(String a, String b, int[][]state){
        LinkedList<String> all = new LinkedList<>();

        if (a.isEmpty() && b.isEmpty()) {
            return Collections.emptyList();
        }

        if (a.isEmpty()) {
            for (Character c : b.toCharArray()) {
                all.add(String.format("-%s", c));
            }
            return all;
        }

        if (b.isEmpty()) {
            for (Character c : a.toCharArray()) {
                all.add(String.format("+%s", c));
            }
            return all;
        }
        int i = state.length - 1;
        int j = state[0].length - 1;
        while (i >= 0 && j >= 0) {
            if (i == 0 && j == 0) {
                String val = a.charAt(i) == b.charAt(j) ? String.format("%s", a.charAt(i)) : String.format("%s->%s", a.charAt(i), b.charAt(j));
                all.push(val);
                break;
            }

            if (i == 0){
                all.push(String.format("+%s", b.charAt(j)));
                j = j -1;
                continue;
            }

            if(j == 0) {
                all.push(String.format("-%s", a.charAt(i)));
                i = i -1;
                continue;
            }

            int min = min(state[i-1][j], state[i][j-1], state[i-1][j-1]);
            if (min == state[i-1][j]){ // deletion
                all.push(String.format("-%s", a.charAt(i)));
                i = i -1;
                continue;
            }

            if (min == state[i][j-1]) { //insertion
                all.push(String.format("+%s", b.charAt(j)));
                j = j -1;
                continue;
            }

            //substitution
            String val = a.charAt(i) == b.charAt(j) ? String.format("%s", a.charAt(i)) : String.format("%s->%s", a.charAt(i), b.charAt(j));
            all.push(val);
            i--;
            j--;
        }
        return all;
    }

    public int levenshteinDistance(String a, String b, int[][] state) {

        if (Math.min(a.length(), b.length()) == 0) {
            return Math.max(a.length(), b.length());
        }

        int i, j;
        i = j = 0;
        for( i  = 0; i < a.length(); i++) {
            for( j = 0; j <b.length(); j++) {
                int val = a.charAt(i) == b.charAt(j) ? 0 : 1;

                if (i ==0 && j == 0) {
                    state[i][j] = val;
                    continue;
                }

                if (i == 0) {
                    state[i][j] = 1 + state[i][j-1];
                    continue;
                }

                if (j == 0) {
                    state[i][j] = 1 + state[i - 1][j];
                    continue;
                }

                int deletion = 1 + state[i - 1][j];
                int insertion = 1 + state[i][j-1];
                int subs = state[i-1][j-1]+ val;
                int min = min(deletion, insertion, subs);
                state[i][j] = min;

            }
        }
        i = Math.max(0, i-1);
        j = Math.max(0, j -1);
        return state[i][j];

    }

    private int min(int ...numbers) {
        Arrays.sort(numbers);
        return numbers[0];
    }


    class  Edge {
        int x, y, w;
        Edge(int x, int y, int w) {
            this.x = x;
            this.y = y;
            this.w = w;
        }
    }

    @Test
    public void shortestPath() {
        Map<Integer, List<Edge>> gr = new HashMap<>();
        gr.put(0, Arrays.asList(new Edge(0,1,1), new Edge(0,2,10)));
        gr.put(1, Arrays.asList(new Edge(1,2,2)));
        gr.put(2, Arrays.asList(new Edge(2,3,5), new Edge(2,6,1000)));
        gr.put(3, Arrays.asList(new Edge(3,4,2)));
        gr.put(4, Arrays.asList(new Edge(4,5,2)));
        gr.put(5, Arrays.asList(new Edge(5,6,100)));
        gr.put(6, Arrays.asList());

        assert 112 == diks(gr, 0, 6);
        assert 3 == diks(gr, 0, 2);
    }

    private int diks(Map<Integer, List<Edge>> graph, int start, int end) {
        Integer[] nodes = graph.keySet().toArray(new Integer[0]);
        boolean[] visited = new boolean[nodes.length];
        int[] distance =  new int[nodes.length];

        for (int i = 0; i < nodes.length; i++) {
            visited[i] = false;
            distance[i] = Integer.MAX_VALUE;
        }

        class dd {
            int node, distance;
            dd(int node, int distance) {this.node = node; this.distance = distance;}
        }
        Queue<dd> nn = new PriorityQueue<>(new Comparator<dd>() {
            @Override
            public int compare(dd o1, dd o2) {
                return Integer.compare(o1.distance, o2.distance);
            }
        });
        nn.add(new dd(start, 0));
        distance[start] = 0;

        dd current = null;
        while (!nn.isEmpty()) {
            current = nn.poll();
            for (Edge e : graph.get(current.node)) {
                if (visited[e.x]) {
                    continue;
                }

                distance[e.y] = Math.min(distance[current.node] + e.w, distance[e.y]);
                nn.add(new dd(e.y, distance[e.y]));
            }
            if (current.node == end) {
                break;
            }
            visited[current.node] = true;
        }
        if (current == null) {
            throw new IllegalArgumentException();
        }
        return distance[current.node];
    }


    @Test
    public void permsInString() {
        String a = "abdc";
        System.out.println(a.substring(0,4));
    }


    @Test
    public void testThreeSum() {
        System.out.println(ThreeSum(Arrays.asList(-2, 0, 2, 1, 3), 0));

    }

    Set<List<Integer>> ThreeSum(List<Integer> nums, int target) {

        Map<Integer, Integer> mapNums = new HashMap<>();
        Set<List<Integer>> res = new HashSet<>();

        for (Integer i : nums) {
            if (mapNums.containsKey(i)) {
                mapNums.put(i, mapNums.get(i)+1);
            } else {
                mapNums.put(i,1);
            }
        }

        for(Integer i : nums) {
            List<Integer> currentNums = new ArrayList<>(nums);
            currentNums.remove(i);
            Map<Integer, Integer> newMap = new HashMap(mapNums);
            remove(i, newMap);
            for (int j : currentNums) {
                int newTarget = target - i - j;
                if (currentNums.contains(newTarget)) {
                    res.add(Arrays.asList(i,j,newTarget));
                }
            }
        }

        return res;
    }

    public void remove(int i, Map<Integer, Integer> map) {
        if(!map.containsKey(i)) {
            return;
        }

        int newVal = map.get(i) - 1;
        if ( newVal == 0) {
            map.remove(i);
        }
        return;
    }

    class Node<T> {
        List<Node> connections;
        T value;
        void visit() {
            System.out.println(value);
        }
        Node(T val) { this.value = val;}
    }

    @Test
    public void testBST(){
        List<Node> nodes = Arrays.asList(new Node<>(2), new Node<>(3), new Node<>(4));
        Node root = new Node<>(0);
        root.connections = nodes;
        BST(root);
    }

    public <T> void BST(Node<T> root) {
        Queue<Node<T>> q = new LinkedList<Node<T>>();
        q.add(root);

        while(!q.isEmpty()) {
            Node n = q.poll();
            if (n == null) {
                continue;
            }
            if (n.connections != null) {
                q.addAll(n.connections);
            }
            n.visit();
        }
    }

}
