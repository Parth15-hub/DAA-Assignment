import java.util.*;

public class Assignment7 {
    static Map<String, List<String>> buildStudentCourses() {
        Map<String, List<String>> sc = new HashMap<>();
        sc.put("S1", Arrays.asList("Math101", "Phys101", "AI201"));
        sc.put("S2", Arrays.asList("Math101", "Chem101"));
        sc.put("S3", Arrays.asList("AI201",  "Bio105"));
        sc.put("S4", Arrays.asList("Phys101","Chem101"));
        sc.put("S5", Arrays.asList("Bio105", "Chem101"));
        sc.put("S6", Arrays.asList("Math101","AI201"));
        sc.put("S7", Arrays.asList("Phys101","Bio105"));
        sc.put("S8", Arrays.asList("AI201","Chem101","Math101"));
        sc.put("S9", Arrays.asList("Bio105","Math101"));
        return sc;
    }

    static List<Room> buildRooms() {
        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room("LH101", 60));
        rooms.add(new Room("LH102", 50));
        rooms.add(new Room("LH103", 40));
        rooms.add(new Room("LH201", 30));
        rooms.add(new Room("LH202", 25));
        return rooms;
    }

    static class Room {
        String id;
        int capacity;
        Room(String id, int capacity) { this.id = id; this.capacity = capacity; }
    }

    static class AllocationInfo {
        int students;
        List<String> rooms = new ArrayList<>();
        String status = "OK";
    }

    static void buildGraph(Map<String, List<String>> studentCourses, Map<String, Set<String>> graph, Map<String, Integer> strength) {
        for (List<String> courses : studentCourses.values()) {
            for (String c : courses) {
                strength.put(c, strength.getOrDefault(c, 0) + 1);
                graph.putIfAbsent(c, new HashSet<>());
            }
        }
        for (List<String> courses : studentCourses.values()) {
            for (int i = 0; i < courses.size(); i++) {
                for (int j = i + 1; j < courses.size(); j++) {
                    String c1 = courses.get(i);
                    String c2 = courses.get(j);
                    graph.get(c1).add(c2);
                    graph.get(c2).add(c1);
                }
            }
        }
    }

    static Map<String, Integer> welshPowellColoring(Map<String, Set<String>> graph) {
        Map<String, Integer> color = new HashMap<>();
        List<String> order = new ArrayList<>(graph.keySet());
        order.sort((a, b) -> Integer.compare(graph.get(b).size(), graph.get(a).size()));
        for (String course : order) {
            Set<Integer> usedSlots = new HashSet<>();
            for (String neighbor : graph.get(course)) {
                if (color.containsKey(neighbor)) {
                    usedSlots.add(color.get(neighbor));
                }
            }
            int slot = 0;
            while (usedSlots.contains(slot)) slot++;
            color.put(course, slot);
        }
        return color;
    }

    static Map<Integer, List<String>> groupBySlot(Map<String, Integer> coloring) {
        Map<Integer, List<String>> slotMap = new HashMap<>();
        for (Map.Entry<String, Integer> e : coloring.entrySet()) {
            String course = e.getKey();
            int slot = e.getValue();
            slotMap.putIfAbsent(slot, new ArrayList<>());
            slotMap.get(slot).add(course);
        }
        return slotMap;
    }

    static void printColoring(Map<String, Integer> coloring) {
        Map<Integer, List<String>> slotMap = groupBySlot(coloring);
        System.out.println("Total Exam Slots Used: " + slotMap.size());
        System.out.println("---------------------------------");
        List<Integer> slots = new ArrayList<>(slotMap.keySet());
        Collections.sort(slots);
        for (int slot : slots) {
            System.out.println("Slot " + slot + " :");
            List<String> courses = slotMap.get(slot);
            Collections.sort(courses);
            for (String c : courses) System.out.println("  - " + c);
            System.out.println("---------------------------------");
        }
    }

    static Map<Integer, Map<String, AllocationInfo>> assignRooms(Map<String, Integer> coloring, Map<String, Integer> strength, List<Room> roomList) {
        Map<Integer, List<String>> slotMap = groupBySlot(coloring);
        Map<Integer, Map<String, AllocationInfo>> result = new HashMap<>();
        for (int slot : slotMap.keySet()) {
            List<String> courses = new ArrayList<>(slotMap.get(slot));
            courses.sort((a, b) -> Integer.compare(strength.getOrDefault(b,0), strength.getOrDefault(a,0)));
            List<Room> available = new ArrayList<>(roomList);
            available.sort((r1, r2) -> Integer.compare(r2.capacity, r1.capacity));
            Map<String, AllocationInfo> thisSlotAllocation = new HashMap<>();
            for (String course : courses) {
                int need = strength.getOrDefault(course, 0);
                AllocationInfo info = new AllocationInfo();
                info.students = need;
                while (need > 0 && !available.isEmpty()) {
                    Room r = available.remove(0);
                    info.rooms.add(r.id);
                    need -= r.capacity;
                }
                if (need > 0) info.status = "NOT ENOUGH CAPACITY";
                thisSlotAllocation.put(course, info);
            }
            result.put(slot, thisSlotAllocation);
        }
        return result;
    }

    static void printFinalTimetable(Map<String, Integer> coloring, Map<Integer, Map<String, AllocationInfo>> allocation) {
        Map<Integer, List<String>> slotMap = groupBySlot(coloring);
        System.out.println("FINAL TIMETABLE WITH ROOMS");
        System.out.println("=================================");
        List<Integer> slots = new ArrayList<>(slotMap.keySet());
        Collections.sort(slots);
        for (int slot : slots) {
            System.out.println("Slot " + slot + " :");
            List<String> courses = new ArrayList<>(slotMap.get(slot));
            Collections.sort(courses);
            for (String course : courses) {
                AllocationInfo info = allocation.get(slot).get(course);
                System.out.println("  Course : " + course);
                System.out.println("    Students : " + info.students);
                System.out.println("    Rooms    : " + info.rooms);
                System.out.println("    Status   : " + info.status);
            }
            System.out.println("---------------------------------");
        }
    }

    public static void main(String[] args) {
        Map<String, List<String>> studentCourses = buildStudentCourses();
        List<Room> rooms = buildRooms();
        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, Integer> strength = new HashMap<>();
        buildGraph(studentCourses, graph, strength);
        Map<String, Integer> coloring = welshPowellColoring(graph);
        System.out.println("=== EXAM SLOTS (no clashes) ===");
        printColoring(coloring);
        Map<Integer, Map<String, AllocationInfo>> allocation = assignRooms(coloring, strength, rooms);
        System.out.println();
        printFinalTimetable(coloring, allocation);
        for (int slot : allocation.keySet()) {
            for (Map.Entry<String, AllocationInfo> e : allocation.get(slot).entrySet()) {
                if (!e.getValue().status.equals("OK"))
                    System.out.println("WARNING: slot " + slot + ", course " + e.getKey() + " lacks enough seating!");
            }
        }
    }
}
