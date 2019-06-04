package com.furioussoulk.collector.stream.worker.collector.stream.worker.cache;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Window<WINDOW_COLLECTION extends Collection> {

    private AtomicInteger windowSwitch = new AtomicInteger(0);

    private WINDOW_COLLECTION pointer;

    private WINDOW_COLLECTION windowDataA;
    private WINDOW_COLLECTION windowDataB;

    protected Window() {
        this.windowDataA = collectionInstance();
        this.windowDataB = collectionInstance();
        this.pointer = windowDataA;
    }

    public abstract WINDOW_COLLECTION collectionInstance();

    public boolean trySwitchPointer() {
        return windowSwitch.incrementAndGet() == 1 && !getLast().isReading();
    }

    public void trySwitchPointerFinally() {
        windowSwitch.addAndGet(-1);
    }

    public void switchPointer() {
        if (pointer == windowDataA) {
            pointer = windowDataB;
        } else {
            pointer = windowDataA;
        }
        getLast().reading();
    }

    protected WINDOW_COLLECTION getCurrentAndWriting() {
        if (pointer == windowDataA) {
            windowDataA.writing();
            return windowDataA;
        } else {
            windowDataB.writing();
            return windowDataB;
        }
    }

    protected WINDOW_COLLECTION getCurrent() {
        return pointer;
    }

    public WINDOW_COLLECTION getLast() {
        if (pointer == windowDataA) {
            return windowDataB;
        } else {
            return windowDataA;
        }
    }

    public void finishReadingLast() {
        getLast().clear();
        getLast().finishReading();
    }
}
