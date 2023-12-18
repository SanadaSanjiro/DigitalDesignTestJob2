package com.digdes.school;

import java.util.List;
import java.util.Map;

public enum Query {
    INSERT(new Command() {
        @Override
        public List<Map<String, Object>> execute(Storage storage, String args) {
            return null;
        }
    }),
    DELETE(new Command() {
        @Override
        public List<Map<String, Object>> execute(Storage storage, String args) {
            return null;
        }
    }),
    SELECT(new Command() {
        @Override
        public List<Map<String, Object>> execute(Storage storage, String args) {
            return null;
        }
    }),
    UPDATE(new Command() {
        @Override
        public List<Map<String, Object>> execute(Storage storage, String args) {
            return null;
        }
    });
    private final Command command;
    Query(Command c) {
        this.command = c;
    }

    public static List<Map<String, Object>> parseQuery(String request) {
        String[] command = request.split(" ");
        Query query = Query.valueOf(command[0]);
        switch (query) {
            case INSERT-> {

            }

        }
        return null;
    }
}
