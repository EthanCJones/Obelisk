/* ==========================================================
 * Author : Ethan Jones
 * Date   : 03/06/2024
 * TODO   : Nothing
 * Uses   : Used to allow the player to specify if the server
 * has an anti-cheat or not. Modules will change their
 * behaviour to comply with different rules
 * ==========================================================
 */
package com.ethancjones.obelisk.module.modules;

import com.ethancjones.obelisk.module.Module;

public class AntiCheat extends Module
{
    public AntiCheat()
    {
        super("AntiCheat", 0, 0);
    }
}
