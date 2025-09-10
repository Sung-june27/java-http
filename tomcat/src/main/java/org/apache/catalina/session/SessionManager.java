package org.apache.catalina.session;

import java.util.HashMap;
import java.util.Map;
import org.apache.catalina.Manager;

public class SessionManager implements Manager {

    private static class SessionManagerHolder {
        private static final SessionManager INSTANCE = new SessionManager();
    }

    private static final Map<String, Session> SESSIONS = new HashMap<>();

    public static SessionManager getInstance() {
        return SessionManager.SessionManagerHolder.INSTANCE;
    }

    @Override
    public void add(final Session session) {
        SESSIONS.put(session.getId(), session);
    }

    @Override
    public Session findSession(final String id) {
        return SESSIONS.get(id);
    }

    @Override
    public void remove(final String id) {
        SESSIONS.remove(id);
    }

    private SessionManager() {}
}
