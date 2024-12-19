package org.poo.bank;

import org.poo.fileio.ExchangeInput;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

public final class CurrencyExchange {
    private static CurrencyExchange instance = null;
    private HashMap<String, Double> rates;

    private CurrencyExchange() {
        rates = new HashMap<>();
    }

    /**
     * Returns the instance of the currency exchanger.
     * If there is no instance, it will create a new instance and return it.
     *
     * @return
     */
    public static CurrencyExchange getInstance() {
        if (instance == null) {
            instance = new CurrencyExchange();
        }
        return instance;
    }

    /**
     * Initializes the exchange rates using the provided input.
     *
     * @param input
     */
    public void init(final ExchangeInput[] input) {
        rates = new HashMap<>();
        for (ExchangeInput in : input) {
            rates.put(in.getFrom() + "-" + in.getTo(), in.getRate());
            rates.put(in.getTo() + "-" + in.getFrom(), 1.0 / in.getRate());
        }
    }

    /**
     * Returns the exchange rate from currency1 to currency2.
     * Completes the chain of rates from currency1 to currency2 if there is no direct link.
     *
     * @param from      currency1
     * @param to        currency2
     * @return          exchange rate
     */
    public double getRate(final String from, final String to) {
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
        // This return should never hit.
        // It would mean that there is an isolated exchange rate,
        // which is not possible.
        return -1;
    }
}
