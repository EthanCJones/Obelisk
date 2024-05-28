/* ==========================================================
 * Author : Ethan Jones
 * Date   : 28/05/2024
 * TODO   : Nothing
 * Uses   : The base of keybinds
 * Keybinds can be assigned to run commands
 * ==========================================================
 */
package com.ethancjones.obelisk.keybind;

public abstract class Keybind implements Runnable
{
    private final int key;

    public Keybind(int key)
    {
        this.key = key;
    }

    public int getKey()
    {
        return key;
    }
}
