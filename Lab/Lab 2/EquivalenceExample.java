package lab2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquivalenceExample {
    public static void main(String[] args) {
        Employee e1 = new Employee(1L, "John");
        Employee e2 = new Employee(1L, "John");
        Employee e3 = new Employee(2L, "Mary");

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(e1);
        employeeList.add(e2);
        employeeList.add(e3);


        Map<Employee, Integer> map = new HashMap<>();
        Integer count;
        for(Employee e : employeeList){
            if ((count = map.get(e)) == null) {
                map.put(e, 1);
            } else {
                map.put(e, 1 + count);
            }
        }

        System.out.println(map);

        e3.setId(3L);
        map.put(new Employee(3L, "Mary"), 3);
        System.out.println(map);

    }
}
