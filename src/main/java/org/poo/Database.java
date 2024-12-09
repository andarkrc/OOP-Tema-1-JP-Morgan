package org.poo;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class Database {
    private ArrayList<DatabaseEntry> entries;
    private HashMap<String, DatabaseEntry> emailDatabase;
    private HashMap<String, DatabaseEntry> IBANDatabase;

    public Database() {
        entries = new ArrayList<>();
        emailDatabase = new HashMap<>();
        IBANDatabase = new HashMap<>();
    }

    public void addUser(User user) {
        DatabaseEntry entry = new DatabaseEntry(user);
        entries.add(entry);
        emailDatabase.put(user.getEmail(), entry);
    }
}
