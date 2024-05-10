package com.hubspot.api;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.hubspot.api.model.Event;

@SpringBootTest
class HubspotApiApplicationTests {

    @Test
    void contextLoads() {
    }

    public List<Event> generateTestData() {
        Event one = new Event("/pages/a-big-river", "d1177368-2310-11e8-9e2a-9b860a0d9039", 1512754583000l);
        Event two = new Event("/pages/a-small-dog", "d1177368-2310-11e8-9e2a-9b860a0d9039", 1512754631000l);
        Event tree = new Event("/pages/a-big-talk", "f877b96c-9969-4abc-bbe2-54b17d030f8b", 1512709065294l);

        Event four = new Event("/pages/a-sad-story", "f877b96c-9969-4abc-bbe2-54b17d030f8b", 1512711000000l);
        Event five = new Event("/pages/a-big-river", "d1177368-2310-11e8-9e2a-9b860a0d9039", 1512754436000l);
        Event six = new Event("/pages/a-sad-story", "f877b96c-9969-4abc-bbe2-54b17d030f8b", 1512709024000l);
        List<Event> list = new ArrayList<>();
        list.add(one);
        list.add(two);
        list.add(tree);
        list.add(four);
        list.add(five);
        list.add(six);
        return list;
    }

}
