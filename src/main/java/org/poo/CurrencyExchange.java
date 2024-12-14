package org.poo;

import org.poo.fileio.ExchangeInput;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

public class CurrencyExchange {
    private static CurrencyExchange instance = null;
    private HashMap<String, Double> rates;

    private CurrencyExchange() {
        rates = new HashMap<>();
    }

    public static CurrencyExchange getInstance() {
        if (instance == null) {
            instance = new CurrencyExchange();
        }
        return instance;
    }

    public void init(ExchangeInput[] input) {
        rates = new HashMap<>();
        for (ExchangeInput in : input) {
            rates.put(in.getFrom() + "-" + in.getTo(), in.getRate());
            rates.put(in.getTo() + "-" + in.getFrom(), 1.0 / in.getRate());
        }
    }

    public double getRate(String from, String to) {
        if (from.equals(to)) {
            return 1.0;
        }
        if (rates.containsKey(from + "-" + to)) {
            return rates.get(from + "-" + to);
        }

        HashSet<String> visited = new HashSet<>();
        LinkedList<String> queue = new LinkedList<>();
        LinkedList<Double> ratesQueue = new LinkedList<>();

        queue.add(from);
        ratesQueue.add(1.0);
        visited.add(from);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            double currentRate = ratesQueue.poll();

            for (Map.Entry<String, Double> entry : rates.entrySet()) {
                String start = entry.getKey().split("-")[0];
                String end = entry.getKey().split("-")[1];
                if (start.equals(current) && !visited.contains(end)) {
                    double newRate = currentRate * rates.get(current + "-" + end);

                    if (!rates.containsKey(start + "-" + end)) {
                        rates.put(start + "-" + end, newRate);
                        rates.put(end + "-" + start, 1.0 / currentRate);
                    }

                    if (end.equals(to)) {
                        return newRate;
                    }

                    queue.add(end);
                    ratesQueue.add(newRate);
                    visited.add(end);
                }
            }
        }

        return -1;
    }
}
