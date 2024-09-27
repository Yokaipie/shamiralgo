package org.example;

import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        String file1 = "src/main/java/org/example/input1.json";
        String file2 = "src/main/java/org/example/input2.json";

        try {
            solve(file1);
            solve(file2);
        } catch (Exception e) {
            System.out.println("An exception has occurred " + Arrays.toString(e.getStackTrace()));
        }

    }

    // Lagrange Interpolation Method
    public static double lagrangeInterpolation(Map<Integer, Long> points, int k) {
        double result = 0.0;
        for (Map.Entry<Integer, Long> p1 : points.entrySet()) {
            int x1 = p1.getKey();
            long y1 = p1.getValue();

            double term = y1;
            for (Map.Entry<Integer, Long> p2 : points.entrySet()) {
                int x2 = p2.getKey();
                if (x1 != x2) {
                    term *= (double) -x2 / (x1 - x2);
                }
            }
            result += term;
        }
        return result;
    }

    public static void solve(String jsonPath) throws Exception {
        // Read the JSON input file
        String jsonString = new String(Files.readAllBytes(Paths.get(jsonPath)));
        JSONObject jsonObject = new JSONObject(jsonString);

        // Extract n and k from the keys object
        JSONObject keys = jsonObject.getJSONObject("keys");
        int n = keys.getInt("n");
        int k = keys.getInt("k");

        // Map to hold the (x, y) points
        Map<Integer, Long> points = new HashMap<>();

        // Loop through the roots and decode the y values
        for (String key : jsonObject.keySet()) {
            if (!key.equals("keys")) {
                int x = Integer.parseInt(key);
                JSONObject point = jsonObject.getJSONObject(key);
                int base = Integer.parseInt(point.getString("base"));
                String value = point.getString("value");

                // Decode the y value from the given base to base 10
                long y = Long.parseLong(value, base);

                // Add the (x, y) point to the map
                points.put(x, y);
            }
        }

        // Use Lagrange interpolation to find the constant term (c)
        double c = lagrangeInterpolation(points, k);

        // Print the result (constant term 'c')
        System.out.println("Constant term c: " + c);
    }
}
