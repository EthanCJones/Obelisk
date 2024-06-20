/* ==========================================================
 * Author : Ethan Jones
 * Date   : 18/06/2024
 * TODO   : Nothing
 * Uses   : Stops the player from being pushed by water and
 * entities
 * ==========================================================
 */
package com.ethancjones.obelisk.module.modules;

import com.ethancjones.obelisk.command.Command;
import com.ethancjones.obelisk.module.Module;

public class AntiPush extends Module
{
    public final Command<Boolean> entities;
    public final Command<Boolean> water;

    public AntiPush()
    {
        super("AntiPush", 0, 0);
        entities = new Command<>(getName(), "entities", true);
        water = new Command<>(getName(), "water", true);
        toggle();
    }
}
