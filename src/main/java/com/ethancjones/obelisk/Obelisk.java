/* ==========================================================
 * Author : Ethan Jones
 * Date   : 27/05/2024
 * TODO   : Nothing
 * Uses   : Called when the game starts by the fabric API to
 * initialise the client
 * ==========================================================
 */
package com.ethancjones.obelisk;

import com.ethancjones.obelisk.command.CommandAPI;
import com.ethancjones.obelisk.keybind.KeybindAPI;
import com.ethancjones.obelisk.module.ModuleAPI;
import net.fabricmc.api.ModInitializer;

public class Obelisk implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        KeybindAPI.initialise();
        CommandAPI.initialise();
        ModuleAPI.initialise();
    }
}
