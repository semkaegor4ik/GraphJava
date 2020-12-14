package com.university.graphs;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoruvkiThread implements Runnable{

    private final UIController controller;
    @Override
    public void run() {
        controller.start();
    }
}
