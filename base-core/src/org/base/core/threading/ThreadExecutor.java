/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.threading;

import java.util.concurrent.TimeoutException;

/**
 *
 * @author martin
 */
public class ThreadExecutor {

    public static void execute(Thread task, long timeout) throws TimeoutException {
        task.start();
        try {
            task.join(timeout);
        } catch (InterruptedException e) {
            /* if somebody interrupts us he knows what he is doing */
        }
        if (task.isAlive()) {
            task.interrupt();
            throw new TimeoutException();
        }
    }
}
